package com.example.jobsterific.recruiter.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.jobsterific.R
import com.example.jobsterific.databinding.FragmentHomeRecruiterBinding
import com.example.jobsterific.recruiter.CourseRVModal
import com.example.jobsterific.recruiter.adapter.SectionsPagerAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeRecruiterFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeRecruiterFragment : Fragment() {
    private var binding: FragmentHomeRecruiterBinding? = null
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    lateinit var courseRV: RecyclerView

    lateinit var courseList: ArrayList<CourseRVModal>

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
        // Inflate the layout for this fragment
        binding = FragmentHomeRecruiterBinding.inflate(inflater, container, false)
        val sectionsPagerAdapter = SectionsPagerAdapter(this)
        val viewPager: ViewPager2 = binding!!.viewPager
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = binding!!.tabs
        val iconResIds = arrayOf(
            R.drawable.ic_flag,
            R.drawable.ic_candidates,
            R.drawable.ic_contender
            // Add more icon resource IDs as needed
        )

        viewPager.isUserInputEnabled = false
        val tabLayoutMediator = TabLayoutMediator(tabs, viewPager) { tab, position ->
            val customView = layoutInflater.inflate(R.layout.custom_tab_layout, null)
            val iconImageView: ImageView = customView.findViewById(R.id.tab_icon)
            iconImageView.setImageResource(iconResIds[position])

            // Set the text for each tab
            val titleTextView: TextView = customView.findViewById(R.id.tab_text)
            titleTextView.text = resources.getString(TAB_TITLES[position])

            tab.customView = customView
        }

        tabLayoutMediator.attach()

        tabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                // Disable smooth animation when moving between tabs
                tab?.view?.setOnClickListener { viewPager.setCurrentItem(tab.position, false) }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}

            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })


        val defaultPage = 1 // Assuming "candidate" is at index 1
        viewPager.setCurrentItem(defaultPage, false)

        val appCompatActivity = activity as? AppCompatActivity
        appCompatActivity?.supportActionBar?.elevation = 0f
        return binding!!.root

    }

    companion object {

        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_text_1,
            R.string.tab_text_2,
            R.string.tab_text_3
        )
        /**
         *
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