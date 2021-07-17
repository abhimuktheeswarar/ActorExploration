package com.msabhi.shared.classic

import com.msabhi.shared.Store
import com.msabhi.shared.common.Action
import com.msabhi.shared.common.State
import com.msabhi.shared.common.StateChangeListener
import platform.Foundation.NSMutableArray
import platform.Foundation.lastObject
import platform.Foundation.removeObject

actual class ClassicStore<S : State> actual constructor(
    initialState: S,
    override val reduce: (Action, S) -> S,
    private val stateChangeListeners: List<StateChangeListener<S>>,
) : Store<S> {

    private val actions = NSMutableArray()

    private var state: S = initialState

    override fun dispatch(action: Action) {
        actions.addObject(action)
        handle(actions.lastObject() as Action)
        actions.removeObject(actions.lastObject())
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