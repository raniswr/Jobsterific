package com.example.jobsterific.recruiter.ui

import ApiConfig
import android.Manifest
import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.ContactsContract
import android.provider.Settings
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.jobsterific.R
import com.example.jobsterific.ViewModelFactoryProfile
import com.example.jobsterific.data.response.ApplymentItem
import com.example.jobsterific.data.response.DetailUserResponse
import com.example.jobsterific.databinding.ActivityDetailMyCandidateBinding
import com.example.jobsterific.recruiter.viewmodel.ProfileCompanyViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailMyCandidateActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailMyCandidateBinding
    var getData: ApplymentItem? = null
    var token = ""
    var pdfUrl = ""
    private val viewModel by viewModels<ProfileCompanyViewModel> {
        ViewModelFactoryProfile.getInstance(applicationContext, token)
    }
    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                // Permission granted, you can now send the email
                sendEmail()
            } else {
                // Permission denied, show a message or open app settings
                showSnackbarWithSettings()
            }
        }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailMyCandidateBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val gson = Gson()
        val extras = intent?.extras
        getData  =  gson.fromJson(extras?.getString("detailUser"), ApplymentItem::class.java)

        binding.nameTextView
            .text = "${getData?.user?.firstName} ${getData?.user?.lastName}"
        binding.adress
            .text = "${getData?.user?.address}"
        binding.email
            .text = getData?.user?.email

        if(getData?.status == true){
            binding.statusCandidate
                .text = "Hired"
        }
        binding.job
            .text = getData?.user?.job




        viewModel.getSession().observe(this) { user ->
            token = user.token
            Log.d("ini token", token.toString())
            getDetailUser(token = token, getData?.user?.userId.toString())

        }

        val toolbar: androidx.appcompat.widget.Toolbar = findViewById(R.id.toolbar)

        setSupportActionBar(toolbar)


        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
        binding.buttonEmail.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.INTERNET
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                // Permission already granted, send the email
                sendEmail()
            } else {
                // Permission not granted, request it
                requestPermissionLauncher.launch(Manifest.permission.INTERNET)
            }

        }

        binding.button2.setOnClickListener(View.OnClickListener {

//            getData?.user?.resume
            val request = DownloadManager.Request(Uri.parse(pdfUrl))
                .setTitle("Resume")
                .setDescription("Downloading PDF...")
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "modern-moon-mist.jpg")

            val downloadManager = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
            downloadManager.enqueue(request)
        })
        binding.buttonNumber.setOnClickListener (View.OnClickListener {
            val contactName = binding.nameTextView.text.toString()
            val phoneNumber = binding.number.text.toString()

            val intent = Intent(ContactsContract.Intents.Insert.ACTION)
            intent.type = ContactsContract.RawContacts.CONTENT_TYPE

            intent.putExtra(ContactsContract.Intents.Insert.NAME, contactName)
            intent.putExtra(ContactsContract.Intents.Insert.PHONE, phoneNumber)

            startActivity(intent)
        })
    }


    private fun sendEmail() {
        // Replace the placeholders with your email details
        val emailIntent = Intent(Intent.ACTION_SEND).apply {
            type = "message/rfc822"
            var email = binding.email.text.toString()
            putExtra(Intent.EXTRA_EMAIL, arrayOf(email))
        }

// Check if there is an app to handle the email intent
        if (emailIntent.resolveActivity(packageManager) != null) {
            startActivity(emailIntent)
        } else {
            // No app to handle the intent, show a message
            showSnackbar("No email app found")
        }
    }

    private fun showSnackbar(message: String) {
        Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_SHORT).show()
    }

    private fun showSnackbarWithSettings() {
        Snackbar.make(
            findViewById(android.R.id.content),
            "Permission denied. Open app settings to grant permission.",
            Snackbar.LENGTH_LONG
        )
            .setAction("SETTINGS") {
                openAppSettings()
            }
            .show()
    }

    private fun openAppSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", packageName, null)
        intent.data = uri
        startActivity(intent)
    }

    fun getDetailUser(token: String,userId : String ) {
        try {
            val call: Call<DetailUserResponse> = ApiConfig.getApiService2(token).getDetailUser(userId)
            call.enqueue(object : Callback<DetailUserResponse> {
                override fun onResponse(
                    call: Call<DetailUserResponse>,
                    response: Response<DetailUserResponse>
                ) {
                    if (response.isSuccessful) {
                        pdfUrl = response.body()!!.resume

                    } else {
                    }
                }
                override fun onFailure(call: Call<DetailUserResponse>, t: Throwable) {
                }
            })
        } catch (e: Exception) {
            // Handle exception if needed
        }
    }


}