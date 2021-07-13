package com.msabhi.androidApp.counter.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.msabhi.androidApp.common.BaseViewModelFactory
import com.msabhi.androidApp.common.ShowToastAction
import com.msabhi.androidApp.counter.entities.CounterAction
import com.msabhi.androidApp.counter.entities.CounterState
import com.msabhi.androidApp.databinding.ActivityCounterBinding
import com.msabhi.shared.EventAction
import com.msabhi.shared.name

class CounterActivity : AppCompatActivity() {

    @Suppress("PrivatePropertyName")
    private val TAG = "CounterActivity"

    private val viewModel by viewModels<CounterViewModel> {
        BaseViewModelFactory {
            CounterViewModel.get(this)
        }
    }

    private lateinit var binding: ActivityCounterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCounterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonDecrement.setOnClickListener {
            viewModel.dispatch(CounterAction.DecrementAction)
        }

        binding.buttonIncrement.setOnClickListener {
            viewModel.dispatch(CounterAction.IncrementAction)
        }

        binding.buttonReset.setOnClickListener {
            viewModel.dispatch(CounterAction.ResetAction)
        }

        binding.buttonShowToast.setOnClickListener {
            viewModel.dispatch(ShowToastAction("${System.currentTimeMillis()}"))
        }

        /*lifecycleScope.launchWhenCreated {
            viewModel.states.collect(::setupViews)
        }*/
    }

    @SuppressLint("SetTextI18n")
    private fun setupViews(state: CounterState) {
        Log.d(TAG, "setupViews = ${state.counter} | ${viewModel.state().counter}")
        binding.textCount.text = state.counter.toString()
    }

    private fun processEvents(action: EventAction) {
        Log.d(TAG, "processEvents = ${action.name()}")
        when (action) {

            is ShowToastAction -> {
                Toast.makeText(this, "A ${action.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}