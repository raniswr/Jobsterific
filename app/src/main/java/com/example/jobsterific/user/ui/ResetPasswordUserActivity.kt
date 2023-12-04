package com.example.jobsterific.user.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.jobsterific.databinding.ActivityResetPasswordUserBinding

class ResetPasswordUserActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResetPasswordUserBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResetPasswordUserBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}