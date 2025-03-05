package com.example.mvidecomposetraining.presentation.favourite

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.example.mvidecomposetraining.domain.entities.City
import com.example.mvidecomposetraining.domain.useCases.details.GetWeatherAndForecastUseCase
import com.example.mvidecomposetraining.domain.useCases.favourite.GetFavouriteCitiesUseCase
import com.example.mvidecomposetraining.presentation.favourite.FavouriteStore.Intent
import com.example.mvidecomposetraining.presentation.favourite.FavouriteStore.Label
import com.example.mvidecomposetraining.presentation.favourite.FavouriteStore.State
import kotlinx.coroutines.launch
import javax.inject.Inject

interface FavouriteStore : Store<Intent, State, Label> {

    sealed interface Intent {

        data object ClickSearch : Intent

        data object ClickAddToFavourite : Intent

        data class CityItemClicked(val city: City) : Intent

    }

    data class State(
        val cityItems: List<CityItem>,
    ) {

        data class CityItem(
            val city: City,
            val state: WeatherState,
        )

        sealed interface WeatherState {

            data object Initial : WeatherState

            data object Loading : WeatherState

            data object Error : WeatherState

            data class Loaded(
                val temp: Float,
                val iconUrl: String,
            ) : WeatherState
        }
    }

    sealed interface Label {

        data object ClickSearch : Label

        data object ClickToFavourite : Label

        data class CityItemClicked(val city: City) : Label
    }
}

class FavouriteStoreFactory @Inject constructor(
    private val storeFactory: StoreFactory,
    private val getFavouriteCitiesUseCase: GetFavouriteCitiesUseCase,
    private val getWeatherAndForecastUseCase: GetWeatherAndForecastUseCase
) {

    fun create(): FavouriteStore = object : FavouriteStore,
        Store<Intent, State, Label> by storeFactory
            .create(
                name = "FavouriteStore",
                initialState = State(listOf()),
                bootstrapper = BootstrapperImpl(),
                executorFactory = ::ExecutorImpl
            ) {}

    private sealed interface Action {
        data class FavouriteCitiesLoaded(
            val cities: List<City>
        ) : Action
    }

    private sealed interface Msg {
        data class FavouriteCitiesLoaded(
            val cities: List<City>
        ) : Msg

        data class WeatherLoaded(
            val citiId: Int,
            val temp: Float,
            val iconUrl: String
        ) : Msg

        data class WeatherLoadedError(
            val citiId: Int
        ) : Msg

        data class WeatherIsLoading(
            val citiId: Int
        ) : Msg
    }

    private inner class BootstrapperImpl : CoroutineBootstrapper<Action>() {
        override fun invoke() {
            scope.launch {
                getFavouriteCitiesUseCase().collect {
                    dispatch(Action.FavouriteCitiesLoaded(it))
                }
            }
        }
    }

    private inner class ExecutorImpl : CoroutineExecutor<Intent, Action, State, Msg, Label>() {

        override fun executeAction(action: Action, getState: () -> State) {
            when (action) {
                is Action.FavouriteCitiesLoaded -> {
                    val cities = action.cities
                    dispatch(Msg.FavouriteCitiesLoaded(cities))
                    cities.forEach {
                        scope.launch {
                            loadWeatherForCity(it)
                        }
                    }
                }
            }
        }

        private suspend fun loadWeatherForCity(city: City) {
            dispatch(Msg.WeatherIsLoading(city.id))
            try {
                val weather = getWeatherAndForecastUseCase.getWeather(city.id)
                dispatch(
                    Msg.WeatherLoaded(
                        citiId = city.id,
                        temp = weather.tempC,
                        iconUrl = weather.condition
                    )
                )
            } catch (e: Exception) {
                dispatch(Msg.WeatherLoadedError(citiId = city.id))
            }
        }

        override fun executeIntent(intent: Intent, getState: () -> State) {
            when (intent) {
                is Intent.CityItemClicked -> {
                    publish(Label.CityItemClicked(intent.city))
                }

                Intent.ClickSearch -> {
                    publish(Label.ClickSearch)
                }

                Intent.ClickAddToFavourite -> {
                    publish(Label.ClickToFavourite)
                }
            }
        }
    }

    private class ReducerImpl : Reducer<State, Msg> {
        override fun State.reduce(msg: Msg) =
            when (msg) {
                is Msg.FavouriteCitiesLoaded -> {
                    copy(
                        cityItems = msg.cities.map {
                            State.CityItem(
                                city = it,
                                state = State.WeatherState.Initial
                            )
                        }
                    )
                }

                is Msg.WeatherIsLoading -> {
                    copy(
                        cityItems = cityItems.map {
                            if (it.city.id == msg.citiId) {
                                it.copy(
                                    state = State.WeatherState.Loading
                                )
                            } else {
                                it
                            }
                        }
                    )
                }

                is Msg.WeatherLoaded -> {
                    copy(
                        cityItems = cityItems.map {
                            if (it.city.id == msg.citiId) {
                                it.copy(
                                    state = State.WeatherState.Loaded(msg.temp, msg.iconUrl)
                                )
                            } else {
                                it
                            }
                        }
                    )
                }

                is Msg.WeatherLoadedError -> {
                    copy(
                        cityItems = cityItems.map {
                            if (it.city.id == msg.citiId) {
                                it.copy(
                                    state = State.WeatherState.Error
                                )
                            } else {
                                it
                            }
                        }
                    )
                }
            }
    }
}
