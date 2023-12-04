package com.example.jobsterific.user.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.jobsterific.MainActivity
import com.example.jobsterific.R
import com.example.jobsterific.databinding.ActivityEditProfileUserBinding

class EditProfileUserActivity : AppCompatActivity() {
    private lateinit var binding : ActivityEditProfileUserBinding
    private var currentImageUri: Uri? = null

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

        binding?.buttonSave?.setOnClickListener {

            val builder = AlertDialog.Builder(this,R.style.CustomAlertDialog)
                .create()
            val view = layoutInflater.inflate(R.layout.customview_layout,null)
            val  button = view.findViewById<Button>(R.id.dialogDismiss_button)
            val  button2 = view.findViewById<Button>(R.id.yes)
            builder.setView(view)
            button.setOnClickListener {
                builder.dismiss()
            }
            button2.setOnClickListener {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
            AlertDialog.Builder(this,R.style.CustomAlertDialog)
            builder.setCanceledOnTouchOutside(false)
            builder.show()
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

    companion object {
        private const val REQUIRED_PERMISSION = Manifest.permission.CAMERA
    }

}