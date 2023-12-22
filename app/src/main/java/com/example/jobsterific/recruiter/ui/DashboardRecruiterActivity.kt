package com.example.jobsterific.recruiter.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
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



      var  fab = binding.fab
        fab.setOnClickListener { loadFragment(MyCampaignFragment())
            fab.backgroundTintList = ContextCompat.getColorStateList(this, R.color.orange)
            bottomNav.selectedItemId = com.example.jobsterific.R.id.arsip
        }


        bottomNav = findViewById(com.example.jobsterific.R.id.bottomNav) as BottomNavigationView
        bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                com.example.jobsterific.R.id.homeRecruiter -> {
                    loadFragment(HomeRecruiterFragment())
                    fab.backgroundTintList = ContextCompat.getColorStateList(this, R.color.black)
                    true
                }
                com.example.jobsterific.R.id.arsip -> {
                    loadFragment(MyCampaignFragment())
                    fab.backgroundTintList = ContextCompat.getColorStateList(this, R.color.orange)
                    true
                }
                com.example.jobsterific.R.id.profile -> {
                    loadFragment(ProfileRecruiterFragment())
                    fab.backgroundTintList = ContextCompat.getColorStateList(this, R.color.black)
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
        transaction.replace(com.example.jobsterific.R.id.container,fragment)
        transaction.commit()
    }
}