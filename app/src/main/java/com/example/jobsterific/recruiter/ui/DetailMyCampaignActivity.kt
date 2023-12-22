package com.example.jobsterific.recruiter.ui

import ApiConfig
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.jobsterific.R
import com.example.jobsterific.ViewModelFactoryProfile
import com.example.jobsterific.data.response.BatchesItem
import com.example.jobsterific.data.response.NewCampaignResponse
import com.example.jobsterific.databinding.ActivityDetailMyCampaignBinding
import com.example.jobsterific.recruiter.viewmodel.ProfileCompanyViewModel
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDate

class DetailMyCampaignActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailMyCampaignBinding
    var getData: BatchesItem? = null
    var token = ""
    private val viewModel by viewModels<ProfileCompanyViewModel> {
        ViewModelFactoryProfile.getInstance(applicationContext, token)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailMyCampaignBinding.inflate(layoutInflater)
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
        viewModel.getSession().observe(this) { user ->
            token = user.token
            Log.d("ini token", token.toString())

        }
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

//        val startDateString = "2023-01-01"
//        val endDateString = "2023-12-31"
//
//        val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
//
//        val startDate = LocalDate.parse(startDateString, dateFormatter)
//        val endDate = LocalDate.parse(endDateString, dateFormatter)
//
//        val dateToCheckString = "2023-06-15"
//        val dateToCheck = LocalDate.parse(dateToCheckString, dateFormatter)
//
//        if (isDateInRange(dateToCheck, startDate, endDate)) {
//            println("The date is within the range.")
//            binding.buttonActive.setImageResource(R.drawable.ic_active)
//        } else {
//            binding.buttonActive.setImageResource(R.drawable.ic_inactive)
//            println("The date is outside the range.")
//        }

        if (getData?.status == true){
            binding.buttonActive.setImageResource(R.drawable.ic_active)

        } else{
            binding.buttonActive.setImageResource(R.drawable.ic_inactive)
        }
    }
    fun isDateInRange(date: LocalDate, startDate: LocalDate, endDate: LocalDate): Boolean {
        return date.isEqual(startDate) || date.isEqual(endDate) || (date.isAfter(startDate) && date.isBefore(endDate))
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_my_campaign, menu)
        val actionActiveItem = menu.findItem(R.id.action_active)
        if (getData?.status == true) {
            actionActiveItem.title = "Set Inactive"
            actionActiveItem.setIcon(R.drawable.ic_inactive)
        } else {
            actionActiveItem.title = "Set Active"
            actionActiveItem.setIcon(R.drawable.ic_active)
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_edit_campaign -> {
                val intent = Intent(this, EditCampaignActivity::class.java)
                intent.putExtra("batchId", getData?.batchId.toString())
                Log.d("batch id di detail", getData?.batchId.toString())
                intent.putExtra("campaignName", getData?.campaignName)
                intent.putExtra("campaignDesc", getData?.campaignDesc)
                intent.putExtra("startDate", getData?.startDate)
                intent.putExtra("endDate", getData?.endDate)
                intent.putExtra("campaignKeyword", getData?.campaignKeyword)
                startActivity(intent)
                true
            }
            R.id.action_candidates -> {
                val intent = Intent(this, ViewCandidateActivity::class.java)
                intent.putExtra("batchId", getData?.batchId.toString())
                startActivity(intent)
                true
            }R.id.action_active -> {
                if (getData?.status == true){
                    item.title = "Set Inactive"
                } else if (getData?.status == false){
                    item.title = "Set Active"
                }
                if (item.title == "Set Inactive"){
                    editCampaign(token = token, batchId = getData?.batchId.toString(), status = false )
                }else if(item.title == "Set Active"){
                    editCampaign(token = token, batchId = getData?.batchId.toString(), status = true )
                }

                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun editCampaign( status: Boolean, token: String, batchId: String) {
        val client = ApiConfig.getApiService2(token).editStatus(status = status, userId = batchId  )
        client.enqueue(object : Callback<NewCampaignResponse> {
            override fun onResponse(
                call: Call<NewCampaignResponse>,
                response: Response<NewCampaignResponse>
            ) {
                val responseBody = response.body()

                if (responseBody != null) {
                    val alertDialog = AlertDialog.Builder(this@DetailMyCampaignActivity).apply {
                        setTitle("Success")
                        setMessage("Your campaign has been updated")

                        setPositiveButton("Back") { _, _ ->
                            val intent = Intent(context, DashboardRecruiterActivity::class.java)
                            intent.flags =
                                Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                            startActivity(intent)
                            finish()
                        }
                    }.create()

                    alertDialog.setOnShowListener {
                        // Get the positive button
                        val positiveButton = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE)

                        // Apply the color filter to the positive button
                        positiveButton?.let {
                            it.setTextColor(
                                ContextCompat.getColor(
                                    this@DetailMyCampaignActivity,
                                    com.example.jobsterific.R.color.black
                                )
                            )
                        }
                    }

                    alertDialog.show()
                } else {
                    val alertDialog = AlertDialog.Builder(this@DetailMyCampaignActivity).apply {
                        setTitle("Sory...")
                        setMessage("Fail Upoloaded")

                        setPositiveButton("OK") { dialog, _ ->
                            dialog.dismiss()
                        }
                    }.create()

                    alertDialog.setOnShowListener {
                        // Get the positive button
                        val positiveButton = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE)

                        // Apply the color filter to the positive button
                        positiveButton?.let {
                            it.setTextColor(
                                ContextCompat.getColor(
                                    this@DetailMyCampaignActivity,
                                    R.color.black
                                )
                            )
                        }
                    }

                    alertDialog.show()
                }
            }
            override fun onFailure(call: Call<NewCampaignResponse>, t: Throwable) {}
        })
    }
}