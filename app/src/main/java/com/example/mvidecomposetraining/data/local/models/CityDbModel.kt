package com.example.mvidecomposetraining.data.local.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favourite_cities")
data class CityDbModel(
    @PrimaryKey val id: Int,
    val name: String,
    val country: String
)
