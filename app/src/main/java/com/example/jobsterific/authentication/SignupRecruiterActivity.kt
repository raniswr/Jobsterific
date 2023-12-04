package com.example.jobsterific.authentication

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.jobsterific.databinding.ActivitySignupRecruiterBinding
import com.example.jobsterific.recruiter.ui.DashboardRecruiterActivity

class SignupRecruiterActivity : AppCompatActivity() {
    private lateinit var binding : ActivitySignupRecruiterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupRecruiterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding?.signupRecruiter?.setOnClickListener {
            val intent = Intent(this,DashboardRecruiterActivity::class.java)
            startActivity(intent)
        }
    }
}