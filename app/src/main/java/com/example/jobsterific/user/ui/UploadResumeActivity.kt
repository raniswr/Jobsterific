package com.example.jobsterific.user.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.pdf.PdfRenderer
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.ParcelFileDescriptor
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.jobsterific.R
import com.example.jobsterific.ViewModelFactory
import com.example.jobsterific.data.ResultState
import com.example.jobsterific.databinding.ActivityUploadResumeBinding
import com.example.jobsterific.pref.UploadResumeModel
import com.example.jobsterific.reduceFilePdf
import com.example.jobsterific.uriToPdfFile
import com.example.jobsterific.user.RealPathUtil
import com.example.jobsterific.user.viewmodel.UploadViewModel
import java.io.File
import java.io.IOException


class UploadResumeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUploadResumeBinding
    private var currentImageUri: Uri? = null
    var token= ""

   var READ_REQUEST_CODE = 1;

    private val viewModel by viewModels<UploadViewModel> {
        ViewModelFactory.getInstance(this, token)
    }


    private fun requestDocument() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.type = "application/pdf"
        startActivityForResult(intent, READ_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == READ_REQUEST_CODE  && resultCode == Activity.RESULT_OK) {
            currentImageUri = data?.data
            uploadResume()
            if(Build.VERSION.RELEASE < "12"){
                val path = RealPathUtil.getRealPath(applicationContext, currentImageUri!!)
                if (path != null) {
                    // Extract the file name from the path
                    val fileName = File(path).name

                    // Log the path and file name
                    Log.d("Path", "Full Path: $path")
                    Log.d("FileName", "File Name: $fileName")

                    // Check if 'fileName' is not null
                    if (fileName.isNotBlank()) {
                        // Perform your desired actions with 'path' and 'fileName'
                        previewPdf(applicationContext, path, binding.previewImageView)


                        viewModel.saveSessionPathResume(UploadResumeModel(path, fileName))


                    } else {
                        Log.e("Error", "File Name is blank or null")
                    }
                } else {
                    // Log an error if 'path' is null
                    Log.e("Error", "Path is null")
                }
            } else {
                data?.data?.let { previewPdfUri(applicationContext, it, binding.previewImageView) }
               showToast("Uploaded")

            }








        }
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUploadResumeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.getSession().observe(this) { user ->

            token = user.token
            Log.d("ini token", token.toString())

            }
        var user = viewModel.getSessionPathResume()

        if (user != null) {
            viewModel.getSessionPathResume().observe(this) { user ->
                val currentImageUri = user.uriPdf
                if(currentImageUri!=null){
                    previewPdf(applicationContext, currentImageUri.toString(), binding.previewImageView)

                }

            }

        }

        binding.button1.setOnClickListener {
            viewModel.deleteResume()
            viewModel. delete(token)
            refreshImage()
        }


        val READ_EXTERNAL_STORAGE_REQUEST_CODE = 1

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (Environment.isExternalStorageManager()) {

            } else {
                val intent = Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION)
                startActivityForResult(intent, READ_EXTERNAL_STORAGE_REQUEST_CODE)
            }
        } else {
            if (ContextCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                    READ_EXTERNAL_STORAGE_REQUEST_CODE
                )
            }
        }
        binding.submitButton.setOnClickListener {
            requestDocument()
        }
    }
    private fun refreshImage() {
//       binding.webView.visibility = View.INVISIBLE
        binding.previewImageView.setImageResource(0)
        binding.previewImageView.setBackgroundColor(ContextCompat.getColor(this, R.color.grey))
        binding.imageTextView.text = "Upload Resume"
        // Stop the refreshing animation
        binding.swipeRefreshLayout.isRefreshing = false
    }


