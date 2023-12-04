package com.example.jobsterific.user.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.jobsterific.pref.UploadResumeModel
import kotlinx.coroutines.launch

class UploadViewModel(private val repository: UploadRepository) : ViewModel() {
    fun getSessionPathResume(): LiveData<UploadResumeModel> {
        val sessionFlow = repository.getSessionPathResume()
        return sessionFlow?.asLiveData() ?: MutableLiveData<UploadResumeModel>().apply {
            value = null
        }
    }

    fun saveSessionPathResume(resume: UploadResumeModel) {
        viewModelScope.launch {
            repository.saveSessionPathResume(resume)
        }
    }




    fun deleteResume() {
        viewModelScope.launch {
            repository.DeleteResume()
        }
    }

}