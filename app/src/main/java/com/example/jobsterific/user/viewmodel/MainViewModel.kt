package com.example.jobsterific.user.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.jobsterific.pref.UserModel
import com.example.jobsterific.user.repository.UserRepository

class MainViewModel(private val repository: UserRepository) : ViewModel() {
    fun getSession(): LiveData<UserModel> {
        val sessionFlow = repository.getSession()
        return sessionFlow?.asLiveData() ?: MutableLiveData<UserModel>().apply {
            value = null
        }
    }
}