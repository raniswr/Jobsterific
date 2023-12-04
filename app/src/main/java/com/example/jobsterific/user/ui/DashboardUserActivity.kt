package com.example.jobsterific.user.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.jobsterific.R
import com.example.jobsterific.databinding.ActivityDashboardUserBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class DashboardUserActivity : AppCompatActivity() {
    lateinit var bottomNav : BottomNavigationView
    private lateinit var binding : ActivityDashboardUserBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDashboardUserBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        binding?.fab?.setOnClickListener {
            val intent = Intent(this, UploadResumeActivity::class.java)
            startActivity(intent)
        }
        loadFragment(HomeFragment())
        bottomNav = findViewById(R.id.bottomNav) as BottomNavigationView
        bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.home -> {
                    loadFragment(HomeFragment())
                    true
                }
                R.id.profile -> {
                    loadFragment(ProfileFragment())
                    true
                }

                else -> {
                    false
                }
            }
        }
    }
    private  fun loadFragment(fragment: Fragment){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container,fragment)
        transaction.commit()
    }
}