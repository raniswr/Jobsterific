package com.example.jobsterific

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.jobsterific.authentication.WelcomeActivity
import com.example.jobsterific.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private val splashTime: Long = 2000
    private lateinit var binding : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        CoroutineScope(Dispatchers.Main).launch {
            kotlinx.coroutines.delay(splashTime)
            navigateToMainActivity()
        }
    }
    private fun navigateToMainActivity() {
        val intent = Intent(this, WelcomeActivity::class.java)
        startActivity(intent)
        finish() // Close the splash activity so that it's not accessible when pressing the back button
    }

}