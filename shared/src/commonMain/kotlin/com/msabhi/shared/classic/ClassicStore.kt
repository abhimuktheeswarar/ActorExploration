package com.msabhi.shared.classic

import com.msabhi.shared.Store
import com.msabhi.shared.common.Action
import com.msabhi.shared.common.State
import com.msabhi.shared.common.StateChangeListener

/**
 * Created by Abhi Muktheeswarar on 14-July-2021.
 */

expect class ClassicStore<S : State>(
    initialState: S,
    reduce: (Action, S) -> S,
    stateChangeListeners: List<StateChangeListener<S>>,
) : Store<S>