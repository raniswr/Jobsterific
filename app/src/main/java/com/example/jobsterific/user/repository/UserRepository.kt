package com.example.jobsterific.user.repository

import ApiService
import UserPreference
import com.example.jobsterific.pref.UserModel
import kotlinx.coroutines.flow.Flow


class UserRepository private constructor(
    private val userPreference: UserPreference,
) {
    suspend fun saveSessionUser(user: UserModel) {
        userPreference.saveSessionUser(user)
    }
    fun getSession(): Flow<UserModel> {
        return userPreference.getSessionUser()
    }
    suspend fun logout() {
        userPreference.logout()
    }

    companion object {
        @Volatile
        private var instance: UserRepository? = null
        fun getInstance(
            userPreference: UserPreference, apiService: ApiService
        ): UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository(userPreference)
            }.also { instance = it }
    }

}
