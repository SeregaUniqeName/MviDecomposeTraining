package com.example.mvidecomposetraining.domain.useCases.details

import com.example.mvidecomposetraining.domain.Repository
import javax.inject.Inject

class GetWeatherAndForecastUseCase @Inject constructor(
    private val repository: Repository
) {

    suspend fun getWeather(cityId: Int) = repository.getWeather(cityId)

    suspend fun getForecast(cityId: Int) = repository.getForecast(cityId)
}