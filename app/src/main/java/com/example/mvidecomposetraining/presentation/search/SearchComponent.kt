package com.example.mvidecomposetraining.presentation.search

import com.example.mvidecomposetraining.domain.entities.City
import kotlinx.coroutines.flow.StateFlow

interface SearchComponent  {

    val model: StateFlow<SearchStore.State>

    fun changeSearchQuery(query: String)

    fun onClickBack()

    fun onClickSearch()

    fun onCityClick(city: City)
}