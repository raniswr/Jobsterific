package com.example.jobsterific.user.ui

import ApiConfig
import ProfileUserViewModel
import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.jobsterific.R
import com.example.jobsterific.ViewModelFactoryProfile
import com.example.jobsterific.data.response.EditUserResponse
import com.example.jobsterific.databinding.ActivityEditProfileUserBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EditProfileUserActivity : AppCompatActivity() {
    private lateinit var binding : ActivityEditProfileUserBinding
    private var currentImageUri: Uri? = null
    var token = ""
    private val viewModel3 by viewModels<ProfileUserViewModel> {
        ViewModelFactoryProfile.getInstance(applicationContext, token)
    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
//                Toast.makeText(this, "Permission request granted", Toast.LENGTH_LONG).show()
            } else {
//                Toast.makeText(this, "Permission request denied", Toast.LENGTH_LONG).show()
            }
        }

    private fun allPermissionsGranted() =
        ContextCompat.checkSelfPermission(
            this,
            EditProfileUserActivity.REQUIRED_PERMISSION
        ) == PackageManager.PERMISSION_GRANTED


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileUserBinding.inflate(layoutInflater)


        setContentView(binding.root)
        val toolbar: androidx.appcompat.widget.Toolbar = findViewById(R.id.toolbar)

        // Set the toolbar as the action bar
        setSupportActionBar(toolbar)

        // Enable the Up button
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
      supportActionBar?.setDisplayShowTitleEnabled(false)
        // Handle the Up button click
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
        viewModel3.getSession().observe(this) { user ->

            token = user.token
            Log.d("ini token", token.toString())
            if(user.token != null){
                viewModel3.userProfile.observe(this) { user ->

                    var email = user.user?.email
                    binding!!.email.setText(email)
                    var  name =  user.user?.firstName
                    binding!!.editUsername.setText(name)
                    var  Lastname = user.user?.lastName
                    binding!!.lastname.setText(Lastname)
                    var phone = user.user?.phone.toString()
                    binding!!.number.setText(phone)
                    var job = user.user?.job.toString()
                    binding!!.job.setText(job)
                    var adress = user.user?.address.toString()
                    binding!!.adress.setText(adress)
                    binding!!.Username.text =" ${user.user?.firstName} ${user.user?.lastName} "

                }
                viewModel3.getProfile(token)
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
                button2.setOnClickListener {
                    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(it.windowToken, 0)
                    editUser(binding.editUsername.text.toString(), binding.lastname.text.toString(),  binding.adress.text.toString(), binding.email.text.toString(),  binding.job.text.toString(),  binding.number.text.toString(),  token)
                }
            }

            alertDialog.setOnShowListener {
                alertDialog.window?.setLayout((resources.displayMetrics.widthPixels * 0.7f).toInt(), WindowManager.LayoutParams.WRAP_CONTENT)
            }

            alertDialog.setCanceledOnTouchOutside(false)
            alertDialog.show()

        }


        if (!allPermissionsGranted()) {
            requestPermissionLauncher.launch(EditProfileUserActivity.REQUIRED_PERMISSION)
        }

        binding.actionImage.setOnClickListener {startGallery() }
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

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun editUser(firstName: String, lastName: String, adress: String, email: String, job: String,phone: String, token: String) {
        val client = ApiConfig.getApiService2(token).editUser(firstName = firstName, lastName = lastName, address = adress, email = email, job = job, phone = phone)
        client.enqueue(object : Callback<EditUserResponse> {
            override fun onResponse(
                call: Call<EditUserResponse>,
                response: Response<EditUserResponse>
            ) {
                val responseBody = response.body()

                if (responseBody != null) {
                    showToast(responseBody.message!!)
                    val intent = Intent(applicationContext, DashboardUserActivity::class.java)
                    startActivity(intent)
                } else {
                    val alertDialog = AlertDialog.Builder(this@EditProfileUserActivity).apply {
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
                                    this@EditProfileUserActivity,
                                    R.color.black
                                )
                            )
                        }
                    }

                    alertDialog.show()
                }
            }
            override fun onFailure(call: Call<EditUserResponse>, t: Throwable) {}
        })
    }

    companion object {
        private const val REQUIRED_PERMISSION = Manifest.permission.CAMERA
    }

}