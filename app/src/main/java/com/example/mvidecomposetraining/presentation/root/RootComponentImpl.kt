package com.example.mvidecomposetraining.presentation.root

import android.os.Parcelable
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.push
import com.arkivanov.decompose.value.Value
import com.example.mvidecomposetraining.domain.entities.City
import com.example.mvidecomposetraining.presentation.details.DetailsComponentImpl
import com.example.mvidecomposetraining.presentation.favourite.FavouritesComponentImpl
import com.example.mvidecomposetraining.presentation.search.OpenReason
import com.example.mvidecomposetraining.presentation.search.SearchComponentImpl
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.parcelize.Parcelize

class RootComponentImpl @AssistedInject constructor(
    private val detailsComponentFactory: DetailsComponentImpl.Factory,
    private val searchComponentFactory: SearchComponentImpl.Factory,
    private val favouritesComponentFactory: FavouritesComponentImpl.Factory,
    @Assisted("componentContext") componentContext: ComponentContext
) : RootComponent, ComponentContext by componentContext {

    private val navigation = StackNavigation<Config>()

    override val stack: Value<ChildStack<*, RootComponent.Child>> =
        childStack(
            source = navigation,
            serializer = null,
            initialConfiguration = Config.Favourite,
            handleBackButton = true,
            childFactory = ::child
        )

    private fun child(
        config: Config,
        componentContext: ComponentContext
    ) : RootComponent.Child {
        return when(config) {
            is Config.Details -> {
                val component = detailsComponentFactory.create(
                    config.city,
                    onClickedBack = {
                        navigation.pop()
                    },
                    componentContext = componentContext
                )
                RootComponent.Child.Details(component)
            }
            Config.Favourite -> {
                val component = favouritesComponentFactory.create(
                    onCityItemClicked = {
                        navigation.push(Config.Details(it))
                    },
                    onAddFavouriteClicked = {
                        navigation.push(Config.Search(OpenReason.ADD_TO_FAVOURITE))
                    },
                    onSearchClicked = {
                        navigation.push(Config.Search(OpenReason.SEARCH))
                    },
                    componentContext = componentContext
                )
                RootComponent.Child.Favourites(component)
            }
            is Config.Search -> {
                val component = searchComponentFactory.create(
                    openReason = config.openReason,
                    onClickedBack = {
                        navigation.pop()
                    },
                    onCitySavedToFavourite = {
                        navigation.pop()
                    },
                    onForecastRequested = {
                        navigation.push(Config.Details(it))
                    },
                    componentContext = componentContext
                )
                RootComponent.Child.Search(component)
            }
        }
    }

    sealed interface Config: Parcelable {

        @Parcelize
        data object Favourite: Config

        @Parcelize
        data class Search(val openReason: OpenReason): Config

        @Parcelize
        data class Details(val city: City) : Config
    }

    @AssistedFactory
    interface Factory {

        fun create(
            @Assisted("componentContext") componentContext: ComponentContext
        ) : RootComponentImpl
    }
}