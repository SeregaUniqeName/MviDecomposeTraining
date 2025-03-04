package com.example.mvidecomposetraining.presentation.search

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.example.mvidecomposetraining.domain.entities.City
import com.example.mvidecomposetraining.domain.useCases.favourite.ChangeFavouriteCityUseCase
import com.example.mvidecomposetraining.domain.useCases.search.SearchCityUseCase
import com.example.mvidecomposetraining.presentation.search.SearchStore.Intent
import com.example.mvidecomposetraining.presentation.search.SearchStore.Label
import com.example.mvidecomposetraining.presentation.search.SearchStore.State
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

interface SearchStore : Store<Intent, State, Label> {

    sealed interface Intent {

        data class ChangeSearchQuery(val input: String) : Intent

        data object ClickBack : Intent

        data object ClickSearch : Intent

        data class ClickCity(val city: City) : Intent
    }

    data class State(
        val searchQuery: String,
        val searchState: SearchState
    ) {

        sealed interface SearchState {

            data object Initial : SearchState

            data object Loading : SearchState

            data object Error : SearchState

            data object EmptyResult : SearchState

            data class Loaded(val cities: List<City>) : SearchState
        }
    }

    sealed interface Label {

        data object ClickBack : Label

        data object SaveToFavourite : Label

        data class OpenForecast(val city: City) : Label
    }
}

class SearchStoreFactory @Inject constructor(
    private val storeFactory: StoreFactory,
    private val searchCityUseCase: SearchCityUseCase,
    private val changeFavouriteCityUseCase: ChangeFavouriteCityUseCase,
) {

    fun create(openReason: OpenReason): SearchStore =
        object : SearchStore, Store<Intent, State, Label> by storeFactory.create(
            name = "SearchStore",
            initialState = State(
                searchQuery = "",
                searchState = State.SearchState.Initial
            ),
            bootstrapper = BootstrapperImpl(),
            executorFactory = { ExecutorImpl(openReason) },
            reducer = ReducerImpl
        ) {}

    private sealed interface Action {


    }

    private sealed interface Msg {

        data class ChangeSearchQuery(val query: String) : Msg

        data object LoadingSearchResult : Msg

        data object SearchResultError : Msg

        data class SearchResultLoaded(val cities: List<City>) : Msg
    }

    private class BootstrapperImpl : CoroutineBootstrapper<Action>() {
        override fun invoke() {
        }
    }

    private inner class ExecutorImpl(
        private val openReason: OpenReason
    ) : CoroutineExecutor<Intent, Action, State, Msg, Label>() {

        private var searchJob: Job? = null

        override fun executeIntent(intent: Intent, getState: () -> State) {

            when (intent) {
                is Intent.ChangeSearchQuery -> {
                    dispatch(Msg.ChangeSearchQuery(intent.input))
                }

                Intent.ClickBack -> {
                    publish(Label.ClickBack)
                }

                is Intent.ClickCity -> {
                    when (openReason) {
                        OpenReason.SEARCH -> {
                            publish(Label.OpenForecast(intent.city))
                        }

                        OpenReason.ADD_TO_FAVOURITE -> {
                            scope.launch {
                                changeFavouriteCityUseCase.addCity(intent.city)
                                publish(Label.SaveToFavourite)
                            }
                        }
                    }

                }

                Intent.ClickSearch -> {
                    searchJob?.cancel()
                    searchJob = scope.launch {
                        dispatch(Msg.LoadingSearchResult)
                        try {
                            val cities = searchCityUseCase.search(getState().searchQuery)
                            dispatch(Msg.SearchResultLoaded(cities))
                        } catch (e: Exception) {
                            dispatch(Msg.SearchResultError)
                        }
                    }
                }
            }
        }

        override fun executeAction(action: Action, getState: () -> State) {
        }
    }

    private object ReducerImpl : Reducer<State, Msg> {
        override fun State.reduce(msg: Msg) = when (msg) {
            is Msg.ChangeSearchQuery -> {
                copy(
                    searchQuery = msg.query
                )
            }

            Msg.LoadingSearchResult -> {
                copy(
                    searchState = State.SearchState.Loading
                )
            }

            Msg.SearchResultError -> {
                copy(
                    searchState = State.SearchState.Error
                )
            }

            is Msg.SearchResultLoaded -> {
                copy(
                    searchState = if (msg.cities.isEmpty()) {
                        State.SearchState.EmptyResult
                    } else {
                        State.SearchState.Loaded(msg.cities)
                    }
                )
            }
        }
    }
}
