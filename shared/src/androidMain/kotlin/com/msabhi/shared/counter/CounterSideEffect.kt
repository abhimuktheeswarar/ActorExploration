package com.msabhi.shared.counter

import com.msabhi.shared.ShowToastAction
import com.msabhi.shared.actor.ActorStore
import com.msabhi.shared.actor.SideEffect
import com.msabhi.shared.common.Action
import com.msabhi.shared.entities.CounterAction

class CounterSideEffect(stateReserve: ActorStore<*>) : SideEffect(stateReserve) {

    override fun handle(action: Action) {
        when (action) {

            is CounterAction.ResetAction -> {
                dispatch(ShowToastAction("Reset successfully"))
            }
        }
    }
}