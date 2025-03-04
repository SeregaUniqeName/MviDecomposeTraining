package com.example.mvidecomposetraining.domain.useCases.details

import com.example.mvidecomposetraining.domain.WeatherRepository
import javax.inject.Inject

class GetWeatherAndForecastUseCase @Inject constructor(
    private val repository: WeatherRepository
) {

    suspend fun getWeather(cityId: Int) = repository.getWeather(cityId)

    suspend fun getForecast(cityId: Int) = repository.getForecast(cityId)
}