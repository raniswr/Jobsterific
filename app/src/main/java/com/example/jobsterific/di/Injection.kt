package com.example.jobsterific.di

import UserPreference
import android.content.Context
import com.example.jobsterific.user.viewmodel.UploadRepository
import dataStore

object Injection {
    // Function to provide UserRepository
    fun provideRepository(context: Context): UploadRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        return UploadRepository.getInstance(pref)
    }




}