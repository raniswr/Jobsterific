package com.example.jobsterific.user.viewmodel

import ApiConfig
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.jobsterific.data.response.RegisterUserResponse
import retrofit2.Call
import retrofit2.Response

class SignUpUserViewModel(application: Application) : AndroidViewModel(application) {

        val registrationResult = MutableLiveData<String>()
        fun registerUser(firstName: String,lastName: String, email: String, password: String, job: String,sex: String,adress: String,phone: String) {

            val client = ApiConfig.getApiService().register(firstName = firstName, lastName = lastName,  email = email, password = password, job = job, address = adress, sex = sex, phone =phone , isAdmin = false, isCustomer = false)

            client.enqueue(object : retrofit2.Callback<RegisterUserResponse> {
                override fun onResponse(call: Call<RegisterUserResponse>, response: Response<RegisterUserResponse>) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        registrationResult.value = responseBody.message!!
                    } else {
                        registrationResult.value = "Gagal Register"
                    }
                }

                override fun onFailure(call: Call<RegisterUserResponse>, t: Throwable) {

                }
            })
        }



}