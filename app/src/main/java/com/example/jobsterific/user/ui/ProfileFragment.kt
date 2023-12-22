package com.example.jobsterific.user.ui

import ApiConfig
import ProfileUserViewModel
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.jobsterific.R
import com.example.jobsterific.ViewModelFactory
import com.example.jobsterific.ViewModelFactoryProfile
import com.example.jobsterific.authentication.WelcomeActivity
import com.example.jobsterific.data.response.LogoutResponse
import com.example.jobsterific.databinding.FragmentProfileBinding
import com.example.jobsterific.user.viewmodel.UploadViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class ProfileFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null
    private var binding: FragmentProfileBinding? = null
    var token = ""
    private val viewModel: UploadViewModel by viewModels {
        ViewModelFactory.getInstance(requireContext(), token)
    }

    private val viewModel3 by viewModels<ProfileUserViewModel> {
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
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        val toolbar: androidx.appcompat.widget.Toolbar = binding!!.toolbar

        val activity = requireActivity() as AppCompatActivity
        activity.setSupportActionBar(toolbar)

        activity.supportActionBar?.setDisplayShowTitleEnabled(false)
        val textOnclickResume: TextView = binding!!.textUpResume
        textOnclickResume.setOnClickListener {
            val intent = Intent(requireContext(), UploadResumeActivity::class.java)
            startActivity(intent)
        }

        val iconResume: ImageView = binding!!.iconUpload
iconResume.setOnClickListener  {
            val intent = Intent(requireContext(), UploadResumeActivity::class.java)
            startActivity(intent)
        }
        val NameResume: TextView = binding!!.statusResume
        viewModel.getSessionPathResume().observe(viewLifecycleOwner) { resumeFileName ->
            val nameResume = resumeFileName.fileName
            if(nameResume!=null){
                NameResume.text = nameResume
                if(NameResume.text == "") {
                    NameResume.text = "Upload Resume"
                }
            }
        }
        viewModel3.getSession().observe(this) { user ->

                token = user.token
            Log.d("ini token", token.toString())
            if(user.token != null){
                viewModel3.userProfile.observe(this) { user ->


                    binding!!.email.text = user.user?.email
                    Log.d("haloo", user.user?.email.toString())
                    binding!!.Username.text =" ${user.user?.firstName} ${user.user?.lastName} "
                    binding!!.number.text = user.user?.phone.toString()
                    binding!!.job.text = user.user?.job
                    binding!!.adress.text = user.user?.address
                    if(user.user?.status == true){
                        binding!!.status.text = "Hired"
                    }else{
                        binding!!.status.text = "Unhired"
                    }
                    binding!!.name.text = " ${user.user?.firstName} ${user.user?.lastName} "




                }
                viewModel3.getProfile(token)


            }




        }



        return binding!!.root


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setHasOptionsMenu(true)

    }
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_appbar, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_edit_profile -> {
                val intent = Intent(requireContext(), EditProfileUserActivity::class.java)
                startActivity(intent)
                true
            } R.id.action_up_resume -> {
                val intent = Intent(requireContext(), UploadResumeActivity::class.java)
                startActivity(intent)
                true
            }
            R.id.action_reset_pass -> {
                val intent = Intent(requireContext(), ResetPasswordUserActivity::class.java)
                startActivity(intent)
                true
            }
            R.id.applyment -> {
                val intent = Intent(requireContext(), MyApplymentActivity::class.java)
                startActivity(intent)
                true
            }
            R.id.action_logout -> {
                viewModel3.logout()
                val intent = Intent(requireContext(), WelcomeActivity::class.java)
                startActivity(intent)
                logout(token)

                true
            }
            else -> false

        }
    }
    private fun logout(token: String) {
        val client = ApiConfig.getApiService2(token).logout()
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
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ProfileFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}