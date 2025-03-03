package com.example.mvidecomposetraining.data.remote.models

import com.google.gson.annotations.SerializedName

data class CityDto(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val cityName: String,
    @SerializedName("country") val country: String
)
