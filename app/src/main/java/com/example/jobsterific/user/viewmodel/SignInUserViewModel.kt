package com.example.jobsterific.user.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jobsterific.pref.UserModel
import com.example.jobsterific.user.repository.UserRepository
import kotlinx.coroutines.launch

class SignInUserViewModel (private val repository: UserRepository) : ViewModel() {
    fun saveSession(user: UserModel) {
        viewModelScope.launch {
            repository.saveSessionUser(user)
        }
    }
}