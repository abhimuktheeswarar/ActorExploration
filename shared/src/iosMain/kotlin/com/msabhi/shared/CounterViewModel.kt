package com.msabhi.shared

import com.msabhi.shared.basic.BasicStoreV1
import com.msabhi.shared.basic.BasicStoreV2
import com.msabhi.shared.common.Action
import com.msabhi.shared.common.counterStateReduce
import com.msabhi.shared.entities.CounterState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.Flow

class CounterViewModel(storeType: StoreType) {

    private val scope = CoroutineScope(Dispatchers.Default)

    private val store: Store<CounterState> = when (storeType) {
        StoreType.CLASSIC -> TODO()
        StoreType.BASIC_V1 -> BasicStoreV1(CounterState(), ::counterStateReduce, scope)
        StoreType.BASIC_V2 -> BasicStoreV2(CounterState(), ::counterStateReduce, scope)
        StoreType.ACTOR -> TODO()
    }

    val states: Flow<CounterState> = store.states

    fun dispatch(action: Action) {
        store.dispatch(action)
    }

    fun state(): CounterState = store.state()

    fun onCleared() {
        scope.cancel()
    }
}