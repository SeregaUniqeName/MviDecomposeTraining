package com.example.mvidecomposetraining.data

import com.example.mvidecomposetraining.data.mappers.toEntity
import com.example.mvidecomposetraining.data.remote.api.ApiService
import com.example.mvidecomposetraining.domain.Repository
import com.example.mvidecomposetraining.domain.entities.Forecast
import com.example.mvidecomposetraining.domain.entities.Weather
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : Repository {
    override suspend fun getWeather(cityId: Int): Weather {
        return apiService.loadCurrentWeather("$PREFIX_ID$cityId").toEntity()
    }

    override suspend fun getForecast(cityId: Int): Forecast {
        return apiService.loadForecast("$PREFIX_ID$cityId").toEntity()
    }

    private companion object {
        private const val PREFIX_ID = "id:"
    }
}

