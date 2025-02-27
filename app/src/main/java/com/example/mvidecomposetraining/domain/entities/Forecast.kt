package com.example.mvidecomposetraining.domain.entities

data class Forecast(
    val current: Weather,
    val upcoming: List<Weather>
)
