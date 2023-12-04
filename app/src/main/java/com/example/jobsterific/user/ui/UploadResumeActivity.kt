package com.example.jobsterific.user.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.pdf.PdfRenderer
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.ParcelFileDescriptor
import android.provider.MediaStore
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
import com.example.jobsterific.databinding.ActivityUploadResumeBinding
import com.example.jobsterific.pref.UploadResumeModel
import com.example.jobsterific.user.RealPathUtil
import com.example.jobsterific.user.viewmodel.UploadViewModel
import java.io.File
import java.io.IOException


class UploadResumeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUploadResumeBinding

   var READ_REQUEST_CODE = 1;


    private val viewModel by viewModels<UploadViewModel> {
        ViewModelFactory.getInstance(this)
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
            val uri = data?.data
            val path = RealPathUtil.getRealPath(applicationContext, uri!!)

// Ensure 'path' is not null
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
                    // Log an error if 'fileName' is blank or null
                    Log.e("Error", "File Name is blank or null")
                }
            } else {
                // Log an error if 'path' is null
                Log.e("Error", "Path is null")
            }



        }
    }




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUploadResumeBinding.inflate(layoutInflater)
        setContentView(binding.root)


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
        // Implement the logic to refresh your image here
        // For example, load a new image, fetch it from a server, etc.

        // Simulate a delay for demonstration purposes
        binding.previewImageView.setImageResource(0)
        binding.previewImageView.setBackgroundColor(ContextCompat.getColor(this, R.color.grey))
        binding.imageTextView.text = "Upload Resume"
        // Stop the refreshing animation
        binding.swipeRefreshLayout.isRefreshing = false
    }



//    fun handleUri(uri: Uri) {
//        val path = getRealPathFromURI(applicationContext, uri)
//        if (path != null) {
//            showImage(uri)
//            viewModel.saveSession(UploadResumeModel(uri.toString()))
//            binding.imageTextView.text = ""
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
        } catch (e: IOException) {
            e.printStackTrace()
            // Handle the IOException
        } catch (e: Exception) {
            e.printStackTrace()
            // Handle other exceptions
        }
    }

//    private fun startGalleryForDocuments() {
//        launcherGalleryForDocuments.launch("application/pdf")
//    }





//    private val launcherGalleryForDocuments =
//        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
//            if (uri != null) {
//         showImage(uri)
//
//            }
//        }
    fun getPath(context: Context, uri: Uri): String? {
        val isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT
        Log.i("URI", uri.toString() + "")
        val result = uri.toString() + ""
        // DocumentProvider
        //  if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
        if (isKitKat && result.contains("media.documents")) {
            val ary = result.split("/".toRegex()).dropLastWhile { it.isEmpty() }
                .toTypedArray()
            val length = ary.size
            val imgary = ary[length - 1]
            val dat = imgary.split("%3A".toRegex()).dropLastWhile { it.isEmpty() }
                .toTypedArray()
            val docId = dat[1]
            val type = dat[0]
            var contentUri: Uri? = null
            if ("image" == type) {
                contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            } else if ("video" == type) {
            } else if ("audio" == type) {
            }
            val selection = "_id=?"
            val selectionArgs = arrayOf(
                dat[1]
            )
            return getDataColumn(context, contentUri, selection, selectionArgs)
        } else if ("content".equals(uri.scheme, ignoreCase = true)) {
            return getDataColumn(context, uri, null, null)
        } else if ("file".equals(uri.scheme, ignoreCase = true)) {
            return uri.path
        }
        return null
    }

    fun getDataColumn(
        context: Context,
        uri: Uri?,
        selection: String?,
        selectionArgs: Array<String>?
    ): String? {
        var cursor: Cursor? = null
        val column = "_data"
        val projection = arrayOf(
            column
        )
        try {
            cursor =
                context.contentResolver.query(uri!!, projection, selection, selectionArgs, null)
            if (cursor != null && cursor.moveToFirst()) {
                val column_index = cursor.getColumnIndexOrThrow(column)
                return cursor.getString(column_index)
            }
        } finally {
            cursor?.close()
        }
        return null
    }

//    private fun showImage(uri: Uri?) {
//        uri?.let { documentUri ->
//Log.d("doc show uri:",documentUri.toString());
//            var renderer: PdfRenderer? = null
//            var page: PdfRenderer.Page? = null
//
//            try {
//                val documentFile = DocumentFile.fromSingleUri(applicationContext, documentUri)
//                val parcelFileDescriptor =
//                    contentResolver.openFileDescriptor(documentFile!!.uri, "r")
//
//                if (parcelFileDescriptor != null) {
//                    renderer = PdfRenderer(parcelFileDescriptor)
//                    page = renderer.openPage(0)
//
//                    val bitmap = Bitmap.createBitmap(
//                        page.width, page.height, Bitmap.Config.ARGB_8888
//                    )
//                    page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)
//                    binding.previewImageView.setImageBitmap(bitmap)
//                } else {
//                    Log.d("PDF Viewer", "Failed to open PDF document")
//                }
//            } catch (e: IOException) {
//                Log.e("PDF Viewer", "Error rendering PDF: ${e.message}", e)
//            } finally {
//                // Close the page and the renderer in the finally block
//                page?.close()
//                renderer?.close()
//            }
//        }
//    }



    private fun uploadImage() {
//        currentImageUri?.let { uri ->
//            val imageFile = uriToFile(uri, this).reduceFileImage()
//            Log.d("Image File", "showImage: ${imageFile.path}")
//            val description = "Ini adalah deksripsi gambar"
//
//            viewModel.uploadImage(imageFile, description).observe(this) { result ->
//                if (result != null) {
//                    when (result) {
//                        is ResultState.Loading -> {
//                            showLoading(true)
//                        }
//
//                        is ResultState.Success -> {
//                            showToast(result.data.message)
//                            showLoading(false)
//                        }
//
//                        is ResultState.Error -> {
//                            showToast(result.error)
//                            showLoading(false)
//                        }
//                    }
//                }
//            }
//        } ?: showToast(getString(R.string.empty_image_warning))
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressIndicator.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

}