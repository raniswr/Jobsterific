package com.example.jobsterific.recruiter.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.jobsterific.databinding.ActivityEditCampaignBinding

class EditCampaignActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditCampaignBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditCampaignBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val toolbar: androidx.appcompat.widget.Toolbar = findViewById(com.example.jobsterific.R.id.toolbar)

        // Set the toolbar as the action bar
        setSupportActionBar(toolbar)


        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
        binding.buttonPost
            .setOnClickListener {
            val alertDialog = AlertDialog.Builder(this@EditCampaignActivity).apply {
                setTitle("Success")
                setMessage("Your campaign has been updated")

                setPositiveButton("Back") { _, _ ->
                    val intent = Intent(context, DashboardRecruiterActivity::class.java)
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
                    it.setTextColor(ContextCompat.getColor(this@EditCampaignActivity, com.example.jobsterific.R.color.black))
                }
            }

            alertDialog.show()
        }
        binding.buttonDelete
            .setOnClickListener {
                val alertDialog = AlertDialog.Builder(this@EditCampaignActivity).apply {
                    setTitle("Delete")
                    setMessage("Your campaign has been deleted")

                    setPositiveButton("Back") { _, _ ->
                        val intent = Intent(context, DashboardRecruiterActivity::class.java)
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
                        it.setTextColor(ContextCompat.getColor(this@EditCampaignActivity, com.example.jobsterific.R.color.black))
                    }
                }

                alertDialog.show()
            }
    }
}