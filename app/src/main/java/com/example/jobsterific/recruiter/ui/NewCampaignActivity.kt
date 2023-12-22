package com.example.jobsterific.recruiter.ui

import ApiConfig
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.jobsterific.R
import com.example.jobsterific.ViewModelFactoryProfile
import com.example.jobsterific.data.response.NewCampaignResponse
import com.example.jobsterific.databinding.ActivityNewCampaignBinding
import com.example.jobsterific.recruiter.viewmodel.ProfileCompanyViewModel
import com.google.android.material.textfield.TextInputLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Locale


class NewCampaignActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNewCampaignBinding
    private var isFormatting: Boolean = false
    var token = ""
    private val viewModel by viewModels<ProfileCompanyViewModel> {
        ViewModelFactoryProfile.getInstance(applicationContext, token)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewCampaignBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val toolbar: androidx.appcompat.widget.Toolbar =
            findViewById(com.example.jobsterific.R.id.toolbar)

        setSupportActionBar(toolbar)
        viewModel.getSession().observe(this) { user ->
            token = user.token
            Log.d("ini token", token.toString())

        }

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
        binding.campaignEnd.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                addSlashAutomatically(s)
                validateDateFormat(s)
            }
        })
        binding.campaignStart.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                addSlashAutomatically(s)
                validateDateFormat2(s)
            }
        })


        binding.description.post {
            binding.description.scrollTo(0, binding.description.bottom)
        }

        binding.buttonSave.setOnClickListener {
 newCampaign(campaignName =  binding.CampaignName.text.toString(), campaignDesc = binding.description.text.toString(),
     campaignKeyword = binding.keywords.text.toString(), startDate =  binding.campaignStart.text.toString(),
     endDate = binding.campaignEnd.text.toString(), token) }
    }

    private fun addSlashAutomatically(s: Editable?) {
        if (s != null && s.length == 5 && s[4] != '/' && !s.toString().endsWith("/")) {
            s.insert(4, "/")
        } else if (s != null && s.length == 8 && s[7] != '/' && !s.toString().endsWith("/")) {
            s.insert(7, "/")
        } else if (s != null && s.length == 4 && s[3] == '/' && s.endsWith("/")) {
            // Handle deletion of the last '/'
            s.delete(3, 4)
        } else if (s != null && s.length == 2 && s[1] == '/' && s.endsWith("/")) {
            // Handle deletion of the last '/'
            s.delete(1, 2)
        }
    }

    private fun validateDateFormat(s: Editable?) {
        try {
            val dateFormat = SimpleDateFormat("yyyy/mm/dd", Locale.getDefault())
            dateFormat.isLenient = false
            val date = dateFormat.parse(s.toString())

            binding.LayoutCampaignEnd.endIconMode = TextInputLayout.END_ICON_CUSTOM
            binding.campaignEnd.error = null
        } catch (e: ParseException) {
            binding.campaignEnd.error = "Invalid date format use yyyy/mm/dd"
            binding.LayoutCampaignEnd.endIconMode = TextInputLayout.END_ICON_NONE
        }
    }

    private fun validateDateFormat2(s: Editable?) {
        try {
            val dateFormat = SimpleDateFormat("yyyy/mm/dd", Locale.getDefault())
            dateFormat.isLenient = false
            val date = dateFormat.parse(s.toString())

            binding.LayoutCampaignStart.endIconMode = TextInputLayout.END_ICON_CUSTOM
            binding.campaignStart.error = null
        } catch (e: ParseException) {
            binding.campaignStart.error = "Invalid date format use yyyy/mm/dd"
            binding.LayoutCampaignStart.endIconMode = TextInputLayout.END_ICON_NONE
        }
    }
    private fun newCampaign(campaignName: String,campaignDesc: String,campaignKeyword: String, startDate: String, endDate: String, token: String) {
        val client = ApiConfig.getApiService2(token).createCampaign(campaignName = campaignName, campaignDesc = campaignDesc, campaignPeriod = null, campaignKeyword = campaignKeyword, startDate = startDate, endDate = endDate, status = true)
        client.enqueue(object : Callback<NewCampaignResponse> {
            override fun onResponse(
                call: Call<NewCampaignResponse>,
                response: Response<NewCampaignResponse>
            ) {
                val responseBody = response.body()

                if (responseBody != null) {
                    val alertDialog = AlertDialog.Builder(this@NewCampaignActivity).apply {
                        setTitle("Success")
                        setMessage("Your Upload has been successful")

                        setPositiveButton("Back") { _, _ ->
                            val intent = Intent(this@NewCampaignActivity, DashboardRecruiterActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                            this@NewCampaignActivity.startActivity(intent)
                            this@NewCampaignActivity.finish()
                        }
                    }.create()

                    alertDialog.setOnShowListener {
                        val positiveButton = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE)
                        positiveButton?.let {
                            it.setTextColor(
                                ContextCompat.getColor(
                                    this@NewCampaignActivity,
                                    com.example.jobsterific.R.color.black
                                )
                            )
                        }
                    }
                    alertDialog.show()
                } else {
                    val alertDialog = AlertDialog.Builder(this@NewCampaignActivity).apply {
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
                                    this@NewCampaignActivity,
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
