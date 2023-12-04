package com.example.jobsterific.authentication

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.jobsterific.databinding.ActivitySigninBinding
import com.example.jobsterific.recruiter.ui.DashboardRecruiterActivity

class SigninActivity : AppCompatActivity() {
    private lateinit var binding : ActivitySigninBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySigninBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding?.signup?.setOnClickListener {
            val intent = Intent(this,WelcomeActivity::class.java)
            startActivity(intent)
        }

        binding?.signin?.setOnClickListener {
            val intent = Intent(this,DashboardRecruiterActivity::class.java)
            startActivity(intent)
        }

    }
}