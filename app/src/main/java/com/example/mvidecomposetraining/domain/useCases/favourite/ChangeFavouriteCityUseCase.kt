package com.example.mvidecomposetraining.domain.useCases.favourite

import com.example.mvidecomposetraining.domain.FavouriteRepository
import com.example.mvidecomposetraining.domain.entities.City
import javax.inject.Inject

class ChangeFavouriteCityUseCase @Inject constructor(
    private val repository: FavouriteRepository
) {

    suspend fun addCity(city: City) = repository.addToFavourite(city)

    suspend fun deleteCity(cityId: Int) = repository.deleteFromFavourite(cityId)
}