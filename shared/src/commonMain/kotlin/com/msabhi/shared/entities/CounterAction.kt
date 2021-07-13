package com.msabhi.shared.entities

import com.msabhi.shared.common.Action

sealed interface CounterAction : Action {

    object IncrementAction : CounterAction

    object DecrementAction : CounterAction

    object ResetAction : CounterAction

    data class ForceUpdateAction(val count: Int) : CounterAction
}