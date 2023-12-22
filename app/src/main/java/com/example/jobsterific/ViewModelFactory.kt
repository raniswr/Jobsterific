package com.example.jobsterific

import ProfileUserViewModel
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.jobsterific.di.Injection
import com.example.jobsterific.recruiter.viewmodel.ContenderViewModel
import com.example.jobsterific.recruiter.viewmodel.ProfileCompanyViewModel
import com.example.jobsterific.user.repository.UserRepository
import com.example.jobsterific.user.viewmodel.ApplymentBatchViewModel
import com.example.jobsterific.user.viewmodel.HomeViewModel
import com.example.jobsterific.user.viewmodel.MainViewModel
import com.example.jobsterific.user.viewmodel.SignInUserViewModel
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
        fun getInstance(context: Context,  token: String): ViewModelFactory {
            return INSTANCE ?: synchronized(ViewModelFactory::class.java) {
                INSTANCE ?: ViewModelFactory(Injection.provideRepository(context = context, token = token )).also {
                    INSTANCE = it
                }
            }
        }
    }
}


class ViewModelFactoryUser private constructor(private val repository: UserRepository) :
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(SignInUserViewModel::class.java) -> {
                SignInUserViewModel(repository) as T
            }  modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                MainViewModel(repository) as T
            }

            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: ViewModelFactoryUser? = null

        @JvmStatic
        fun getInstance(context: Context): ViewModelFactoryUser {
            return INSTANCE ?: synchronized(ViewModelFactoryUser::class.java) {
                INSTANCE ?: ViewModelFactoryUser(Injection.provideRepositoryUser(context)).also {
                    INSTANCE = it
                }
            }
        }
    }
}


class ViewModelFactoryProfile private constructor(private val repository: UserRepository) :
    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(ProfileUserViewModel::class.java) -> {
                ProfileUserViewModel(repository) as T
            } modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                HomeViewModel(repository) as T
            }modelClass.isAssignableFrom(ContenderViewModel::class.java) -> {
                ContenderViewModel(repository) as T
            }modelClass.isAssignableFrom(ProfileCompanyViewModel::class.java) -> {
               ProfileCompanyViewModel(repository) as T
            }

            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: ViewModelFactoryProfile? = null

        @JvmStatic
        fun getInstance(context: Context,  token: String): ViewModelFactoryProfile {
            return INSTANCE ?: synchronized(ViewModelFactoryProfile::class.java) {
                INSTANCE ?: ViewModelFactoryProfile(Injection.provideRepositoryProfile(context = context, token = token )).also {
                    INSTANCE = it
                }
            }
        }
    }
}




