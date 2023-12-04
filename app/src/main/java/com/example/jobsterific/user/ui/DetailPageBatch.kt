package com.example.jobsterific.user.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.jobsterific.R
import com.example.jobsterific.databinding.ActivityDetailPageBatchBinding

class DetailPageBatch : AppCompatActivity() {
    private lateinit var binding: ActivityDetailPageBatchBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailPageBatchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val toolbar: androidx.appcompat.widget.Toolbar = findViewById(R.id.toolbar)

        // Set the toolbar as the action bar
        setSupportActionBar(toolbar)


        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        binding.myButton.setOnClickListener {
            //            val intent = Intent(this, ApplymentBatchActivity::class.java)
//            startActivity(intent)
            val alertDialog = AlertDialog.Builder(this@DetailPageBatch).apply {
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
                    it.setTextColor(ContextCompat.getColor(this@DetailPageBatch, R.color.black))
                }
            }
            alertDialog.show()
        }

    }
}