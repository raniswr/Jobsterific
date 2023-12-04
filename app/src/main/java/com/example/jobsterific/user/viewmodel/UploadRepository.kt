package com.example.jobsterific.user.viewmodel

import UserPreference
import com.example.jobsterific.pref.UploadResumeModel


class UploadRepository private constructor(
    private val userPreference: UserPreference,
) {


    suspend fun saveSessionPathResume(user: UploadResumeModel) {
        userPreference.saveSessionPathResume(user)
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
            userPreference: UserPreference
        ): UploadRepository =
            instance ?: synchronized(this) {
                instance ?: UploadRepository(userPreference)
            }.also { instance = it }
    }

}
