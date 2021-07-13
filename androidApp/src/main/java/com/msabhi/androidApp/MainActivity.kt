package com.msabhi.androidApp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.msabhi.androidApp.counter.ui.CounterActivity
import com.msabhi.androidApp.databinding.ActivityMainBinding
import com.msabhi.shared.Greeting

fun greet(): String {
    return Greeting().greeting()
}

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.textPlatform.text = greet()
        binding.buttonCounterExample.setOnClickListener {
            startActivity(Intent(this, CounterActivity::class.java))
        }
    }
}

