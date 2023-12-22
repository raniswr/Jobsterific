package com.example.jobsterific.authentication

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.jobsterific.R
import com.example.jobsterific.databinding.ActivitySignupRecruiterBinding
import com.example.jobsterific.user.viewmodel.SignUpCompanyViewModel

class SignupRecruiterActivity : AppCompatActivity() {
    private lateinit var binding : ActivitySignupRecruiterBinding
    private val signUpViewModel: SignUpCompanyViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupRecruiterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.signupRecruiter.setOnClickListener { view ->
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)

            signUpViewModel.registrationResult.observe(this@SignupRecruiterActivity, { result ->
                if (result != "Gagal Register") {
                    val alertDialog = AlertDialog.Builder(this@SignupRecruiterActivity).apply {
                        setTitle("Success!")
                        setMessage("User has been created")

                        setPositiveButton("Next") { _, _ ->
                            val intent = Intent(context, SigninActivity::class.java)
                            intent.flags =
                                Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                            startActivity(intent)
                            finish()
                        }
                    }.create()

                    alertDialog.setOnShowListener {
                        val positiveButton = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE)

                        // Apply the color filter to the positive button
                        positiveButton?.let {
                            it.setTextColor(
                                ContextCompat.getColor(
                                    this@SignupRecruiterActivity,
                                    R.color.black
                                )
                            )
                        }
                    }
                    alertDialog.show()
                } else {
                    val alertDialog = AlertDialog.Builder(this@SignupRecruiterActivity).apply {
                        setTitle("Sory...")
                        setMessage("Fail Register")

                        setPositiveButton("OK") { dialog, _ ->
                            dialog.dismiss()
                        }
                    }.create()

                    alertDialog.setOnShowListener {

                        val positiveButton = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE)
                        positiveButton?.let {
                            it.setTextColor(
                                ContextCompat.getColor(
                                    this@SignupRecruiterActivity,
                                    R.color.black
                                )
                            )
                        }
                    }
                    alertDialog.show()
                }
            })


            signUpViewModel.registerCompany(
                binding.firstName.text.toString(),
                binding.description.text.toString(),
                binding.emailEditText.text.toString(),
                binding.password.text.toString(),
                "FEMALE",
                binding.adress.text.toString(),
                binding.phone.text.toString(),
                binding.website.text.toString()
            )


        }
    }

}
