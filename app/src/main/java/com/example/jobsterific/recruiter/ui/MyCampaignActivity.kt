package com.example.jobsterific.recruiter.ui

import MyCampaignAdapter
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.jobsterific.R
import com.example.jobsterific.databinding.ActivityMyCampaignBinding
import com.example.jobsterific.recruiter.CourseRVModal

class MyCampaignActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMyCampaignBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyCampaignBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val toolbar: androidx.appcompat.widget.Toolbar = findViewById(R.id.toolbar)

        // Set the toolbar as the action bar
        setSupportActionBar(toolbar)


        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val myCampaignRV = binding!!.idRVCampaign
        val myCampaignList = getCompanyData() // Assuming you have a function to get company data
        val myCampaignLayoutManager = LinearLayoutManager(this)

        myCampaignRV.layoutManager = myCampaignLayoutManager
        val myCampaignAdapter = MyCampaignAdapter(myCampaignList, this)
        myCampaignRV.adapter = myCampaignAdapter


        binding?.idRVCampaign?.adapter = myCampaignAdapter

        myCampaignAdapter.onItemClick = { clickedCourse ->
            val intent = Intent(this, DetailMyCampaignActivity::class.java)
            startActivity(intent)
        }

        binding.newCampaign.setOnClickListener {
            val intent = Intent(this, NewCampaignActivity::class.java)
            startActivity(intent)
        }



    }
    private fun getCompanyData(): ArrayList<CourseRVModal> {
        val companyList = ArrayList<CourseRVModal>()
        // Add your company data here
        companyList.add(CourseRVModal("UI/UX Hiring Batch"))
        companyList.add(CourseRVModal("Tech Hiring Batch"))
        // Add more companies as needed
        return companyList
    }


}