package com.msabhi.shared.basic

import com.msabhi.shared.Store
import com.msabhi.shared.common.Action
import com.msabhi.shared.common.State
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class BasicStoreV1<S : State>(
    initialState: S,
    override val reduce: (Action, S) -> S,
    scope: CoroutineScope,
) : Store<S> {

    private val actions: Channel<Action> =
        Channel(capacity = Channel.UNLIMITED, onBufferOverflow = BufferOverflow.SUSPEND)

    private val mutableState = MutableStateFlow(initialState)
    override val states: Flow<S> = mutableState

    private var state: S = initialState

    init {

        scope.launch {
            while (isActive) {
                for (action in actions) {
                    handle(action)
                }
            }
        }
    }

    override fun dispatch(action: Action) {
        actions.trySend(action)
    }

    override fun state(): S = mutableState.value

    private suspend fun handle(action: Action) {
        val newState = reduce(action, state)
        state = newState
        mutableState.emit(state)
    }
}