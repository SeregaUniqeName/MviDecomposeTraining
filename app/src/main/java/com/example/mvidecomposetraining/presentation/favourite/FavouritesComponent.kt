package com.example.mvidecomposetraining.presentation.favourite

import com.example.mvidecomposetraining.domain.entities.City
import kotlinx.coroutines.flow.StateFlow

interface FavouritesComponent {

    val model: StateFlow<FavouriteStore.State>

    fun onClickSearch()

    fun onCLickAddFavourite()

    fun onCityItemClick(city: City)
}