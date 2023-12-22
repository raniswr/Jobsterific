package com.example.jobsterific.recruiter.ui

import ApiConfig
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.jobsterific.R
import com.example.jobsterific.ViewModelFactoryProfile
import com.example.jobsterific.authentication.WelcomeActivity
import com.example.jobsterific.data.response.LogoutResponse
import com.example.jobsterific.databinding.FragmentProfileRecruiterBinding
import com.example.jobsterific.recruiter.viewmodel.ProfileCompanyViewModel
import com.example.jobsterific.user.ui.ResetPasswordUserActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ProfileRecruiterFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProfileRecruiterFragment : Fragment() {
    private var binding: FragmentProfileRecruiterBinding? = null
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    var token = ""
    private val viewModel by viewModels<ProfileCompanyViewModel> {
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
        binding = FragmentProfileRecruiterBinding.inflate(inflater, container, false)
        val toolbar: androidx.appcompat.widget.Toolbar = binding!!.toolbar

        val activity = requireActivity() as AppCompatActivity
        activity.setSupportActionBar(toolbar)

        activity.supportActionBar?.setDisplayShowTitleEnabled(false)
        viewModel.getSession().observe(this) { user ->

            token = user.token
            Log.d("ini token", token.toString())
            if(user.token != null) {
                viewModel.userProfile.observe(this) { user ->


                    binding!!.email.text = user.customer?.email
                    binding!!.number.text = user.customer?.phone.toString()
                    binding!!.adress.text = user.customer?.address
                    binding!!.description.text = user.customer?.description.toString()
                    binding!!.website.text = user.customer?.website

                    binding!!.name.text = " ${user.customer?.firstName} "
                    binding!!.companyName.text =
                        " ${user.customer?.firstName} "
                }
                viewModel.getProfile(token, user.userId)
            }

            }
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.profile_menu_recruiter, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_edit_profile -> {
                val intent = Intent(requireContext(), EditCompanyProfileActivity::class.java)
                startActivity(intent)
                true
            }
            R.id.action_reset_pass -> {
                val intent = Intent(requireContext(), ResetPasswordUserActivity::class.java)
                startActivity(intent)
                true
            }
            R.id.action_logout -> {
                viewModel.logout()
                val intent = Intent(requireContext(), WelcomeActivity::class.java)
                startActivity(intent)
                logout(token)
                true
            }
            else -> false

        }
    }
    private fun logout(token: String) {
        val client = ApiConfig.getApiService2(token).logoutCompany()
        client.enqueue(object : Callback<LogoutResponse> {
            override fun onResponse(
                call: Call<LogoutResponse>,
                response: Response<LogoutResponse>
            ) {
                val responseBody = response.body()
                if (responseBody != null) {
                    Log.d("Logout", responseBody.message.toString())
                } else {
                    Log.d("Gagal Logout", responseBody?.message.toString())
                }
            }
            override fun onFailure(call: Call<LogoutResponse>, t: Throwable) {}
        })
    }
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ProfileRecruiterFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ProfileRecruiterFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}