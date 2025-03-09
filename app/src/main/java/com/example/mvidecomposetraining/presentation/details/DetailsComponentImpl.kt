package com.example.mvidecomposetraining.presentation.details

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.example.mvidecomposetraining.domain.entities.City
import com.example.mvidecomposetraining.presentation.utils.componentScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DetailsComponentImpl @AssistedInject constructor(
    private val detailsStoreFactory: DetailsStoreFactory,
    @Assisted("city") private val city: City,
    @Assisted("onClickedBack") private val onClickedBack: () -> Unit,
    @Assisted("componentContext") componentContext: ComponentContext,
) : DetailsComponent, ComponentContext by componentContext {

    private val store = instanceKeeper.getStore { detailsStoreFactory.create(city) }

    private val scope = componentScope()

    init {
        scope.launch {
            store.labels.collect {
                when(it) {
                    DetailsStore.Label.ClickBack -> onClickedBack()
                }
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val model: StateFlow<DetailsStore.State> = store.stateFlow

    override fun onClickBack() {
        store.accept(DetailsStore.Intent.ClickBack)
    }

    override fun onClickChangeFavouriteStatus() {
        store.accept(DetailsStore.Intent.ClickChangeFavouriteStatus)
    }

    @AssistedFactory
    interface Factory{

        fun create(
            @Assisted("city") city: City,
            @Assisted("onClickedBack") onClickedBack: () -> Unit,
            @Assisted("componentContext") componentContext: ComponentContext,
        ) : DetailsComponentImpl
    }
}