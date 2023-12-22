package com.example.jobsterific.recruiter.ui

import ApiConfig
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.jobsterific.R
import com.example.jobsterific.ViewModelFactoryProfile
import com.example.jobsterific.data.response.EditCompanyProfileResponse
import com.example.jobsterific.databinding.ActivityEditCompanyProfileBinding
import com.example.jobsterific.recruiter.viewmodel.ProfileCompanyViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EditCompanyProfileActivity : AppCompatActivity() {
    private lateinit var binding : ActivityEditCompanyProfileBinding
    private var currentImageUri: Uri? = null
    var token = ""
    var userId =""
    private val viewModel by viewModels<ProfileCompanyViewModel> {
        ViewModelFactoryProfile.getInstance(applicationContext, token)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditCompanyProfileBinding.inflate(layoutInflater)


        setContentView(binding.root)
        val toolbar: androidx.appcompat.widget.Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        toolbar.setNavigationOnClickListener {
            val intent = Intent(applicationContext, DashboardRecruiterActivity::class.java)
            startActivity(intent)
        }
        viewModel.getSession().observe(this) { user ->

            token = user.token
            Log.d("ini token", token.toString())

            if(user.token != null){
                viewModel.userProfile.observe(this) { user ->

                    var email = user.customer?.email
                    binding!!.email.setText(email)
                    var  name = " ${user.customer?.firstName}"
                    binding!!.companyNameEdit.setText(name)

                    var description = user.customer?.description.toString()
                    binding!!.editDescription.setText(description)
                    var adress = user.customer?.address.toString()
                    binding!!.adress.setText(adress)
                    var website = user.customer?.website.toString()
                    binding!!.website.setText(website)
                    binding!!.companyName.text =" ${user.customer?.firstName} "
                    userId = user.customer?.userId.toString()
                }
                viewModel.getProfile(userId = user.userId, token = token)
            }


        }

        binding?.buttonSave?.setOnClickListener {
            val builder = AlertDialog.Builder(this, R.style.CustomAlertDialog)
            val view = layoutInflater.inflate(R.layout.customview_layout, null)
            val button = view.findViewById<Button>(R.id.dialogDismiss_button)
            val button2 = view.findViewById<Button>(R.id.yes)

            builder.setView(view)
            val alertDialog = builder.create()

            button.setOnClickListener {
                alertDialog.dismiss()
            }

            button2.setOnClickListener {
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(it.windowToken, 0)
                editCompany(binding.companyNameEdit.text.toString(),binding.adress.text.toString(),binding.email.text.toString(), binding.editDescription.text.toString(), binding.website.text.toString(), token,userId )

            }

            alertDialog.setOnShowListener {
                // Set the width of the AlertDialog when it is shown
                alertDialog.window?.setLayout((resources.displayMetrics.widthPixels * 0.7f).toInt(), WindowManager.LayoutParams.WRAP_CONTENT)
            }

            alertDialog.setCanceledOnTouchOutside(false)
            alertDialog.show()

        }
        binding.actionImage.setOnClickListener {startGallery()}
    }


    private fun editCompany(firstName: String,address: String, email: String, description: String,website: String, token: String,userId: String) {
        val client = ApiConfig.getApiService2(token).editCompanyProfile( firstName = firstName, userId = userId, website = website, email = email, address = address, description = description )
        client.enqueue(object : Callback<EditCompanyProfileResponse> {
            override fun onResponse(
                call: Call<EditCompanyProfileResponse>,
                response: Response<EditCompanyProfileResponse>
            ) {
                val responseBody = response.body()

                if (responseBody != null) {
                    val alertDialog = AlertDialog.Builder(this@EditCompanyProfileActivity).apply {
                        setTitle("Success")
                        setMessage("Success Edit")
                        setPositiveButton("Back") { dialog, _ ->
                            val intent = Intent(applicationContext, EditCompanyProfileActivity::class.java)
                            startActivity(intent)
                        }
                    }.create()

                    alertDialog.setOnShowListener {
                        // Get the positive button
                        val positiveButton = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE)

                        positiveButton?.let {
                            it.setTextColor(
                                ContextCompat.getColor(
                                    this@EditCompanyProfileActivity,
                                    R.color.black
                                )
                            )
                        }
                    }

                    alertDialog.show()

                } else {
                    val alertDialog = AlertDialog.Builder(this@EditCompanyProfileActivity).apply {
                        setTitle("Sory...")
                        setMessage("Fail Edit Profile")

                        setPositiveButton("OK") { dialog, _ ->
                            dialog.dismiss()
                        }
                    }.create()

                    alertDialog.setOnShowListener {
                        // Get the positive button
                        val positiveButton = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE)

                        positiveButton?.let {
                            it.setTextColor(
                                ContextCompat.getColor(
                                    this@EditCompanyProfileActivity,
                                    R.color.black
                                )
                            )
                        }
                    }

                    alertDialog.show()
                }
            }
            override fun onFailure(call: Call<EditCompanyProfileResponse>, t: Throwable) {}
        })
    }


    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            currentImageUri = uri
            showImage()
        } else {
            Log.d("Photo Picker", "No media selected")
        }
    }




    private fun showImage() {
        currentImageUri?.let {
            Log.d("Image URI", "showImage: $it")
            binding.image.setImageURI(it)
        }
    }

}