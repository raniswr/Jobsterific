package com.example.jobsterific.authentication

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.jobsterific.databinding.ActivitySignupUserBinding
import com.example.jobsterific.user.ui.DashboardUserActivity

class SignupUserActivity : AppCompatActivity() {
    private lateinit var binding : ActivitySignupUserBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupUserBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding?.signupUser?.setOnClickListener {
            val intent = Intent(this, DashboardUserActivity::class.java)
            startActivity(intent)
        }
    }
}