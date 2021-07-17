package com.msabhi.androidApp.counter.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.msabhi.androidApp.common.BaseViewModelFactory
import com.msabhi.androidApp.databinding.ActivityCounterBinding
import com.msabhi.shared.ShowToastAction
import com.msabhi.shared.common.Action
import com.msabhi.shared.common.name
import com.msabhi.shared.counter.CounterViewModel
import kotlinx.coroutines.flow.collect

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
            viewModel.dispatch(com.msabhi.shared.entities.CounterAction.DecrementAction)
        }

        binding.buttonIncrement.setOnClickListener {
            viewModel.dispatch(com.msabhi.shared.entities.CounterAction.IncrementAction)
        }

        binding.buttonReset.setOnClickListener {
            viewModel.dispatch(com.msabhi.shared.entities.CounterAction.ResetAction)
        }

        binding.buttonShowToast.setOnClickListener {
            viewModel.dispatch(ShowToastAction("${System.currentTimeMillis()}"))
        }

        lifecycleScope.launchWhenCreated {
            viewModel.states.collect(::setupViews)
        }

        lifecycleScope.launchWhenCreated {
            viewModel.actions.collect(::processEvents)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setupViews(state: com.msabhi.shared.entities.CounterState) {
        Log.d(TAG, "setupViews = ${state.counter} | ${viewModel.state().counter}")
        binding.textCount.text = state.counter.toString()
    }

    private fun processEvents(action: Action) {
        Log.d(TAG, "processEvents = ${action.name()}")
        when (action) {

            is ShowToastAction -> {
                Toast.makeText(this, "A ${action.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}