package com.msabhi.shared.basic

import com.msabhi.shared.Store
import com.msabhi.shared.common.Action
import com.msabhi.shared.common.State
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class BasicStoreV2<S : State>(
    initialState: S,
    override val reduce: (Action, S) -> S,
    scope: CoroutineScope,
) : Store<S> {

    private val actions: Channel<Action> =
        Channel(capacity = Channel.UNLIMITED, onBufferOverflow = BufferOverflow.SUSPEND)

    private val mutableState = MutableSharedFlow<S>(
        replay = 1,
        extraBufferCapacity = Int.MAX_VALUE,
        onBufferOverflow = BufferOverflow.SUSPEND,
    ).apply { tryEmit(initialState) }
    val states: Flow<S> = mutableState

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

    override fun state(): S = mutableState.replayCache.last()

    private suspend fun handle(action: Action) {
        val newState = reduce(action, mutableState.replayCache.last())
        mutableState.emit(newState)
    }
}