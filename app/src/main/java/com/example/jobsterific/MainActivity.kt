package com.example.jobsterific

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.jobsterific.authentication.WelcomeActivity
import com.example.jobsterific.databinding.ActivityMainBinding
import com.example.jobsterific.recruiter.ui.DashboardRecruiterActivity
import com.example.jobsterific.user.ui.DashboardUserActivity
import com.example.jobsterific.user.viewmodel.MainViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private val splashTime: Long = 2000
    private lateinit var binding : ActivityMainBinding
    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactoryUser.getInstance(applicationContext)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel.getSession().observe(this) { user ->

            Log.d("customer gak", user.isCostumer.toString())

      if (user.isLogin && user.isCostumer == false){
              val intent = Intent(this, DashboardUserActivity::class.java)
              startActivity(intent)
      }else if (user.isLogin && user.isCostumer == true){
          val intent = Intent(this, DashboardRecruiterActivity::class.java)
          startActivity(intent)
      }
      else{
          CoroutineScope(Dispatchers.Main).launch {
              kotlinx.coroutines.delay(splashTime)
              navigateToMainActivity()
          }
      }


        }

    }
    private fun navigateToMainActivity() {
        val intent = Intent(this, WelcomeActivity::class.java)
        startActivity(intent)
        finish() // Close the splash activity so that it's not accessible when pressing the back button
    }

}