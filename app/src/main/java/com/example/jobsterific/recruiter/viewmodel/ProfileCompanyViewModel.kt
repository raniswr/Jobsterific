package com.example.jobsterific.recruiter.viewmodel


import ApiConfig
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.jobsterific.data.response.ProfileCompanyResponse
import com.example.jobsterific.pref.UserModel
import com.example.jobsterific.user.repository.UserRepository
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileCompanyViewModel(private val repository: UserRepository) : ViewModel() {


    private val _userProfile = MutableLiveData<ProfileCompanyResponse>()
    val userProfile: LiveData<ProfileCompanyResponse>
        get() = _userProfile

    fun getSession(): LiveData<UserModel> {
        val sessionFlow = repository.getSession()
        return sessionFlow?.asLiveData() ?: MutableLiveData<UserModel>().apply {
            value = null
        }
    }
    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }



    fun getProfile(token: String,userId : String ) {
        try {
            val call: Call<ProfileCompanyResponse> = ApiConfig.getApiService2(token).getCompanyProfile(userId)

            call.enqueue(object : Callback<ProfileCompanyResponse> {
                override fun onResponse(
                    call: Call<ProfileCompanyResponse>,
                    response: Response<ProfileCompanyResponse>
                ) {
                    if (response.isSuccessful) {
                        val profileResponse = response.body()
                        _userProfile.value = profileResponse!!
                    } else {
                    }
                }
                override fun onFailure(call: Call<ProfileCompanyResponse>, t: Throwable) {
                }
            })
        } catch (e: Exception) {
            // Handle exception if needed
        }
    }

}
