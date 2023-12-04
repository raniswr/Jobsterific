package com.example.jobsterific

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.jobsterific.di.Injection
import com.example.jobsterific.user.viewmodel.ApplymentBatchViewModel
import com.example.jobsterific.user.viewmodel.UploadRepository
import com.example.jobsterific.user.viewmodel.UploadViewModel

class ViewModelFactory private constructor(private val repository: UploadRepository) :
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(UploadViewModel::class.java) -> {
                UploadViewModel(repository) as T
            }  modelClass.isAssignableFrom(ApplymentBatchViewModel::class.java) ->{
               ApplymentBatchViewModel(repository) as T
        }

            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: ViewModelFactory? = null

        @JvmStatic
        fun getInstance(context: Context): ViewModelFactory {
            return INSTANCE ?: synchronized(ViewModelFactory::class.java) {
                INSTANCE ?: ViewModelFactory(Injection.provideRepository(context)).also {
                    INSTANCE = it
                }
            }
        }
    }
}
