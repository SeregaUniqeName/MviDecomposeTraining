package com.example.mvidecomposetraining.presentation.details

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.example.mvidecomposetraining.domain.entities.City
import com.example.mvidecomposetraining.domain.entities.Forecast
import com.example.mvidecomposetraining.domain.useCases.details.GetWeatherAndForecastUseCase
import com.example.mvidecomposetraining.domain.useCases.favourite.ChangeFavouriteCityUseCase
import com.example.mvidecomposetraining.domain.useCases.favourite.ObserveFavouriteCityStateUseCase
import com.example.mvidecomposetraining.presentation.details.DetailsStore.Intent
import com.example.mvidecomposetraining.presentation.details.DetailsStore.Label
import com.example.mvidecomposetraining.presentation.details.DetailsStore.State
import kotlinx.coroutines.launch
import javax.inject.Inject

interface DetailsStore : Store<Intent, State, Label> {

    sealed interface Intent {

        data object ClickBack : Intent

        data object ClickChangeFavouriteStatus : Intent

    }

    data class State(
        val city: City,
        val isFavourite: Boolean,
        val forecastState: ForecastState
    ) {

        sealed interface ForecastState {

            data object Initial : ForecastState

            data object Loading : ForecastState

            data object Error : ForecastState

            data class Loaded(
                val forecast: Forecast
            ) : ForecastState
        }
    }

    sealed interface Label {

        data object ClickBack : Label
    }

}

internal class DetailsStoreFactory @Inject constructor(
    private val storeFactory: StoreFactory,
    private val getWeatherAndForecastUseCase: GetWeatherAndForecastUseCase,
    private val changeFavouriteCityUseCase: ChangeFavouriteCityUseCase,
    private val observeFavouriteCityStateUseCase: ObserveFavouriteCityStateUseCase
) {

    fun create(city: City): DetailsStore =
        object : DetailsStore, Store<Intent, State, Label> by storeFactory
            .create(
                name = "DetailsStore",
                initialState = State(
                    city = city,
                    isFavourite = false,
                    forecastState = State.ForecastState.Initial
                ),
                bootstrapper = BootstrapperImpl(city.id),
                executorFactory = ::ExecutorImpl,
                reducer = ReducerImpl
            ) {}

    private sealed interface Action {

        data class FavouriteStatusChanged(val isFavourite: Boolean) : Action

        data class ForecastLoaded(val forecast: Forecast) : Action

        data object ForecastStartLoading : Action

        data object ForecastLoadingError : Action
    }

    private sealed interface Msg {

        data class FavouriteStatusChanged(val isFavourite: Boolean) : Msg

        data class ForecastLoaded(val forecast: Forecast) : Msg

        data object ForecastStartLoading : Msg

        data object ForecastLoadingError : Msg
    }

    private inner class BootstrapperImpl(private val cityId: Int) : CoroutineBootstrapper<Action>() {
        override fun invoke() {
            scope.launch {
                observeFavouriteCityStateUseCase(cityId = cityId).collect {
                    dispatch(Action.FavouriteStatusChanged(it))
                }
            }
            scope.launch {
                dispatch(Action.ForecastStartLoading)
                try {
                    val forecast = getWeatherAndForecastUseCase.getForecast(cityId)
                    dispatch(Action.ForecastLoaded(forecast))
                } catch (e: Exception) {
                    dispatch(Action.ForecastLoadingError)
                }

            }
        }

    }

    private inner class ExecutorImpl :
        CoroutineExecutor<Intent, Action, State, Msg, Label>() {

        override fun executeAction(action: Action, getState: () -> State) {
            when(action){
                is Action.FavouriteStatusChanged -> {
                    dispatch(Msg.FavouriteStatusChanged(action.isFavourite))
                }
                is Action.ForecastLoaded -> {
                    dispatch(Msg.ForecastLoaded(action.forecast))
                }
                Action.ForecastLoadingError -> {
                    dispatch(Msg.ForecastLoadingError)
                }
                Action.ForecastStartLoading -> {
                    dispatch(Msg.ForecastStartLoading)
                }
            }
        }

        override fun executeIntent(intent: Intent, getState: () -> State) {
            when(intent) {
                Intent.ClickBack -> {
                    publish(Label.ClickBack)
                }
                Intent.ClickChangeFavouriteStatus -> {
                    scope.launch {
                        val state = getState()
                        if (state.isFavourite){
                            changeFavouriteCityUseCase.deleteCity(state.city.id)
                        } else {
                            changeFavouriteCityUseCase.addCity(state.city)
                        }
                    }
                }
            }
        }
    }

    private object ReducerImpl : Reducer<State, Msg> {

        override fun State.reduce(msg: Msg) = when(msg) {
            is Msg.FavouriteStatusChanged -> {
                copy(
                    isFavourite = msg.isFavourite
                )
            }
            is Msg.ForecastLoaded -> {
                copy(forecastState = State.ForecastState.Loaded(msg.forecast))
            }
            Msg.ForecastLoadingError -> {
                copy(forecastState = State.ForecastState.Error)
            }
            Msg.ForecastStartLoading -> {
                copy(forecastState = State.ForecastState.Loading)
            }
        }

    }

}