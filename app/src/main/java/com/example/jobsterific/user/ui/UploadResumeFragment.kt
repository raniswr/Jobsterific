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
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContract
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.jobsterific.R
import com.example.jobsterific.ViewModelFactory
import com.example.jobsterific.data.ResultState
import com.example.jobsterific.databinding.FragmentUploadResumeBinding
import com.example.jobsterific.pref.UploadResumeModel
import com.example.jobsterific.reduceFilePdf
import com.example.jobsterific.uriToPdfFile
import com.example.jobsterific.user.RealPathUtil
import com.example.jobsterific.user.viewmodel.UploadViewModel
import java.io.File
import java.io.IOException


class UploadResumeFragment : Fragment() {

    private var _binding: FragmentUploadResumeBinding? = null
    private val binding get() = _binding!!
    var token = ""
    private var currentImageUri: Uri? = null

    private val viewModel by lazy {
        ViewModelProvider(
            requireActivity(),
            ViewModelFactory.getInstance(requireContext(), token = token)
        ).get(UploadViewModel::class.java)
    }

    private val READ_REQUEST_CODE = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUploadResumeBinding.inflate(inflater, container, false)
        return binding.root
    }
//    private val pickDocumentLauncher =
//        registerForActivityResult(PickDocumentContract()) { uri: Uri? ->
//            if (uri != null) {
//                val path = RealPathUtil.getRealPath(requireContext(), uri)
//                if (path != null) {
//                    previewPdf(requireContext(), path, binding.previewImageView)
//                    viewModel.saveSessionPathResume(UploadResumeModel(path, path.substringAfterLast("/")))
//                }
//            }
//        }
//private val pickDocumentLauncher =
//    registerForActivityResult(PickDocumentContract()) { uri: Uri? ->
//        uri?.let {
//            val uri: Uri = uri
//            val filePath = getFilePathFromUri(requireContext(), uri)
//
//            if (filePath != null) {
//                // You have the file path, use it as needed
//                Log.d("FilePath", filePath)
//            } else {
//                // Unable to retrieve the file path
//                Log.d("FilePath", "Unable to retrieve the file path")
//            }
//
//        }
//    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.submitButton.setOnClickListener {
            requestDocument()
        }
        viewModel.getSession().observe(this) { user ->

            token = user.token
            Log.d("ini token", token.toString())
        }

        var user = viewModel.getSessionPathResume()

        if (user != null) {
            viewModel.getSessionPathResume().observe(this) { user ->
                val currentImageUri = user.uriPdf
                if (currentImageUri != null) {
                    previewPdf(
                        requireContext(),
                        currentImageUri.toString(),
                        binding.previewImageView
                    )

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
                    requireContext(),
                    android.Manifest.permission.READ_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                    READ_EXTERNAL_STORAGE_REQUEST_CODE
                )
            }
        }

    }



    private fun refreshImage() {
        binding.previewImageView.setImageResource(0)
        binding.previewImageView.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.grey))
        binding.imageTextView.text = "Upload Resume"
        binding.swipeRefreshLayout.isRefreshing = false
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
            if(Build.VERSION.RELEASE < "11"){
                val path = RealPathUtil.getRealPath(requireContext(), currentImageUri!!)
                if (path != null) {
                    // Extract the file name from the path
                    val fileName = File(path).name

                    // Log the path and file name
                    Log.d("Path", "Full Path: $path")
                    Log.d("FileName", "File Name: $fileName")

                    // Check if 'fileName' is not null
                    if (fileName.isNotBlank()) {
                        // Perform your desired actions with 'path' and 'fileName'
                        previewPdf(requireContext(), path, binding.previewImageView)


                        viewModel.saveSessionPathResume(UploadResumeModel(path, fileName))


                    } else {
                        Log.e("Error", "File Name is blank or null")
                    }
                } else {
                    // Log an error if 'path' is null
                    Log.e("Error", "Path is null")
                }
            } else {
                data?.data?.let { previewPdfUri(requireContext(), it, binding.previewImageView) }
                showToast("Uploaded")

            }








        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    private fun previewPdf(context: Context, pdfPath: String, imageView: ImageView) {
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
            val imageFile = uriToPdfFile(uri, requireContext()).reduceFilePdf(requireContext())
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
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }


}

class PickDocumentContract : ActivityResultContract<String, Uri?>() {
    override fun createIntent(context: Context, input: String): Intent {
        return Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = input
        }
    }

    override fun parseResult(resultCode: Int, intent: Intent?): Uri? {
        if (resultCode == android.app.Activity.RESULT_OK && intent != null) {
            return intent.data
        }
        return null
    }



}
