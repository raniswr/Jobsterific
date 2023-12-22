package com.example.jobsterific.recruiter.ui

import ApiConfig
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.jobsterific.R
import com.example.jobsterific.ViewModelFactoryProfile
import com.example.jobsterific.data.response.DeleteResponse
import com.example.jobsterific.data.response.NewCampaignResponse
import com.example.jobsterific.databinding.ActivityEditCampaignBinding
import com.example.jobsterific.recruiter.viewmodel.ProfileCompanyViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EditCampaignActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditCampaignBinding
    var token = ""

    private val viewModel by viewModels<ProfileCompanyViewModel> {
        ViewModelFactoryProfile.getInstance(applicationContext, token)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditCampaignBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel.getSession().observe(this) { user ->
            token = user.token
            Log.d("ini token", token.toString())

        }

    val  batchId = intent.getStringExtra("batchId")

        Log.d("ini batch id", batchId.toString())
        val campaignName = intent.getStringExtra("campaignName")
        val campaignDesc = intent.getStringExtra("campaignDesc")
        val startDate = intent.getStringExtra("startDate")
        val endDate = intent.getStringExtra("endDate")
        val campaignKeyword = intent.getStringExtra("campaignKeyword")

        binding.editTextCampaignName.setText(campaignName)
        binding.editTextDescription.setText(campaignDesc)
        binding.campaignStart.setText(startDate)
        binding.campaignEnd.setText(endDate)
        binding.editTextKeyword.setText(campaignKeyword)
        val toolbar: androidx.appcompat.widget.Toolbar =
            findViewById(com.example.jobsterific.R.id.toolbar)

        setSupportActionBar(toolbar)


        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
        binding.buttonPost
            .setOnClickListener {
                editCampaign(campaignName =  binding.editTextCampaignName.text.toString(), campaignDesc = binding.editTextDescription.text.toString(),
                    campaignKeyword = binding.editTextKeyword.text.toString(), startDate =  binding.campaignStart.text.toString(),
                    endDate = binding.campaignEnd.text.toString(), token, batchId?:"")

            }
        binding.buttonDelete
            .setOnClickListener {

                delete(token, batchId?:"")
                val alertDialog = AlertDialog.Builder(this@EditCampaignActivity).apply {
                    setTitle("Delete")
                    setMessage("Your campaign has been deleted")

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
                                this@EditCampaignActivity,
                                com.example.jobsterific.R.color.black
                            )
                        )
                    }
                }

                alertDialog.show()
            }

    }

    fun delete(token: String, batchId: String) {
        try {
            val client = ApiConfig.getApiService2(token).deleteBatch(batchId)
            client.enqueue(object : Callback<DeleteResponse> {
                override fun onResponse(
                    call: Call<DeleteResponse>,
                    response: Response<DeleteResponse>
                ) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        Log.d("Delete", responseBody.message.toString())
                    } else {
                        Log.d("Gagal delete", responseBody?.message.toString())
                    }
                }
                override fun onFailure(call: Call<DeleteResponse>, t: Throwable) {}
            })
        } catch (e: Exception) {
            // Handle exception if needed
        }
    }

    private fun editCampaign(campaignName: String,campaignDesc: String,campaignKeyword: String, startDate: String, endDate: String, token: String, batchId: String) {
        val client = ApiConfig.getApiService2(token).editCampaign(campaignName = campaignName, campaignDesc = campaignDesc, campaignPeriod = null, campaignKeyword = campaignKeyword, startDate = startDate, endDate = endDate, status = true, userId = batchId  )
        client.enqueue(object : Callback<NewCampaignResponse> {
            override fun onResponse(
                call: Call<NewCampaignResponse>,
                response: Response<NewCampaignResponse>
            ) {
                val responseBody = response.body()

                if (responseBody != null) {
                    val alertDialog = AlertDialog.Builder(this@EditCampaignActivity).apply {
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
                                    this@EditCampaignActivity,
                                    com.example.jobsterific.R.color.black
                                )
                            )
                        }
                    }

                    alertDialog.show()
                } else {
                    val alertDialog = AlertDialog.Builder(this@EditCampaignActivity).apply {
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
                                    this@EditCampaignActivity,
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