package com.example.mvidecomposetraining.presentation.details

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.example.mvidecomposetraining.presentation.details.DetailsStore.*

interface DetailsStore : Store<Intent, State, Label> {

    sealed interface Intent {

    }

    data class State(val todo: Unit)

    sealed interface Label {

    }

}

internal class DetailsStoreFactory(
    private val storeFactory: StoreFactory
) {

    fun create(): DetailsStore =
        object : DetailsStore, Store<Intent, State, Label> by storeFactory
            .create(
                name = "DetailsStore",
                initialState = State(Unit),
                bootstrapper = BootstrapperImpl(),
                executorFactory = ::ExecutorImpl,
                reducer = ReducerImpl
            ) {}

    private sealed interface Action {

    }

    private sealed interface Msg {

    }

    private class BootstrapperImpl : CoroutineBootstrapper<Action>() {
        override fun invoke() {
            TODO("Not yet implemented")
        }

    }

    private class ExecutorImpl :
        CoroutineExecutor<Intent, Action, State, Msg, Label>() {

        override fun executeAction(action: Action, getState: () -> State) {
            super.executeAction(action, getState)
        }

        override fun executeIntent(intent: Intent, getState: () -> State) {
            super.executeIntent(intent, getState)
        }
    }

    private object ReducerImpl: Reducer<State, Msg>{

        override fun State.reduce(msg: Msg) = State(Unit)

    }

}