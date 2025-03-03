package com.example.mvidecomposetraining.data.remote.models

import com.google.gson.annotations.SerializedName

data class ForecastDto(
    @SerializedName("forecastday") val list: List<DayDto>
)
