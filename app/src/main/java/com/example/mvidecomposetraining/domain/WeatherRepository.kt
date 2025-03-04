package com.example.mvidecomposetraining.domain

import com.example.mvidecomposetraining.domain.entities.Forecast
import com.example.mvidecomposetraining.domain.entities.Weather

interface WeatherRepository {

    suspend fun getWeather(cityId: Int): Weather

    suspend fun getForecast(cityId: Int): Forecast

}