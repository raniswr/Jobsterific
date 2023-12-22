package com.example.jobsterific.user.viewmodel

import ApiConfig
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.example.jobsterific.data.ResultState
import com.example.jobsterific.data.response.DeleteResponse
import com.example.jobsterific.data.response.UploadResumeResponse
import com.example.jobsterific.pref.UploadResumeModel
import com.example.jobsterific.pref.UserModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File


class UploadViewModel(private val repository: UploadRepository) : ViewModel() {
    fun getSessionPathResume(): LiveData<UploadResumeModel> {
        val sessionFlow = repository.getSessionPathResume()
        return sessionFlow?.asLiveData() ?: MutableLiveData<UploadResumeModel>().apply {
            value = null
        }
    }

    fun saveSessionPathResume(resume: UploadResumeModel) {
        viewModelScope.launch {
            repository.saveSessionPathResume(resume)
        }
    }

    fun getSession(): LiveData<UserModel> {
        val sessionFlow = repository.getSession()
        return sessionFlow?.asLiveData() ?: MutableLiveData<UserModel>().apply {
            value = null
        }
    }

//    fun uploadResume(pdfFile: File, token: String) = liveData(Dispatchers.IO) {
//        emit(ResultState.Loading)
//
//        val requestPdfFile = pdfFile.asRequestBody("application/pdf".toMediaType())
//
//        val multipartBody = MultipartBody.Part.createFormData(
//            "resume",
//            pdfFile.name,
//            requestPdfFile
//        )
//        try {
//            val successResponse = ApiConfig.getApiService2(token).uploadResume(multipartBody)
//            emit(ResultState.Success(successResponse))
//        } catch (e: HttpException) {
//            val errorBody = e.response()?.errorBody()?.string()
//            val errorResponse = Gson().fromJson(errorBody, UploadResumeResponse::class.java)
//            emit(ResultState.Error(errorResponse.message!!))
//        }
//    }
fun uploadResume(pdfFile: File, token: String) = liveData(Dispatchers.IO) {
Log.d("upload running", "yuhu")
    val client = OkHttpClient()
    val mediaType = "text/plain".toMediaType()
    val body = MultipartBody.Builder().setType(MultipartBody.FORM)
        .addFormDataPart("resume","1ee9d49f-857e-4f70-97ab-6ffdcc546544.pdf",
            File(pdfFile.path).asRequestBody("application/octet-stream".toMediaType()))
        .build()
    val request = Request.Builder()
        .url("http://34.101.235.222:5000/api/users/resume")
        .post(body)
        .addHeader("token", token)
        .build()
    val response = client.newCall(request).execute()
    Log.d("upload running", response.isSuccessful.toString())
  if (response.isSuccessful){
      emit(ResultState.Success(UploadResumeResponse("berhasil")))
  }else{
      emit(ResultState.Error("eror upload"))
  }

}
    fun delete(token: String) {
        try {
            val client = ApiConfig.getApiService2(token).deleteResume()
            client.enqueue(object : Callback<DeleteResponse> {
                override fun onResponse(
                    call: Call<DeleteResponse>,
                    response: Response<DeleteResponse>
                ) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        Log.d("Delete", responseBody.message.toString())
                    } else {
                        Log.d("Gagal delete", responseBody?.message.toString())
                    }
                }
                override fun onFailure(call: Call<DeleteResponse>, t: Throwable) {}
            })
        } catch (e: Exception) {
            // Handle exception if needed
        }
    }




    fun deleteResume() {
        viewModelScope.launch {
            repository.DeleteResume()
        }
    }

}