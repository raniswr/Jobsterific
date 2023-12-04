package com.example.jobsterific.recruiter.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.jobsterific.R
import com.example.jobsterific.databinding.ActivityDashboardRecruiterBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class DashboardRecruiterActivity : AppCompatActivity() {
    lateinit var bottomNav : BottomNavigationView
    private lateinit var binding : ActivityDashboardRecruiterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDashboardRecruiterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        loadFragment(HomeRecruiterFragment())
        binding?.fab?.setOnClickListener {
            val intent = Intent(this, MyCampaignActivity::class.java)
            startActivity(intent)
        }
        bottomNav = findViewById(R.id.bottomNav) as BottomNavigationView
        bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.homeRecruiter -> {
                    loadFragment(HomeRecruiterFragment())
                    true
                }
                R.id.profile -> {
                    loadFragment(ProfileRecruiterFragment())
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