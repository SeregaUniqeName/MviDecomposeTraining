package com.example.mvidecomposetraining.data.models

import com.google.gson.annotations.SerializedName

data class ForecastDto(
    @SerializedName("forecastday") val list: List<DayDto>
)
