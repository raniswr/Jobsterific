package com.example.jobsterific.user.ui

import ApiConfig
import ProfileUserViewModel
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.jobsterific.R
import com.example.jobsterific.ViewModelFactoryProfile
import com.example.jobsterific.data.response.BatchesItem
import com.example.jobsterific.data.response.MakeApplymentResponse
import com.example.jobsterific.databinding.ActivityDetailPageBatchBinding
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailPageBatch : AppCompatActivity() {
    private lateinit var binding: ActivityDetailPageBatchBinding
    var getData: BatchesItem? = null
    var token = ""
    private val viewModel by viewModels<ProfileUserViewModel> {
        ViewModelFactoryProfile.getInstance(applicationContext, token)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailPageBatchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val gson = Gson()
        val extras = intent?.extras
        getData  =  gson.fromJson(extras?.getString("detailUser"),BatchesItem::class.java)

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
        viewModel.getSession().observe(this) { user ->

            token = user.token
            Log.d("ini token", token.toString())
            }
        binding.myButton.setOnClickListener {
           makeApplyment(token, getData?.batchId.toString())
        }

    }
    private fun makeApplyment(token: String, batchId : String) {
        Log.d("ini batch yuhuu", batchId )
        val client = ApiConfig.getApiService2(token).makeApplyment(batchId)
        client.enqueue(object : Callback<MakeApplymentResponse> {
            override fun onResponse(
                call: Call<MakeApplymentResponse>,
                response: Response<MakeApplymentResponse>
            ) {
                val responseBody = response.body()
                if (responseBody != null) {
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

                } else {

                }
            }
            override fun onFailure(call: Call<MakeApplymentResponse>, t: Throwable) {}
        })
    }
}