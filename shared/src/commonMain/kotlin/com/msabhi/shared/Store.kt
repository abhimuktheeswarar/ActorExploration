package com.msabhi.shared

import com.msabhi.shared.common.Action
import com.msabhi.shared.common.State
import kotlinx.coroutines.flow.Flow

interface Store<S : State> {

    val states: Flow<S>

    val reduce: (Action, S) -> S

    fun dispatch(action: Action)

    fun state(): S
}