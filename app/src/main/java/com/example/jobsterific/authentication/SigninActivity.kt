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
import com.example.jobsterific.data.response.LoginUserResponse
import com.example.jobsterific.databinding.ActivitySigninBinding
import com.example.jobsterific.pref.UserModel
import com.example.jobsterific.recruiter.ui.DashboardRecruiterActivity
import com.example.jobsterific.user.ui.DashboardUserActivity
import com.example.jobsterific.user.viewmodel.SignInUserViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SigninActivity : AppCompatActivity() {
    private lateinit var binding : ActivitySigninBinding
    private val viewModel by viewModels<SignInUserViewModel> {
        ViewModelFactoryUser.getInstance(this)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySigninBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding?.signup?.setOnClickListener {
            val intent = Intent(this,SignupActivity::class.java)
            startActivity(intent)
        }

        binding?.signin?.setOnClickListener {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(it.windowToken, 0)
           LoginUser(binding.email.text.toString(), binding.password.text.toString())
        }

    }

    private fun LoginUser(email: String, password: String) {
        val client = ApiConfig.getApiService().loginUser(email=email, password=password)
        client.enqueue(object : Callback<LoginUserResponse> {
            override fun onResponse(
                call: Call<LoginUserResponse>,
                response: Response<LoginUserResponse>
            ) {
                val responseBody = response.body()
                var result = response.body()?.user
                if (response.isSuccessful) {
                if (responseBody != null) {


                        val phoneNumberString = result?.phone?.toString()
                            ?: ""

                        val numericPhoneNumberString =
                            phoneNumberString.replace("[^0-9]".toRegex(), "")

                        val phoneNumber: Int? = try {
                            numericPhoneNumberString.toInt()
                        } catch (e: NumberFormatException) {
                            // Handle the case where the conversion fails
                            null
                        }

                        viewModel.saveSession(
                            UserModel(
                                result?.token.toString(),
                                result?.userId.toString(),
                                result?.firstName.toString(),
                                result?.lastName.toString(),
                                result?.email.toString(),
                                result?.password.toString(),
                                phoneNumber,
                                result?.profile.toString(),
                                result?.sex.toString(),

                                result?.status!!,
                                result?.job.toString(),
                                result?.address.toString(),
                                result?.website.toString(),
                                result?.description.toString(),
                                true,
                                result?.isAdmin,
                                result?.isCustomer,
                            )
                        )
                        val alertDialog = AlertDialog.Builder(this@SigninActivity).apply {
                            setTitle("Success!")
                            setMessage("You are Sign In")

                            setPositiveButton("Next") { _, _ ->
                                if (result?.isCustomer == true) {
                                    val intent =
                                        Intent(context, DashboardRecruiterActivity::class.java)
                                    intent.flags =
                                        Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                                    startActivity(intent)
                                } else if (result?.isCustomer == false) {
                                    val intent = Intent(context, DashboardUserActivity::class.java)
                                    intent.flags =
                                        Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                                    startActivity(intent)
                                    finish()
                                }

                            }
                        }.create()

                        alertDialog.setOnShowListener {
                            val positiveButton = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE)
                            positiveButton?.let {
                                it.setTextColor(
                                    ContextCompat.getColor(
                                        this@SigninActivity,
                                        R.color.black
                                    )
                                )
                            }
                        }
                        alertDialog.show()

                    }
              }else {
                    // Handle unsuccessful response, e.g., display an error Toast
                    val errorMessage = "Error: ${response.message()}"
                    showToast(errorMessage)
                    showToast("Wrong Username and Password")
                    val alertDialog = AlertDialog.Builder(this@SigninActivity).apply {
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
                                    this@SigninActivity,
                                    R.color.black
                                )
                            )
                        }
                    }

                    alertDialog.show()
                }
            }
            override fun onFailure(call: Call<LoginUserResponse>, t: Throwable) {}
        })
    }
    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

}