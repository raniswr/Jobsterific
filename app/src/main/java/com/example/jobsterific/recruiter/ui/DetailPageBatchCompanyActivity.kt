package com.example.jobsterific.recruiter.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.jobsterific.R
import com.example.jobsterific.databinding.ActivityDetailPageBatchCompanyBinding

class DetailPageBatchCompanyActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailPageBatchCompanyBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailPageBatchCompanyBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val toolbar: androidx.appcompat.widget.Toolbar = findViewById(R.id.toolbar)

        // Set the toolbar as the action bar
        setSupportActionBar(toolbar)


        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }

}