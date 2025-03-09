package com.example.mvidecomposetraining.presentation.favourite

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.example.mvidecomposetraining.domain.entities.City
import com.example.mvidecomposetraining.presentation.utils.componentScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class FavouritesComponentImpl @AssistedInject constructor(
    private val favouriteStoreFactory: FavouriteStoreFactory,
    @Assisted("onCityItemClicked") private val onCityItemClicked: (City) -> Unit,
    @Assisted("onAddFavouriteClicked") private val onAddFavouriteClicked: () -> Unit,
    @Assisted("onSearchClicked") private val onSearchClicked: () -> Unit,
    @Assisted("componentContext") componentContext: ComponentContext,
) : FavouritesComponent, ComponentContext by componentContext {

    private val store = instanceKeeper.getStore {
        favouriteStoreFactory.create()
    }

    private val scope = componentScope()

    init {
        scope.launch {
            store.labels.collect {
                when(it){
                    is FavouriteStore.Label.CityItemClicked -> {
                        onCityItemClicked(it.city)
                    }
                    FavouriteStore.Label.ClickSearch -> {
                        onSearchClicked()
                    }
                    FavouriteStore.Label.ClickToFavourite -> {
                        onAddFavouriteClicked()
                    }
                }
            }
        }

    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val model: StateFlow<FavouriteStore.State> = store.stateFlow

    override fun onClickSearch() {
        store.accept(FavouriteStore.Intent.ClickSearch)
    }

    override fun onClickAddFavourite() {
        store.accept(FavouriteStore.Intent.ClickAddToFavourite)
    }

    override fun onCityItemClicked(city: City) {
        store.accept(FavouriteStore.Intent.CityItemClicked(city))
    }

    @AssistedFactory
    interface Factory {

        fun create(
            @Assisted("onCityItemClicked") onCityItemClicked: (City) -> Unit,
            @Assisted("onAddFavouriteClicked") onAddFavouriteClicked: () -> Unit,
            @Assisted("onSearchClicked") onSearchClicked: () -> Unit,
            @Assisted("componentContext") componentContext: ComponentContext
        ): FavouritesComponentImpl
    }
}