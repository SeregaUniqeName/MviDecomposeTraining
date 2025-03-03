package com.example.mvidecomposetraining.data

import com.example.mvidecomposetraining.data.mappers.toEntities
import com.example.mvidecomposetraining.data.remote.api.ApiService
import com.example.mvidecomposetraining.domain.SearchRepository
import com.example.mvidecomposetraining.domain.entities.City
import javax.inject.Inject

class SearchRepositoryImpl @Inject constructor(
    private val apiService: ApiService
): SearchRepository {

    override suspend fun search(query: String): List<City> {
        return apiService.searchCity(query).toEntities()
    }
}