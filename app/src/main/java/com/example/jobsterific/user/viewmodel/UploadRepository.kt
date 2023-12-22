package com.example.jobsterific.user.viewmodel

import ApiService
import UserPreference
import com.example.jobsterific.pref.UploadResumeModel
import com.example.jobsterific.pref.UserModel
import kotlinx.coroutines.flow.Flow


class UploadRepository private constructor(
    private val userPreference: UserPreference,
    private val apiService: ApiService,
) {


    suspend fun saveSessionPathResume(user: UploadResumeModel) {
        userPreference.saveSessionPathResume(user)
    }
    fun getSession(): Flow<UserModel> {
        return userPreference.getSessionUser()
    }

    fun getSessionPathResume(): kotlinx.coroutines.flow.Flow<UploadResumeModel> {
        return userPreference.getSessionPathResume()
    }

    suspend fun DeleteResume() {
        userPreference.deleteResume()
    }



    companion object {
        @Volatile
        private var instance: UploadRepository? = null
        fun getInstance(
            userPreference: UserPreference,
            apiService: ApiService,

        ): UploadRepository =
            instance ?: synchronized(this) {
                instance ?: UploadRepository(userPreference, apiService)
            }.also { instance = it }
    }

}
