package com.example.jobsterific.recruiter.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.jobsterific.R
import com.example.jobsterific.data.response.BatchesItem
import com.example.jobsterific.databinding.ActivityDetailPageBatchCompanyBinding
import com.google.gson.Gson

class DetailPageBatchCompanyActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailPageBatchCompanyBinding
    var getData: BatchesItem? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailPageBatchCompanyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val gson = Gson()
        val extras = intent?.extras
        getData  =  gson.fromJson(extras?.getString("detailUser"), BatchesItem::class.java)

        binding.description.text = getData?.campaignDesc
        binding.period.text = "${getData?.startDate} - ${getData?.endDate}"
        binding.job.text = getData?.campaignKeyword

        val toolbar: androidx.appcompat.widget.Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = getData?.campaignName

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }

}