package com.example.jobsterific


import android.content.Context
import android.graphics.Bitmap
import android.graphics.Matrix
import android.graphics.pdf.PdfRenderer
import android.net.Uri
import androidx.core.net.toUri
import androidx.exifinterface.media.ExifInterface
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.min

private const val MAXIMAL_SIZE = 1000000 //1 MB
private const val FILENAME_FORMAT = "yyyyMMdd_HHmmss"
private val timeStamp: String = SimpleDateFormat(FILENAME_FORMAT, Locale.US).format(Date())


fun createCustomTempFile(context: Context, extension: String = ""): File {
    val filesDir = context.externalCacheDir
    return File.createTempFile(timeStamp, extension, filesDir)
}

fun uriToPdfFile(pdfUri: Uri, context: Context): File {
    val pdfFile = createCustomTempFile(context, ".pdf")
    val inputStream: InputStream = context.contentResolver.openInputStream(pdfUri) ?: return pdfFile
    val outputStream = FileOutputStream(pdfFile)
    inputStream.use { input ->
        outputStream.use { output ->
            input.copyTo(output)
        }
    }
    return pdfFile
}

fun File.reduceFilePdf(context: Context, maxPages: Int = 5): File {
    val pdfFile = this
    val pdfRenderer = PdfRenderer(context.contentResolver.openFileDescriptor(pdfFile.toUri(), "r")!!)

    for (pageIndex in 0 until min(pdfRenderer.pageCount, maxPages)) {
        val page = pdfRenderer.openPage(pageIndex)

        // Process each page as needed (you can modify this part)

        // Close the page
        page.close()
    }

    // Close the PdfRenderer
    pdfRenderer.close()

    return pdfFile
}
fun Bitmap.getRotatedBitmap(file: File): Bitmap? {
    val orientation = ExifInterface(file).getAttributeInt(
        ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED
    )
    return when (orientation) {
        ExifInterface.ORIENTATION_ROTATE_90 -> rotateImage(this, 90F)
        ExifInterface.ORIENTATION_ROTATE_180 -> rotateImage(this, 180F)
        ExifInterface.ORIENTATION_ROTATE_270 -> rotateImage(this, 270F)
        ExifInterface.ORIENTATION_NORMAL -> this
        else -> this
    }
}

fun rotateImage(source: Bitmap, angle: Float): Bitmap? {
    val matrix = Matrix()
    matrix.postRotate(angle)
    return Bitmap.createBitmap(
        source, 0, 0, source.width, source.height, matrix, true
    )
}