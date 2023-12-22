package com.example.jobsterific.authentication

import ApiConfig
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.jobsterific.R
import com.example.jobsterific.ViewModelFactoryUser
import com.example.jobsterific.data.response.LoginCompanyResponse
import com.example.jobsterific.databinding.ActivitySigninCompanyBinding
import com.example.jobsterific.pref.UserModel
import com.example.jobsterific.recruiter.ui.DashboardRecruiterActivity
import com.example.jobsterific.user.viewmodel.SignInUserViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SigninCompanyActivity : AppCompatActivity() {
    private lateinit var binding : ActivitySigninCompanyBinding
    private val viewModel by viewModels<SignInUserViewModel> {
        ViewModelFactoryUser.getInstance(this)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySigninCompanyBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding?.signup?.setOnClickListener {
            val intent = Intent(this,SignupActivity::class.java)
            startActivity(intent)
        }

        binding?.signin?.setOnClickListener {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(it.windowToken, 0)
            LoginCompany(binding.email.text.toString(), binding.password.text.toString())
        }

    }

    private fun LoginCompany(email: String, password: String) {
        val client = ApiConfig.getApiService().loginCompany(email=email, password=password)
        client.enqueue(object : Callback<LoginCompanyResponse> {
            override fun onResponse(
                call: Call<LoginCompanyResponse>,
                response: Response<LoginCompanyResponse>
            ) {
                val responseBody = response.body()
                var result = response.body()?.customer

                if (responseBody != null) {
                    showToast(responseBody.message!!)

                    val email = binding.email.text.toString()
                    viewModel.saveSession(
                        UserModel(
                            result?.token.toString(),
                            result?.userId.toString(),
                            result?.firstName.toString(),
                            result?.lastName.toString(),
                            result?.email.toString(),
                            result?.password.toString(),
                            (result?.phone?.toLongOrNull()?.toInt()),
                            result?.profile.toString(),
                            result?.sex.toString(),
                            result?.status!!,
                            result?.job.toString(),
                            result?.address.toString(),
                            result?.website.toString(),
                            result?.description.toString(),
                            true,
                        )
                    )
                    val alertDialog = AlertDialog.Builder(this@SigninCompanyActivity).apply {
                        setTitle("Success!")
                        setMessage("You are Sign In")

                        setPositiveButton("Next") { _, _ ->
                            val intent = Intent(context, DashboardRecruiterActivity::class.java)
                            intent.flags =
                                Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                            startActivity(intent)
                            finish()
                        }
                    }.create()

                    alertDialog.setOnShowListener {
                        val positiveButton = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE)
                        positiveButton?.let {
                            it.setTextColor(
                                ContextCompat.getColor(
                                    this@SigninCompanyActivity,
                                    R.color.black
                                )
                            )
                        }
                    }
                    alertDialog.show()


                } else {
                    val alertDialog = AlertDialog.Builder(this@SigninCompanyActivity).apply {
                        setTitle("Sory...")
                        setMessage("Wrong Username and Password")

                        setPositiveButton("OK") { dialog, _ ->
                            dialog.dismiss()
                        }
                    }.create()

                    alertDialog.setOnShowListener {
                        // Get the positive button
                        val positiveButton = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE)

                        // Apply the color filter to the positive button
                        positiveButton?.let {
                            it.setTextColor(
                                ContextCompat.getColor(
                                    this@SigninCompanyActivity,
                                    R.color.black
                                )
                            )
                        }
                    }

                    alertDialog.show()
                }
            }
            override fun onFailure(call: Call<LoginCompanyResponse>, t: Throwable) {}
        })
    }
    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

}