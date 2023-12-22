package com.example.jobsterific.recruiter.ui

import ApiConfig
import BatchAdapter
import android.content.Intent
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.jobsterific.R
import com.example.jobsterific.ViewModelFactoryProfile
import com.example.jobsterific.data.response.BatchesItem
import com.example.jobsterific.data.response.GetAllBatchResponse
import com.example.jobsterific.databinding.FragmentCampaignBinding
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
 * Use the [CampaignFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CampaignFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    var token = ""
    private var binding: FragmentCampaignBinding? = null
    val categories = listOf("All", "Tech Hiring", "UI/UX", "Category 3", "Category 4")
    private lateinit var batchAdapter: BatchAdapter
    private val viewModel by viewModels<ContenderViewModel> {
        ViewModelFactoryProfile.getInstance(requireContext(), token)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
//        binding = FragmentCampaignBinding.inflate(inflater, container, false)
//        val layoutManager = LinearLayoutManager(requireContext())
//        binding!!.idRVBatch.layoutManager = layoutManager
//        val itemDecoration = DividerItemDecoration(requireContext(), layoutManager.orientation)
//        binding!!.idRVBatch.addItemDecoration(itemDecoration)
//        viewModel.getSession().observe(this) { user ->
//            token = user.token
//            Log.d("token inih", token.toString())
//            getBatch(token)
//        }
//
//        binding?.idRVBatch?.adapter = batchAdapter
//
//
////        batchAdapter.onItemClick = { clickedCourse ->
////            val intent = Intent(requireContext(), DetailPageBatchCompanyActivity::class.java)
////            startActivity(intent)
////        }
//
//
//        val categoryContainer: LinearLayout = binding!!.categoryContainer
//
//        for (category in categories) {
//            for (category in categories) {
//                addCategoryButton( category, isActive = false)
//            }
//        }
//
//        return binding!!.root
        binding = FragmentCampaignBinding.inflate(inflater, container, false)
        val layoutManager = LinearLayoutManager(requireContext())
        binding!!.idRVBatch.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(requireContext(), layoutManager.orientation)
        binding!!.idRVBatch.addItemDecoration(itemDecoration)
        viewModel.getSession().observe(this) { user ->
            token = user.token
            Log.d("token inih", token.toString())
            getBatch(token)
        }

        // Initialize batchAdapter here
        batchAdapter = BatchAdapter(emptyList(), requireContext())

        binding?.idRVBatch?.adapter = batchAdapter

        for (category in categories) {
            addCategoryButton(category, isActive = false)
        }

        return binding!!.root
    }



    private fun addCategoryButton(categoryName: String, isActive: Boolean) {
        val categoryButton = Button(requireContext())
        val layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )

        val margin = resources.getDimensionPixelSize(R.dimen.button_margin)
        layoutParams.setMargins(margin, 0, 0, 0)

        categoryButton.id = View.generateViewId()
        categoryButton.text = categoryName
        categoryButton.isAllCaps = false
        categoryButton.layoutParams = layoutParams
        updateButtonColor(categoryButton, isActive)


//        categoryButton.setOnClickListener {
//            setActiveCategory(categoryButton.id)
//            Log.d("BatchAdapter", "Clicked category: $categoryName")
//            if (categoryName == "All") {
//                 batchAdapter.showAll()
//            } else {
//                batchAdapter.filter(categoryName)
//            }
//        }

        val categoryContainer: LinearLayout = binding?.categoryContainer ?: return
        categoryContainer.addView(categoryButton)
    }

    private fun setActiveCategory(buttonId: Int) {
        val categoryContainer: LinearLayout = binding!!.categoryContainer

        for (i in 0 until categoryContainer.childCount) {
            val child = categoryContainer.getChildAt(i) as Button
            val isActive = child.id == buttonId
            updateButtonColor(child, isActive)
        }
    }

    private fun updateButtonColor(button: Button, isActive: Boolean) {
        val backgroundColor = if (isActive) {
            ContextCompat.getColor(requireContext(), R.color.black)
        } else {
            ContextCompat.getColor(requireContext(), R.color.white)
        }
        val textColor = if (isActive) {
            ContextCompat.getColor(requireContext(), R.color.white)
        } else {
            ContextCompat.getColor(requireContext(), R.color.black)
        }
        val cornerRadius = resources.getDimensionPixelSize(R.dimen.button_corner_radius).toFloat()

        val shapeDrawable = GradientDrawable()
        shapeDrawable.setColor(backgroundColor)
        shapeDrawable.cornerRadius = cornerRadius

        button.background = shapeDrawable
        button.setTextColor(textColor)
        val typeface = ResourcesCompat.getFont(requireContext(), R.font.abezee_regular)
        button.typeface = typeface

    }



    private fun getBatch(token: String) {

        val client = ApiConfig.getApiService2(token = token).getAllBatch()
        client.enqueue(object : Callback<GetAllBatchResponse> {
            override fun onResponse(
                call: Call<GetAllBatchResponse>,
                response: Response<GetAllBatchResponse>
            ) {
                val responseBody = response.body()

                if (responseBody != null) {
                    setUserData(responseBody.batches)
                }

            }
            override fun onFailure(call: Call<GetAllBatchResponse>, t: Throwable) {

            }
        })


    }



    private var dataUsers: List<BatchesItem> = emptyList()
    private fun setUserData(itemUser: List<BatchesItem>) {
        val adapter = BatchAdapter(dataUsers,requireContext())
        adapter.submitList(itemUser)
        binding!!.idRVBatch.adapter = adapter
        dataUsers = itemUser

        adapter.onItemClick = {
            val intent = Intent(requireContext(), DetailPageBatchCompanyActivity::class.java)
            intent.putExtra("detailUser", Gson().toJson(it))
            startActivity(intent)
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment CampaignFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CampaignFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}