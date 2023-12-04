package com.example.jobsterific.user.ui

import BatchAdapter
import ViewPagerAdapter
import android.content.Intent
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager.widget.ViewPager
import com.example.jobsterific.R
import com.example.jobsterific.databinding.FragmentHomeBinding
import com.example.jobsterific.recruiter.CourseRVModal
import com.example.jobsterific.recruiter.ui.HomeRecruiterFragment


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeRecruiterFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
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

        val categoryContainer: LinearLayout = binding!!.categoryContainer
        val categories = listOf("Category 1", "Category 2", "Category 3", "Category 4")
        for (category in categories) {
            for (category in categories) {
                addCategoryButton( category, isActive = false)
            }
        }
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val batchRV = binding!!.idRVBatch
        val batchList = getCompanyData() // Assuming you have a function to get company data
        val batchLayoutManager = LinearLayoutManager(context)

        batchRV.layoutManager = batchLayoutManager
        val batchAdapter = BatchAdapter(batchList, context)
        batchRV.adapter = batchAdapter


        binding?.idRVBatch?.adapter = batchAdapter

        batchAdapter.onItemClick = { clickedCourse ->
            val intent = Intent(requireContext(), DetailPageBatch::class.java)
            startActivity(intent)
        }
        viewPager = binding!!.idViewPager

        var imageList: MutableList<Int> = mutableListOf()
        imageList.add(R.drawable.image2)
        imageList.add(R.drawable.image2)
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

        categoryButton.setOnClickListener {
            setActiveCategory(categoryButton.id)
        }

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
        // Set background color based on the active state
        val backgroundColor = if (isActive) {
            // Set the color for the active button
            ContextCompat.getColor(requireContext(), R.color.black)
        } else {
            // Set the color for the inactive button
            ContextCompat.getColor(requireContext(), R.color.white)
        }
        val textColor = if (isActive) {
            // Set the color for the active button
            ContextCompat.getColor(requireContext(), R.color.white)
        } else {
            // Set the color for the inactive button
            ContextCompat.getColor(requireContext(), R.color.black)
        }
        val cornerRadius = resources.getDimensionPixelSize(R.dimen.button_corner_radius).toFloat()

        // Create a rounded shape drawable
        val shapeDrawable = GradientDrawable()
        shapeDrawable.setColor(backgroundColor)
        shapeDrawable.cornerRadius = cornerRadius

        // Set the drawable as the background
        button.background = shapeDrawable
        button.setTextColor(textColor)
        val typeface = ResourcesCompat.getFont(requireContext(), R.font.abezee_regular)
        button.typeface = typeface

    }






    private fun getCompanyData(): ArrayList<CourseRVModal> {
        val companyList = ArrayList<CourseRVModal>()
        // Add your company data here
        companyList.add(CourseRVModal("UI/UX Hiring Batch"))
        companyList.add(CourseRVModal("Tech Hiring Batch"))
        // Add more companies as needed
        return companyList
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

