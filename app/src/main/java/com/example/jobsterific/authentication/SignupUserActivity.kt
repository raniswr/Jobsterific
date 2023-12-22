package com.example.jobsterific.authentication

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
import com.example.jobsterific.databinding.ActivitySignupUserBinding
import com.example.jobsterific.user.viewmodel.SignUpUserViewModel

class SignupUserActivity : AppCompatActivity() {
    private lateinit var binding : ActivitySignupUserBinding
    private val signUpViewModel: SignUpUserViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupUserBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.signupUser.setOnClickListener { view ->
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)

            signUpViewModel.registrationResult.observe(this, { result ->
                if (result == "Success Register"){
                    val alertDialog = AlertDialog.Builder(this@SignupUserActivity).apply {
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
                        // Get the positive button
                        val positiveButton = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE)

                        // Apply the color filter to the positive button
                        positiveButton?.let {
                            it.setTextColor(
                                ContextCompat.getColor(
                                    this@SignupUserActivity,
                                    R.color.black
                                )
                            )
                        }
                    }

                    alertDialog.show()
                }else {
                    val alertDialog = AlertDialog.Builder(this@SignupUserActivity).apply {
                        setTitle("Sory...")
                        setMessage("Fail Register")

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
                                    this@SignupUserActivity,
                                    R.color.black
                                )
                            )
                        }
                    }

                    alertDialog.show()
                }
            })


            signUpViewModel.registerUser(
                binding.firstName.text.toString(),
                binding.lastName.text.toString(),
                binding.email.text.toString(),
                binding.password.text.toString(),
                binding.job.text.toString(),
                binding.gender.text.toString(),
                binding.adress.text.toString(),
                binding.number.text.toString(),


            )


        }
    }


private fun showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

}