package com.msabhi.androidApp.counter.entities

import com.msabhi.shared.Action

sealed interface CounterAction : Action {

    object IncrementAction : CounterAction

    object DecrementAction : CounterAction

    object ResetAction : CounterAction

    data class ForceUpdateAction(val count: Int) : CounterAction
}