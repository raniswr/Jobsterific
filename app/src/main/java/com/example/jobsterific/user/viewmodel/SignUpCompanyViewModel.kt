package com.example.jobsterific.user.viewmodel

import ApiConfig
import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.jobsterific.data.response.RegisterCompanyResponse
import retrofit2.Call
import retrofit2.Response

class SignUpCompanyViewModel (application: Application) : AndroidViewModel(application) {

    val registrationResult = MutableLiveData<String>()
    fun registerCompany(firstName: String,description : String ,email: String, password: String,sex: String,adress: String,phone: String, website: String) {
        val client = ApiConfig.getApiService().registerCompany(firstName = firstName, lastName = "", description = description,email = email,password = password,adress = adress,sex = sex,phone = phone,website = website)
        Log.d("hello","nyampe sini")

        client.enqueue(object : retrofit2.Callback<RegisterCompanyResponse> {
            override fun onResponse(call: Call<RegisterCompanyResponse>, response: Response<RegisterCompanyResponse>) {
                val responseBody = response.body()
                if (responseBody != null) {
                    registrationResult.value = ""

                } else {
                    registrationResult.value = "Gagal Register"
                }
            }

            override fun onFailure(call: Call<RegisterCompanyResponse>, t: Throwable) {

            }
        })
    }


}