package com.msabhi.androidApp.counter.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import com.msabhi.androidApp.counter.entities.CounterState
import com.msabhi.shared.Action
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class CounterViewModel : ViewModel() {

    private val mutableState = MutableStateFlow(CounterState())
    val states: Flow<CounterState> = mutableState

    fun dispatch(action: Action) {
    }

    fun state(): CounterState = mutableState.value

    companion object {

        fun get(context: Context): CounterViewModel {
            return CounterViewModel()
        }
    }
}