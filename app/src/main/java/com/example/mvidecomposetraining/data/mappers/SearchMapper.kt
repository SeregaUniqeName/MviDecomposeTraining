package com.example.mvidecomposetraining.data.mappers

import com.example.mvidecomposetraining.data.remote.models.CityDto
import com.example.mvidecomposetraining.domain.entities.City

fun CityDto.toEntity() = City(this.id, this.cityName, this.country)
fun List<CityDto>.toEntities() : List<City> {
    return map{it.toEntity()}
}
