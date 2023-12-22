package com.example.jobsterific.user.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
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

        var  fab = binding.fab
        fab.setOnClickListener { loadFragment(UploadResumeFragment())
            fab.backgroundTintList = ContextCompat.getColorStateList(this, R.color.blue)
            bottomNav.selectedItemId = com.example.jobsterific.R.id.upload
        }
        loadFragment(HomeFragment())
        bottomNav = findViewById(R.id.bottomNav) as BottomNavigationView
        bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.home -> {
                    loadFragment(HomeFragment())
                    fab.backgroundTintList = ContextCompat.getColorStateList(this, R.color.black)
                    true
                }
                R.id.upload -> {
                    loadFragment(UploadResumeFragment())
                    fab.backgroundTintList = ContextCompat.getColorStateList(this, R.color.blue)
                    true
                }
                R.id.profile -> {
                    loadFragment(ProfileFragment())
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
        transaction.replace(R.id.container,fragment)
        transaction.commit()
    }

}