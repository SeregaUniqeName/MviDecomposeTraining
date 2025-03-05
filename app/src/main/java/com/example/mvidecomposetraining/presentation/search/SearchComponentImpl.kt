package com.example.mvidecomposetraining.presentation.search

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.example.mvidecomposetraining.domain.entities.City
import com.example.mvidecomposetraining.presentation.componentScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SearchComponentImpl @AssistedInject constructor(
    private val searchStoreFactory: SearchStoreFactory,
    @Assisted("openReason") private val openReason: OpenReason,
    @Assisted("onClickedBack") private val onClickedBack: () -> Unit,
    @Assisted("onCitySavedToFavourite") private val onCitySavedToFavourite: () -> Unit,
    @Assisted("onForecastRequested") private val onForecastRequested: (City) -> Unit,
    @Assisted("componentContext") componentContext: ComponentContext
) : SearchComponent, ComponentContext by componentContext {

    private val store = instanceKeeper.getStore { searchStoreFactory.create(openReason) }
    private val scope = componentScope()

    init {
        scope.launch {
            store.labels.collect {
                when (it) {
                    SearchStore.Label.ClickBack -> {
                        onClickedBack()
                    }

                    is SearchStore.Label.OpenForecast -> {
                        onForecastRequested(it.city)
                    }

                    SearchStore.Label.SaveToFavourite -> {
                        onCitySavedToFavourite()
                    }
                }
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val model: StateFlow<SearchStore.State> = store.stateFlow

    override fun changeSearchQuery(query: String) {
        store.accept(SearchStore.Intent.ChangeSearchQuery(query))
    }

    override fun onClickBack() {
        store.accept(SearchStore.Intent.ClickBack)
    }

    override fun onClickSearch() {
        store.accept(SearchStore.Intent.ClickSearch)
    }

    override fun onCityClick(city: City) {
        store.accept(SearchStore.Intent.ClickCity(city))
    }

    @AssistedFactory
    interface Factory {

        fun create(
            @Assisted("openReason") openReason: OpenReason,
            @Assisted("onClickedBack") onClickedBack: () -> Unit,
            @Assisted("onCitySavedToFavourite") onCitySavedToFavourite: () -> Unit,
            @Assisted("onForecastRequested") onForecastRequested: (City) -> Unit,
            @Assisted("componentContext") componentContext: ComponentContext
        ): SearchComponentImpl
    }
}