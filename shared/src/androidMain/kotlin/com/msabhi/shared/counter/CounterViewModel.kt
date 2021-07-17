package com.msabhi.shared.counter

import android.content.Context
import androidx.lifecycle.ViewModel
import com.msabhi.shared.Store
import com.msabhi.shared.StoreType
import com.msabhi.shared.actor.ActorStore
import com.msabhi.shared.basic.BasicStoreV1
import com.msabhi.shared.basic.BasicStoreV2
import com.msabhi.shared.classic.ClassicStore
import com.msabhi.shared.common.Action
import com.msabhi.shared.common.StateChangeListener
import com.msabhi.shared.common.counterStateReduce
import com.msabhi.shared.entities.CounterState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow

class CounterViewModel(storeType: StoreType) : ViewModel() {

    private val scope = CoroutineScope(Dispatchers.Default)

    private val mutableState by lazy {
        MutableSharedFlow<CounterState>(replay = 1).apply {
            tryEmit(CounterState())
        }
    }
    private val stateChangeListener by lazy<StateChangeListener<CounterState>> {
        { state ->
            mutableState.tryEmit(state)
        }
    }

    private val store: Store<CounterState> = when (storeType) {
        StoreType.CLASSIC -> ClassicStore(CounterState(),
            ::counterStateReduce,
            listOf(stateChangeListener))
        StoreType.BASIC_V1 -> BasicStoreV1(CounterState(), ::counterStateReduce, scope)
        StoreType.BASIC_V2 -> BasicStoreV2(CounterState(), ::counterStateReduce, scope)
        StoreType.ACTOR -> ActorStore(CounterState(), ::counterStateReduce, scope)
    }

    lateinit var states: Flow<CounterState>

    lateinit var actions: Flow<Action>

    init {

        when (store) {
            is ClassicStore -> {
                actions = MutableSharedFlow()
                states = mutableState
            }
            is BasicStoreV1 -> {
                actions = MutableSharedFlow()
                states = store.states
            }
            is BasicStoreV2 -> {
                actions = MutableSharedFlow()
                states = store.states
            }
            is ActorStore -> {
                states = store.states
                actions = store.actions
                CounterSideEffect(store)
            }
        }
    }

    fun dispatch(action: Action) {
        store.dispatch(action)
    }

    fun state(): CounterState = store.state()

    override fun onCleared() {
        super.onCleared()
        scope.cancel()
    }

    companion object {

        fun get(context: Context): CounterViewModel {
            return CounterViewModel(StoreType.ACTOR)
        }
    }
}