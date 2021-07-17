package com.msabhi.shared.classic

import com.msabhi.shared.Store
import com.msabhi.shared.common.Action
import com.msabhi.shared.common.State
import com.msabhi.shared.common.StateChangeListener
import java.util.concurrent.LinkedBlockingQueue

actual class ClassicStore<S : State> actual constructor(
    initialState: S,
    override val reduce: (Action, S) -> S,
    private val stateChangeListeners: List<StateChangeListener<S>>,
) : Store<S> {

    private val actions = LinkedBlockingQueue<Action>()

    @Volatile
    private var state: S = initialState

    @Synchronized
    override fun dispatch(action: Action) {
        actions.offer(action)
        actions.poll()?.let {
            handle(it)
        }
    }

    override fun state(): S = state

    private fun handle(action: Action) {
        val newState = reduce(action, state)
        if (state != newState) {
            state = newState
            stateChangeListeners.forEach { it.invoke(newState) }
        }
    }
}