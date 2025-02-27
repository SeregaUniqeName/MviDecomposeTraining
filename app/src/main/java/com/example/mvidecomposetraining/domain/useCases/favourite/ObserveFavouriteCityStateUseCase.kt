package com.example.mvidecomposetraining.domain.useCases.favourite

import com.example.mvidecomposetraining.domain.FavouriteRepository
import javax.inject.Inject

class ObserveFavouriteCityStateUseCase @Inject constructor(
    private val repository: FavouriteRepository
) {

    operator fun invoke(cityId: Int) = repository.observeIsFavourite(cityId)
}