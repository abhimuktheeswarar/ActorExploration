package com.msabhi.shared.actor

import com.msabhi.shared.common.Action
import com.msabhi.shared.common.State
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

abstract class SideEffect(
    private val stateReserve: ActorStore<*>,
) {

    protected val TAG: String = this::class.simpleName ?: "SideEffect"
    protected val scope: CoroutineScope = stateReserve.scope

    init {
        stateReserve.actions.onEach(::handle).launchIn(scope)
    }

    fun dispatch(action: Action) {
        stateReserve.dispatch(action)
    }

    fun <S : State> state(): S = stateReserve.state() as S

    suspend fun <S : State> awaitState(): S = stateReserve.awaitState() as S

    abstract fun handle(action: Action)
}