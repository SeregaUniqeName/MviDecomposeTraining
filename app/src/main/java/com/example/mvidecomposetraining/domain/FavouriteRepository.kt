package com.example.mvidecomposetraining.domain

import com.example.mvidecomposetraining.domain.entities.City
import kotlinx.coroutines.flow.Flow

interface FavouriteRepository {

    val favouriteCities: Flow<City>

    fun observeIsFavourite(cityId: Int): Flow<Boolean>

    suspend fun addToFavourite(city: City)

    suspend fun deleteFromFavourite(cityId: Int)
}