package com.example.jobsterific.recruiter.ui

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
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.jobsterific.R
import com.example.jobsterific.databinding.ActivityDetailCandidatesBinding
import com.google.android.material.snackbar.Snackbar

class DetailCandidatesActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailCandidatesBinding
    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                sendEmail()
            } else {
                showSnackbarWithSettings()
            }
        }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailCandidatesBinding.inflate(layoutInflater)
        setContentView(binding.root)
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
                sendEmail()
            } else {
                requestPermissionLauncher.launch(Manifest.permission.INTERNET)
            }

        }

        binding.button2.setOnClickListener(View.OnClickListener {
            val pdfUrl = "https://www.bikincv.com/blog/wp-content/uploads/2021/02/modern-moon-mist.jpg"

            val request = DownloadManager.Request(Uri.parse(pdfUrl))
                .setTitle("Your PDF Title")
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
}