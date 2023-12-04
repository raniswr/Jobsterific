package com.example.jobsterific.recruiter.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.widget.Button
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.jobsterific.MainActivity
import com.example.jobsterific.R
import com.example.jobsterific.databinding.ActivityEditCompanyProfileBinding

class EditCompanyProfileActivity : AppCompatActivity() {
    private lateinit var binding : ActivityEditCompanyProfileBinding
    private var currentImageUri: Uri? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditCompanyProfileBinding.inflate(layoutInflater)


        setContentView(binding.root)
        val toolbar: androidx.appcompat.widget.Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        toolbar.setNavigationOnClickListener {
            onBackPressed()
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
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }

            alertDialog.setOnShowListener {
                // Set the width of the AlertDialog when it is shown
                alertDialog.window?.setLayout((resources.displayMetrics.widthPixels * 0.7f).toInt(), WindowManager.LayoutParams.WRAP_CONTENT)
            }

            alertDialog.setCanceledOnTouchOutside(false)
            alertDialog.show()



//
//            val view = layoutInflater.inflate(R.layout.customview_layout, null)
//
//            val mDialogBuilder = AlertDialog.Builder(this, R.style.CustomAlertDialog)
//            mDialogBuilder.apply {
//                setView(view) // Set the custom view here
//            }
//
//            val mDialog = mDialogBuilder.create()
//            mDialog.show()
//
//            val displayMetrics = DisplayMetrics()
//            windowManager.defaultDisplay.getMetrics(displayMetrics)
//
//            val mDisplayWidth = displayMetrics.widthPixels
//            val mDisplayHeight = displayMetrics.heightPixels
//
//            val mLayoutParams = WindowManager.LayoutParams()
//            mLayoutParams.width = (mDisplayWidth * 0.5f).toInt()
//            mLayoutParams.height = (mDisplayHeight * 0.5f).toInt()
//
//            mLayoutParams.gravity = Gravity.CENTER
//
//            mDialog.window?.attributes = mLayoutParams

        }
        binding.actionImage.setOnClickListener {startGallery()}
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