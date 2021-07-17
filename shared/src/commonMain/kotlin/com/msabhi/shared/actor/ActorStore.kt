/*
 * Copyright (C) 2021 Abhi Muktheeswarar
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.msabhi.shared.actor

import com.msabhi.shared.Store
import com.msabhi.shared.common.Action
import com.msabhi.shared.common.State
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.selects.select

private fun <S : State> CoroutineScope.stateMachine(
    initialState: S,
    inputActions: ReceiveChannel<Action>,
    requestStates: ReceiveChannel<Unit>,
    sendStates: SendChannel<S>,
    setStates: MutableSharedFlow<S>,
    actions: MutableSharedFlow<Action>,
    reduce: (Action, S) -> S,
) = launch {

    var state = initialState

    while (isActive) {

        select<Unit> {

            inputActions.onReceive { action ->
                val newState = reduce(action, state)
                if (newState != state) {
                    state = newState
                    setStates.emit(state)
                }
                actions.emit(action)
            }

            requestStates.onReceive {
                sendStates.send(state)
            }
        }
    }
}

class ActorStore<S : State>(
    private val initialState: S,
    override val reduce: (Action, S) -> S,
    val scope: CoroutineScope,
) : Store<S> {

    private val inputActionsChannel: Channel<Action> =
        Channel(capacity = Channel.UNLIMITED, onBufferOverflow = BufferOverflow.SUSPEND)

    private val requestStatesChannel: Channel<Unit> =
        Channel(capacity = Channel.UNLIMITED, onBufferOverflow = BufferOverflow.SUSPEND)
    private val sendStatesChannel: Channel<S> =
        Channel(capacity = Channel.UNLIMITED, onBufferOverflow = BufferOverflow.SUSPEND)

    private val actionsSharedFlow: MutableSharedFlow<Action> = MutableSharedFlow()
    private val setStates: MutableSharedFlow<S> = MutableSharedFlow<S>(
        replay = 1,
        extraBufferCapacity = Int.MAX_VALUE,
        onBufferOverflow = BufferOverflow.SUSPEND,
    ).apply { tryEmit(initialState) }

    val actions: Flow<Action> = actionsSharedFlow
    val states: Flow<S> = setStates

    init {
        scope.stateMachine(
            initialState = initialState,
            inputActions = inputActionsChannel,
            requestStates = requestStatesChannel,
            sendStates = sendStatesChannel,
            setStates = setStates,
            actions = actionsSharedFlow,
            reduce = reduce,
        )
    }

    override fun dispatch(action: Action) {
        inputActionsChannel.trySend(action)
    }

    override fun state(): S = setStates.replayCache.last()

    suspend fun awaitState(): S {
        requestStatesChannel.send(Unit)
        return sendStatesChannel.receive()
    }

    fun terminate() {
        scope.cancel()
    }
}