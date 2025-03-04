package com.example.mvidecomposetraining.data

import com.example.mvidecomposetraining.data.local.db.FavouriteCitiesDao
import com.example.mvidecomposetraining.data.mappers.toDbModel
import com.example.mvidecomposetraining.data.mappers.toEntities
import com.example.mvidecomposetraining.domain.FavouriteRepository
import com.example.mvidecomposetraining.domain.entities.City
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class FavouriteRepositoryImpl @Inject constructor(
    private val favouriteCitiesDao: FavouriteCitiesDao,
) : FavouriteRepository {

    override val favouriteCities: Flow<List<City>> = favouriteCitiesDao.getFavouriteCities().map { it.toEntities() }

    override fun observeIsFavourite(cityId: Int) = favouriteCitiesDao.observeIsFavourite(cityId)

    override suspend fun addToFavourite(city: City) = favouriteCitiesDao.addToFavourite(city.toDbModel())

    override suspend fun deleteFromFavourite(cityId: Int) = favouriteCitiesDao.removeFromFavourite(cityId)


}