package com.example.jobsterific.recruiter.ui

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.example.jobsterific.R
import com.example.jobsterific.databinding.ActivityDetailMyCampaignBinding
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class DetailMyCampaignActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailMyCampaignBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailMyCampaignBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val toolbar: androidx.appcompat.widget.Toolbar = findViewById(R.id.toolbar)

        // Set the toolbar as the action bar
        setSupportActionBar(toolbar)


        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        val startDateString = "2023-01-01"
        val endDateString = "2023-12-31"

        val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

        val startDate = LocalDate.parse(startDateString, dateFormatter)
        val endDate = LocalDate.parse(endDateString, dateFormatter)

        val dateToCheckString = "2023-06-15"
        val dateToCheck = LocalDate.parse(dateToCheckString, dateFormatter)

        if (isDateInRange(dateToCheck, startDate, endDate)) {
            println("The date is within the range.")
            binding.buttonActive.setImageResource(R.drawable.ic_active)
        } else {
            binding.buttonActive.setImageResource(R.drawable.ic_inactive)
            println("The date is outside the range.")
        }
    }
    fun isDateInRange(date: LocalDate, startDate: LocalDate, endDate: LocalDate): Boolean {
        return date.isEqual(startDate) || date.isEqual(endDate) || (date.isAfter(startDate) && date.isBefore(endDate))
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_my_campaign, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_edit_campaign -> {
                val intent = Intent(this, EditCampaignActivity::class.java)
                startActivity(intent)
                true
            }
            R.id.action_candidates -> {
                val intent = Intent(this, ViewCandidateActivity::class.java)
                startActivity(intent)
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }
}