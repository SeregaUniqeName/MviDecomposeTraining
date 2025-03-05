package com.example.mvidecomposetraining.presentation.root

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import com.example.mvidecomposetraining.presentation.details.DetailsComponent
import com.example.mvidecomposetraining.presentation.favourite.FavouritesComponent
import com.example.mvidecomposetraining.presentation.search.SearchComponent

interface RootComponent {

    val stack: Value<ChildStack<*, Child>>

    sealed interface Child {

        data class Favourites(
            val component: FavouritesComponent
        ) : Child

        data class Search(
            val component: SearchComponent
        ) : Child

        data class Details(
            val component: DetailsComponent
        ) : Child
    }
}