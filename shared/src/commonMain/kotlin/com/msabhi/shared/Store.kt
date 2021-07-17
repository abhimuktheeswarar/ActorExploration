package com.msabhi.shared

import com.msabhi.shared.common.Action
import com.msabhi.shared.common.State

interface Store<S : State> {

    val reduce: (Action, S) -> S

    fun dispatch(action: Action)

    fun state(): S
}