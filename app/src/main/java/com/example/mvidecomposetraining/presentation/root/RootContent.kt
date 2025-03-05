package com.example.mvidecomposetraining.presentation.root

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.example.mvidecomposetraining.presentation.details.DetailsContent
import com.example.mvidecomposetraining.presentation.favourite.FavouriteContent
import com.example.mvidecomposetraining.presentation.search.SearchContent
import com.example.mvidecomposetraining.ui.theme.MviDecomposeTrainingTheme

@Composable
fun RootContent(
    component: RootComponent
) {
    MviDecomposeTrainingTheme {
        Children(
            stack = component.stack
        ) {
            when (val instance = it.instance) {
                is RootComponent.Child.Details -> {
                    DetailsContent(component = instance.component)
                }
                is RootComponent.Child.Favourites -> {
                    FavouriteContent(component = instance.component)
                }
                is RootComponent.Child.Search -> {
                    SearchContent(component = instance.component)
                }
            }
        }
    }
}