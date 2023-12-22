package com.example.jobsterific.authentication

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.jobsterific.databinding.ActivitySignupBinding

class SignupActivity : AppCompatActivity() {
    private lateinit var binding : ActivitySignupBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding?.button1?.setOnClickListener {
            val intent = Intent(this,SignupUserActivity::class.java)
            startActivity(intent)
        }
        binding?.button2?.setOnClickListener {
            val intent = Intent(this,SignupRecruiterActivity::class.java)
            startActivity(intent)
        }
    }
}