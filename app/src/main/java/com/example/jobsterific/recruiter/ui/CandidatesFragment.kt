package com.example.jobsterific.recruiter.ui

import android.content.Intent
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.jobsterific.R
import com.example.jobsterific.databinding.FragmentCandidatesBinding
import com.example.jobsterific.recruiter.CourseRVModal
import com.example.jobsterific.recruiter.adapter.CandidatesAdapter

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [CandidatesFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CandidatesFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var binding: FragmentCandidatesBinding? = null

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
        binding = FragmentCandidatesBinding.inflate(inflater, container, false)
        val candidatesRV = binding!!.idRVCandidates
        val candidatesList = getCandidatesData() // Assuming you have a function to get company data
        val batchLayoutManager = LinearLayoutManager(context)

        candidatesRV.layoutManager = batchLayoutManager
        val candidatesAdapter = CandidatesAdapter(candidatesList, context)
        candidatesRV.adapter = candidatesAdapter
        binding?.idRVCandidates?.adapter = candidatesAdapter

        candidatesAdapter.onItemClick = { clickedCourse ->
            val intent = Intent(requireContext(), DetailCandidatesActivity::class.java)
            startActivity(intent)
        }
        val categoryContainer: LinearLayout = binding!!.categoryContainer
        val categories = listOf("Category 1", "Category 2", "Category 3", "Category 4")
        for (category in categories) {
            for (category in categories) {
                addCategoryButton( category, isActive = false)
            }
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

    private fun getCandidatesData(): ArrayList<CourseRVModal> {
        val candidatesList = ArrayList<CourseRVModal>()
        // Add your company data here
        candidatesList.add(CourseRVModal("Samuel Zakaria H"))
        candidatesList.add(CourseRVModal("Rani Prabaswari"))
        // Add more companies as needed
        return candidatesList
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment CandidatesFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CandidatesFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}