package com.example.mvidecomposetraining.domain.useCases.search

import com.example.mvidecomposetraining.domain.SearchRepository
import javax.inject.Inject

class SearchCityUseCase @Inject constructor(
    private val repository: SearchRepository
) {

    suspend fun search(query: String) = repository.search(query)
}