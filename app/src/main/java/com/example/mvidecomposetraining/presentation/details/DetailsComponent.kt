package com.example.mvidecomposetraining.presentation.details

import kotlinx.coroutines.flow.StateFlow

interface DetailsComponent  {

    val model: StateFlow<DetailsStore.State>

    fun onClickBack()

    fun onClickChangeFavouriteStatus()
}