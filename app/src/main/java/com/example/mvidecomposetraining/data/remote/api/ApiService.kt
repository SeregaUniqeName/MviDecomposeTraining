package com.example.mvidecomposetraining.data.remote.api

import com.example.mvidecomposetraining.data.remote.models.CityDto
import com.example.mvidecomposetraining.data.remote.models.WeatherCurrentDto
import com.example.mvidecomposetraining.data.remote.models.WeatherForecastDto
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("current.json")
    suspend fun loadCurrentWeather(
        @Query("q") query: String
    ) : WeatherCurrentDto

    @GET("forecast.json")
    suspend fun loadForecast(
        @Query("q") query: String,
        @Query("days") days: Int = 4
    ) : WeatherForecastDto

    @GET("search.json")
    suspend fun searchCity(
        @Query("q") query: String
    ) : List<CityDto>
}