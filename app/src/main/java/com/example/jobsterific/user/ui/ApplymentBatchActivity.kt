package com.example.jobsterific.user.ui

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.jobsterific.R
import com.example.jobsterific.databinding.ActivityApplymentBatchBinding
import com.example.jobsterific.ViewModelFactory
import com.example.jobsterific.user.viewmodel.UploadViewModel

class ApplymentBatchActivity : AppCompatActivity() {
    private lateinit var binding: ActivityApplymentBatchBinding
    private val viewModel by viewModels<UploadViewModel> {
        ViewModelFactory.getInstance(this)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityApplymentBatchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.getSessionPathResume().observe(this) { user ->

            val currentImageUri = user?.fileName
            if (currentImageUri != null) {

                binding.statusResume.text = currentImageUri
                if ( binding.statusResume.text == "") {
                    binding.statusResume.text = "Upload Resume"
                }
            }


        }
        setupView()
            binding.textUpResume.setOnClickListener {
                val intent = Intent(this, UploadResumeActivity::class.java)
                startActivity(intent)
            }
        binding.submitButton.setOnClickListener {
            val alertDialog = AlertDialog.Builder(this@ApplymentBatchActivity).apply {
                setTitle("Success")
                setMessage("Your Application has been successful")

                setPositiveButton("Back") { _, _ ->
                    val intent = Intent(context, DashboardUserActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                    finish()
                }
            }.create()

            alertDialog.setOnShowListener {
                // Get the positive button
                val positiveButton = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE)

                // Apply the color filter to the positive button
                positiveButton?.let {
                    it.setTextColor(ContextCompat.getColor(this@ApplymentBatchActivity, R.color.black))
                }
            }

            alertDialog.show()
        }
        }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }
    }
