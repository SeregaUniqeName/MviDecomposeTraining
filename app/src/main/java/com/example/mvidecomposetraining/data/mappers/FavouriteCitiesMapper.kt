package com.example.mvidecomposetraining.data.mappers

import com.example.mvidecomposetraining.data.local.models.CityDbModel
import com.example.mvidecomposetraining.domain.entities.City

fun City.toDbModel() = CityDbModel(id, country, name)

fun CityDbModel.toEntity() = City(id, name, country)

fun List<CityDbModel>.toEntities() = map { it.toEntity() }