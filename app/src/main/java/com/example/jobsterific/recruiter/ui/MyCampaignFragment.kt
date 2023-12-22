package com.example.jobsterific.recruiter.ui

import ApiConfig
import BatchAdapter
import MyCampaignAdapter
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.jobsterific.ViewModelFactoryProfile
import com.example.jobsterific.data.response.BatchItem
import com.example.jobsterific.data.response.MyBatchResponse
import com.example.jobsterific.databinding.FragmentMyCampaignBinding
import com.example.jobsterific.recruiter.viewmodel.ContenderViewModel
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MyCampaignFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MyCampaignFragment : Fragment() {

    private lateinit var binding: FragmentMyCampaignBinding
    var token = ""

    private lateinit var batchAdapter: BatchAdapter
    private val viewModel by viewModels<ContenderViewModel> {
        ViewModelFactoryProfile.getInstance(requireContext(), token)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMyCampaignBinding.inflate(inflater, container, false)

        val layoutManager = LinearLayoutManager(requireContext())
        binding!!.idRVCampaign.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(requireContext(), layoutManager.orientation)
        binding!!.idRVCampaign.addItemDecoration(itemDecoration)
        viewModel.getSession().observe(this) { user ->
            token = user.token

            Log.d("token inih", token.toString())
            getMyBatch(token, user.userId)
        }

        // Initialize batchAdapter here
        batchAdapter = BatchAdapter(emptyList(), requireContext())

        binding?.idRVCampaign?.adapter = batchAdapter

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.newCampaign.setOnClickListener {
            val intent = Intent(requireContext(), NewCampaignActivity::class.java)
            startActivity(intent)
        }
    }

    private fun getMyBatch(token: String, userId: String) {
        val client = ApiConfig.getApiService2(token = token).getMyBatch()
        client.enqueue(object : Callback<MyBatchResponse> {
            override fun onResponse(
                call: Call<MyBatchResponse>,
                response: Response<MyBatchResponse>
            ) {
                val responseBody = response.body()

                if (responseBody != null) {
                    setUserData(responseBody.batch.orEmpty())
                }
            }

            override fun onFailure(call: Call<MyBatchResponse>, t: Throwable) {
                // Handle failure
            }
        })
    }

    private var dataUsers: List<BatchItem> = emptyList()

    private fun setUserData(itemUser: List<BatchItem?>) {
        val batchItemList: List<BatchItem> = itemUser.filterNotNull()

        // Check if the fragment is attached to a context
        val context = context ?: return

        val adapter = MyCampaignAdapter(dataUsers, context)
        adapter.submitList(batchItemList)
        binding?.idRVCampaign?.adapter = adapter
        dataUsers = batchItemList

        adapter.onItemClick = {
            val intent = Intent(context, DetailMyCampaignActivity::class.java)
            intent.putExtra("detailUser", Gson().toJson(it))
            startActivity(intent)
        }
    }



}
