package com.example.mvidecomposetraining.domain

import com.example.mvidecomposetraining.domain.entities.City

interface SearchRepository {

    suspend fun search(query: String) : List<City>
}