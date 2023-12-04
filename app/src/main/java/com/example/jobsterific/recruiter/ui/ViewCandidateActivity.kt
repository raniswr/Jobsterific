package com.example.jobsterific.recruiter.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.jobsterific.R
import com.example.jobsterific.databinding.ActivityViewCandidateBinding
import com.example.jobsterific.recruiter.CourseRVModal
import com.example.jobsterific.recruiter.adapter.ViewCandidateOfCampaignAdapter

class ViewCandidateActivity : AppCompatActivity() {
    private lateinit var binding: ActivityViewCandidateBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityViewCandidateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar: androidx.appcompat.widget.Toolbar = findViewById(R.id.toolbar)

        setSupportActionBar(toolbar)


        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        val viewCandidateRV = binding!!.idViewCandidates
        val viewCandidateList = getCandidateData()// Assuming you have a function to get company data
        val viewCandidateLayoutManager = LinearLayoutManager(this)

        viewCandidateRV.layoutManager =viewCandidateLayoutManager
        val viewCandidateAdapter = ViewCandidateOfCampaignAdapter(viewCandidateList, this)
        viewCandidateRV.adapter = viewCandidateAdapter


        binding?.idViewCandidates?.adapter = viewCandidateAdapter

        viewCandidateAdapter.onItemClick = { clickedCourse ->
            val intent = Intent(this, DetailMyCandidateActivity::class.java)
            startActivity(intent)
        }
    }

    private fun getCandidateData(): ArrayList<CourseRVModal> {
        val companyList = ArrayList<CourseRVModal>()
        // Add your company data here
        companyList.add(CourseRVModal("A.A Rani Prabaswari Dewi"))
        companyList.add(CourseRVModal("A.A Rani Prabaswari Dewi"))
        // Add more companies as needed
        return companyList
    }


}