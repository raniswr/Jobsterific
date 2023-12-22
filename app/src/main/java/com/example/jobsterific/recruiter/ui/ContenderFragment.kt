package com.example.jobsterific.recruiter.ui

import ApiConfig
import ContenderAdapter
import android.content.Context
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
import com.example.jobsterific.data.response.GetAllUserResponseItem
import com.example.jobsterific.databinding.FragmentContenderBinding
import com.example.jobsterific.recruiter.CourseRVModal
import com.example.jobsterific.recruiter.viewmodel.ContenderViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// TODO: Rename parameter arguments, choose names that match
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ContenderFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ContenderFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    var token = ""
    private lateinit var contenderAdapter: ContenderAdapter
    private var binding: FragmentContenderBinding? = null
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
        binding = FragmentContenderBinding.inflate(inflater, container, false)
        val layoutManager = LinearLayoutManager(requireContext())
        binding!!.idRVContender.layoutManager = layoutManager

        val itemDecoration = DividerItemDecoration(requireContext(), layoutManager.orientation)
        binding!!.idRVContender.addItemDecoration(itemDecoration)
        getContender(token)

        val categoryContainer: LinearLayout = binding!!.categoryContainer
        val categories = listOf("Cloud", "Mobile", "Machine Learning", "Developer")

        contenderAdapter = ContenderAdapter(emptyList())

        for (category in categories) {
            addCategoryButton(category, isActive = false)
        }


    viewModel.getSession().observe(this) { user ->
            token = user.token
            Log.d("token inih", token.toString())

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
//            contenderAdapter.filterByCategory(categoryName)
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

    private fun getContenderData(): ArrayList<CourseRVModal> {
        val candidatesList = ArrayList<CourseRVModal>()

        candidatesList.add(CourseRVModal("A.A Rani Prabaswari Dewi"))
        candidatesList.add(CourseRVModal("A.A Rani Prabaswari Dewi"))

        return candidatesList
    }

    private fun getContender(token: String) {
        val client = ApiConfig.getApiService3(token).getAllUser()
        client.enqueue(object : Callback<List<GetAllUserResponseItem>> {
            override fun onResponse(
                call: Call<List<GetAllUserResponseItem>>,
                response: Response<List<GetAllUserResponseItem>>
            ) {

                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {

                        setUserData(responseBody, context)
                    }
                } else {

                }
            }
            override fun onFailure(call: Call<List<GetAllUserResponseItem>>, t: Throwable) {
//                binding.progressBar.visibility = View.INVISIBLE
            }
        })
    }
    private var dataUsers: List<GetAllUserResponseItem?>? = null
    private fun setUserData(itemUser: List<GetAllUserResponseItem>, context: Context?) {

        val adapter =ContenderAdapter( itemUser)
        adapter.submitList(itemUser)
        binding?.idRVContender?.adapter = adapter
        dataUsers = itemUser

    }
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ContenderFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ContenderFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}