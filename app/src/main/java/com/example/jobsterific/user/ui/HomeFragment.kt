package com.example.jobsterific.user.ui

import ApiConfig
import BatchAdapter
import ProfileUserViewModel
import ViewPagerAdapter
import android.content.Intent
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.os.Handler
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
import androidx.viewpager.widget.ViewPager
import com.example.jobsterific.R
import com.example.jobsterific.ViewModelFactoryProfile
import com.example.jobsterific.data.response.BatchesItem
import com.example.jobsterific.data.response.GetAllBatchResponse
import com.example.jobsterific.databinding.FragmentHomeBinding
import com.example.jobsterific.recruiter.ui.HomeRecruiterFragment
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Locale


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class HomeFragment : Fragment() {
    private var binding: FragmentHomeBinding? = null
    private var param1: String? = null
    private var param2: String? = null
    lateinit var viewPager: ViewPager
    lateinit var viewPagerAdapter: ViewPagerAdapter
    private val handler = Handler()
    private var currentPage = 0
    private val DELAY_MS: Long = 3000
    private val PERIOD_MS: Long = 5000
    private lateinit var batchAdapter: BatchAdapter

    var token = ""

    private val viewModel by viewModels<ProfileUserViewModel> {
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
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        binding!!.searchView.setupWithSearchBar(binding!!.searchBar)



        fun onQueryTextSubmit(query: String?): Boolean {
            binding!!.searchView.clearFocus()
            findUser(query.orEmpty(), token)
            return true
        }

        // When search view text change
        fun onQueryTextChange(newText: String?): Boolean {
            val searchText = newText?.toLowerCase(Locale.getDefault()) ?: ""
            if (searchText.isNotEmpty()) {
                // Do something with searchText
                findUser(searchText!!, token)
            }
            return false
        }

        binding?.searchView?.editText?.setOnEditorActionListener { textView, actionId, event ->
            val query = textView.text.toString()
            binding!!.searchView.hide()
            onQueryTextSubmit(query)
            onQueryTextChange(query)
            false
        }
//        val categoryContainer: LinearLayout = binding!!.categoryContainer
//        val categories = listOf("Category 1", "Category 2", "Category 3", "Category 4")
//        for (category in categories) {
//            for (category in categories) {
//                addCategoryButton( category, isActive = false)
//            }
//        }


        val layoutManager = LinearLayoutManager(requireContext())
        binding!!.idRVBatch.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(requireContext(), layoutManager.orientation)
        binding!!.idRVBatch.addItemDecoration(itemDecoration)

        // Initialize batchAdapter here
        batchAdapter = BatchAdapter(emptyList(), requireContext())

        binding?.idRVBatch?.adapter = batchAdapter
        viewModel.getSession().observe(this) { user ->
            token = user.token
            Log.d("ini token", token.toString())
            getBatch(token)
//            findUser("tech", token)
            if (user != null) {
                binding!!.name.text = "Hi ${user.firstName}!"
                if (user.status == true) {
                    binding!!.status.text = "Hired"
                }
                binding!!.adress.text = user.adress
            }
        }



        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        viewPager = binding!!.idViewPager

        var imageList: MutableList<Int> = mutableListOf()
        imageList.add(R.drawable.image2)
        imageList.add(R.drawable.image3)
        imageList.add(R.drawable.image2)

        viewPagerAdapter = ViewPagerAdapter(requireContext(), imageList)
        viewPager.adapter = viewPagerAdapter


        handler.postDelayed(runnable, DELAY_MS)
        val tabLayout =binding!!.tabLayout
        tabLayout.setupWithViewPager(viewPager, true)
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(runnable)
    }
    private val runnable: Runnable = object : Runnable {
        override fun run() {
            if (currentPage == viewPagerAdapter.count - 1) {
                currentPage = 0
            } else {
                currentPage++
            }
            viewPager.setCurrentItem(currentPage, true)
            handler.postDelayed(this, PERIOD_MS)
        }
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
//        }
//
//        val categoryContainer: LinearLayout = binding?.categoryContainer ?: return
//        categoryContainer.addView(categoryButton)
    }

//    private fun setActiveCategory(buttonId: Int) {
//        val categoryContainer: LinearLayout = binding!!.categoryContainer
//
//        for (i in 0 until categoryContainer.childCount) {
//            val child = categoryContainer.getChildAt(i) as Button
//            val isActive = child.id == buttonId
//            updateButtonColor(child, isActive)
//        }
//    }

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


    private var dataUsers: List<BatchesItem> = emptyList()
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
                // Handle failure
            }
        })
    }

    private fun findUser(search: String, token: String) {
        val client = ApiConfig.getApiService2(token).getFindBatch(search = search)
        client.enqueue(object : Callback<GetAllBatchResponse> {
            override fun onResponse(
                call: Call<GetAllBatchResponse>,
                response: Response<GetAllBatchResponse>
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        setUserData(responseBody.batches)
                        Log.d("search berhasil", responseBody.batches.toString())
                    }
                } else {
                    // Handle unsuccessful response
                }
            }

            override fun onFailure(call: Call<GetAllBatchResponse>, t: Throwable) {
                // Handle failure
            }
        })
    }

    private fun setUserData(itemUser: List<BatchesItem>) {

            batchAdapter = BatchAdapter(dataUsers, requireContext())
            binding!!.idRVBatch.adapter = batchAdapter
            batchAdapter.onItemClick = {
                val intent = Intent(requireContext(), DetailPageBatch::class.java)
                intent.putExtra("detailUser", Gson().toJson(it))
                startActivity(intent)
            }


        // Update the dataset in the adapter
        batchAdapter.submitList(itemUser)
        dataUsers = itemUser
    }










    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HomeRecruiterFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeRecruiterFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}

