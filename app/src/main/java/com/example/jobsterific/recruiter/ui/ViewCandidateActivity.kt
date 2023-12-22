package com.example.jobsterific.recruiter.ui

import ApiConfig
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.jobsterific.R
import com.example.jobsterific.ViewModelFactoryProfile
import com.example.jobsterific.data.response.ApplymentItem
import com.example.jobsterific.data.response.GetMyCandidateResponse
import com.example.jobsterific.databinding.ActivityViewCandidateBinding
import com.example.jobsterific.recruiter.CourseRVModal
import com.example.jobsterific.recruiter.adapter.ViewCandidateOfCampaignAdapter
import com.example.jobsterific.recruiter.viewmodel.ContenderViewModel
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ViewCandidateActivity : AppCompatActivity() {
    private lateinit var binding: ActivityViewCandidateBinding
    private lateinit var batchAdapter: ViewCandidateOfCampaignAdapter
    var token = ""
    private val viewModel by viewModels<ContenderViewModel> {
        ViewModelFactoryProfile.getInstance(applicationContext, token)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityViewCandidateBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val  batchId = intent.getStringExtra("batchId")
        val toolbar: androidx.appcompat.widget.Toolbar = findViewById(R.id.toolbar)

        setSupportActionBar(toolbar)


        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        val layoutManager = LinearLayoutManager(applicationContext)
        binding!!.idViewCandidates
            .layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(applicationContext, layoutManager.orientation)
        binding!!.idViewCandidates.addItemDecoration(itemDecoration)
        viewModel.getSession().observe(this) { user ->
            token = user.token
            Log.d("token inih", token.toString())
            getCandidate(token, batchId.toString())
        }

        batchAdapter = ViewCandidateOfCampaignAdapter(emptyList(),applicationContext)

        binding?.idViewCandidates?.adapter = batchAdapter
    }

    private fun getCandidateData(): ArrayList<CourseRVModal> {
        val companyList = ArrayList<CourseRVModal>()
        // Add your company data here
        companyList.add(CourseRVModal("A.A Rani Prabaswari Dewi"))
        companyList.add(CourseRVModal("A.A Rani Prabaswari Dewi"))
        // Add more companies as needed
        return companyList
    }
    private fun getCandidate(token: String,batchId: String) {

        val client = ApiConfig.getApiService2(token = token).getMycandidate(batchId = batchId)
        client.enqueue(object : Callback<GetMyCandidateResponse> {
            override fun onResponse(
                call: Call<GetMyCandidateResponse>,
                response: Response<GetMyCandidateResponse>
            ) {
                val responseBody = response.body()


                if (responseBody != null) {
                    setUserData(responseBody.applyment)
                }

            }
            override fun onFailure(call: Call<GetMyCandidateResponse>, t: Throwable) {

            }
        })


    }



    private var dataUsers: List<ApplymentItem?>? = emptyList()
    private fun setUserData(itemUser: List<ApplymentItem?>?) {
        val adapter = ViewCandidateOfCampaignAdapter(dataUsers,applicationContext)
        adapter.submitList(itemUser)
        binding!!.idViewCandidates.adapter = adapter
        dataUsers = itemUser

        adapter.onItemClick = {
            val intent = Intent(applicationContext, DetailMyCandidateActivity::class.java)
            intent.putExtra("detailUser", Gson().toJson(it))
            startActivity(intent)
        }
    }



}