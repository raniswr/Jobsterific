package com.example.jobsterific.user.ui

import ApiConfig
import BatchAdapter
import ProfileUserViewModel
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.jobsterific.ViewModelFactoryProfile
import com.example.jobsterific.data.response.ApplymentItems
import com.example.jobsterific.data.response.MyApplymentResponse
import com.example.jobsterific.databinding.ActivityMyApplymentBinding
import com.example.jobsterific.recruiter.ui.DetailPageBatchCompanyActivity
import com.example.jobsterific.user.ui.adapter.MyApplymentAdapter
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyApplymentActivity : AppCompatActivity() {
    private var binding: ActivityMyApplymentBinding? = null
    var token = ""
    private lateinit var batchAdapter: BatchAdapter
    private val viewModel by viewModels<ProfileUserViewModel> {

        ViewModelFactoryProfile.getInstance(applicationContext, token)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyApplymentBinding.inflate(layoutInflater)
        setContentView(binding?.root)


        val layoutManager = LinearLayoutManager(applicationContext)
        binding!!.idRVBatch.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(applicationContext, layoutManager.orientation)
        binding!!.idRVBatch.addItemDecoration(itemDecoration)
        viewModel.getSession().observe(this) { user ->
            token = user.token
            Log.d("token inih", token.toString())
            getBatch(token)
        }

        // Initialize batchAdapter here
        batchAdapter = BatchAdapter(emptyList(), applicationContext)

        binding?.idRVBatch?.adapter = batchAdapter
    }

    private fun getBatch(token: String) {

        val client = ApiConfig.getApiService2(token = token).getMyApplyment()
        client.enqueue(object : Callback<MyApplymentResponse> {
            override fun onResponse(
                call: Call<MyApplymentResponse>,
                response: Response<MyApplymentResponse>
            ) {
                val responseBody = response.body()

                if (responseBody != null) {
                    setUserData(responseBody.applyment.orEmpty())

                }

            }
            override fun onFailure(call: Call<MyApplymentResponse>, t: Throwable) {

            }
        })


    }




    private var dataUsers: List<ApplymentItems> = emptyList()

    private fun setUserData(itemUser: List<ApplymentItems?>) {
        val applymentItemsList: List<ApplymentItems> = itemUser.filterNotNull()

        val adapter = MyApplymentAdapter(applymentItemsList, applicationContext)
        adapter.submitList(applymentItemsList)
        binding!!.idRVBatch.adapter = adapter
if(applymentItemsList == null){
    binding?.textViewHeader?.text = ""
}
        adapter.onItemClick = {
            val intent = Intent(applicationContext, DetailPageBatchCompanyActivity::class.java)
            intent.putExtra("detailUser", Gson().toJson(it))
            startActivity(intent)
        }

        dataUsers = applymentItemsList
    }
}