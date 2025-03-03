package com.example.mvidecomposetraining.data.remote.models

import com.google.gson.annotations.SerializedName

data class DayDto(
    @SerializedName("date_epoch") val date: Long,
    @SerializedName("day") val days: DayWeatherDto
)
