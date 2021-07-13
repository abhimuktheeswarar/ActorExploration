package com.msabhi.shared.common

import com.msabhi.shared.entities.CounterAction
import com.msabhi.shared.entities.CounterState

fun counterStateReduce(action: Action, state: CounterState): CounterState {
    return when (action) {

        is CounterAction.IncrementAction -> state.copy(counter = state.counter + 1)

        is CounterAction.DecrementAction -> state.copy(counter = state.counter - 1)

        is CounterAction.ForceUpdateAction -> state.copy(counter = action.count)

        is CounterAction.ResetAction -> state.copy(counter = 0)

        else -> state
    }
}