//    private fun startGalleryForPdf() {
//        launcherGalleryForPdf.launch("application/pdf")
//    }
//
//    private val launcherGalleryForPdf = registerForActivityResult(
//        ActivityResultContracts.GetContent()
//    ) { uri: Uri? ->
//        if (uri != null) {
//            currentImageUri = uri
////                        viewModel.saveSessionPathResume(UploadResumeModel(uri , url))
//            previewPdf(applicationContext,currentImageUri!!, binding.previewImageView)
//
////            val webView = binding.webView
////                    webView.visibility = View.VISIBLE
////                    webView.settings.javaScriptEnabled = true
////                    webView.webViewClient = WebViewClient()
////
////                    val url = "https://storage.googleapis.com/demo-jobsterific/users-resumes/Samuel/2-Samuel-1702957465463.pdf?X-Goog-Algorithm=GOOG4-RSA-SHA256&X-Goog-Credential=demo-jobsterific%40ninth-arena-403511.iam.gserviceaccount.com%2F20231219%2Fauto%2Fstorage%2Fgoog4_request&X-Goog-Date=20231219T034434Z&X-Goog-Expires=604800&X-Goog-SignedHeaders=host&X-Goog-Signature=5b95aaf9a463fe3845764002afbaa8af22b6f0b2abe064b9e0ebce387cac27bf0144387fab4caf41d1291dcf5a9e81fb6da4219d2e2e31d5ef2bdf4d2cd1bdb9b3bbc6abceae55660fbe31603711f31a4ac98f650104615a5361ee673896e5250a748c7455c8360bfed164d32e548fddf9f6c20137d401939182c87684b1b010dcdca321b967055a50280d751a12e58dcf6eacab535808934685a918eec5768f33be3fcc95556916786b77a985db1c13bd242ca629d4b566c595910a91c0b8200e917d7916668768a454435d913ad6cc93ed00e8008715be3351a65686fefed60d874ef62bcb127d4cc4b6abd963206dbd8b65cd87bd42e8435fd946d10f7e0e"
////            val googleDocsViewerUrl = "https://docs.google.com/gview?embedded=true&url=$url"
////            viewModel.saveSessionPathResume(UploadResumeModel(url , url))
////                    webView.loadUrl(googleDocsViewerUrl)
////                    binding.imageTextView.text = ""
////                    val imageView = binding.previewImageView
////                    imageView.visibility = View.INVISIBLE
//        } else {
//            Log.d("PDF Picker", "No PDF selected")
//        }
//    }

    fun previewPdf(context: Context, pdfPath: String, imageView: ImageView) {
        try {
            val file = File(pdfPath)

            if (!file.exists()) {
                return
            }

            val parcelFileDescriptor = ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY)
            val pdfRenderer = PdfRenderer(parcelFileDescriptor)

            // Choose a page to render (0-based index)
            val pageIndex = 0

            if (pdfRenderer.pageCount <= pageIndex) {
                // Handle the case where the PDF does not have the specified page
                return
            }

            val page = pdfRenderer.openPage(pageIndex)

            // Adjust the scale factor as needed
            val scale = context.resources.displayMetrics.density
            val bitmap = Bitmap.createBitmap((page.width * scale).toInt(), (page.height * scale).toInt(), Bitmap.Config.ARGB_8888)

            // Render the page onto the Bitmap
            page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)

            // Set the Bitmap to the ImageView
            imageView.setImageBitmap(bitmap)
            imageView.setBackgroundColor(Color.TRANSPARENT)
            binding.imageTextView.text = ""

            // Close the page and the PdfRenderer
            page.close()
            pdfRenderer.close()

            // Close the ParcelFileDescriptor
            parcelFileDescriptor.close()
        } catch (e: java.io.IOException) {
            e.printStackTrace()
            // Handle the IOException
        } catch (e: Exception) {
            e.printStackTrace()
            // Handle other exceptions
        }
    }

    fun previewPdfUri(context: Context, pdfUri: Uri, imageView: ImageView) {
        try {
            val parcelFileDescriptor = context.contentResolver.openFileDescriptor(pdfUri, "r")
            parcelFileDescriptor?.let {
                val pdfRenderer = PdfRenderer(parcelFileDescriptor)

                // Choose a page to render (0-based index)
                val pageIndex = 0

                if (pdfRenderer.pageCount <= pageIndex) {
                    // Handle the case where the PDF does not have the specified page
                    return
                }

                val page = pdfRenderer.openPage(pageIndex)

                // Adjust the scale factor as needed
                val scale = context.resources.displayMetrics.density
                val bitmap = Bitmap.createBitmap((page.width * scale).toInt(), (page.height * scale).toInt(), Bitmap.Config.ARGB_8888)

                // Render the page onto the Bitmap
                page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)

                // Set the Bitmap to the ImageView
                imageView.setImageBitmap(bitmap)
                imageView.setBackgroundColor(Color.TRANSPARENT)

                // Close the page and the PdfRenderer
                page.close()
                pdfRenderer.close()

                // Close the ParcelFileDescriptor
                parcelFileDescriptor.close()
            }
        } catch (e: IOException) {
            e.printStackTrace()
            // Handle the IOException
        } catch (e: Exception) {
            e.printStackTrace()
            // Handle other exceptions
        }
    }





    private fun uploadResume() {
        currentImageUri?.let { uri ->
            val imageFile = uriToPdfFile(uri, this).reduceFilePdf(applicationContext)
            Log.d("Image File", "showImage: ${imageFile.path}")

            viewModel.uploadResume(imageFile, token).observe(this) { result ->
                if (result != null) {
                    when (result) {
                        is ResultState.Loading -> {
                            showLoading(true)
                        }

                        is ResultState.Success -> {
                            showToast(result.data.message!!)
//                            getResume(token)


                            showLoading(false)
                        }

                        is ResultState.Error -> {
                            showToast(result.error)
                            showLoading(false)
                        }
                    }
                }
            }
        } ?: showToast("Wrong file")
    }




    private fun showLoading(isLoading: Boolean) {
        binding.progressIndicator.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }


}