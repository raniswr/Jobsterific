package com.example.jobsterific.di

import ApiConfig
import UserPreference
import android.content.Context
import com.example.jobsterific.user.repository.UserRepository
import com.example.jobsterific.user.viewmodel.UploadRepository
import dataStore

object Injection {
    fun provideRepository(context: Context, token: String): UploadRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        val apiService = ApiConfig.getApiService2(token)
        return UploadRepository.getInstance(pref, apiService)
    }



    fun provideRepositoryProfile(context: Context, token: String): UserRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        val apiService = ApiConfig.getApiService2(token)
        return UserRepository.getInstance(pref, apiService)
    }

    fun provideRepositoryUser(context: Context): UserRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        val apiService = ApiConfig.getApiService() // Assuming you have a function to get ApiService
        return UserRepository.getInstance(pref, apiService)
    }

}