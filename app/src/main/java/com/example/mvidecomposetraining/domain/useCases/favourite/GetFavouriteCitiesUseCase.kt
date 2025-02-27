package com.example.mvidecomposetraining.domain.useCases.favourite

import com.example.mvidecomposetraining.domain.FavouriteRepository
import javax.inject.Inject

class GetFavouriteCitiesUseCase @Inject constructor(
    private val repository: FavouriteRepository
) {

    operator fun invoke() = repository.favouriteCities
}