package com.example.mvidecomposetraining.domain.entities

import java.util.Calendar

data class Weather(
    val tempC: Float,
    val condition: String,
    val conditionUrl: String,
    val date: Calendar
)
