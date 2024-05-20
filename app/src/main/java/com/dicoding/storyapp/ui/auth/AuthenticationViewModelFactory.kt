package com.dicoding.storyapp.ui.auth

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dicoding.storyapp.data.repository.UserRepository
import com.dicoding.storyapp.di.UserInjection

class AuthenticationViewModelFactory(private val repository: UserRepository) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(AuthenticationViewModel::class.java) -> {
                AuthenticationViewModel(repository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: AuthenticationViewModelFactory? = null
        @JvmStatic
        fun getInstance(context: Context): AuthenticationViewModelFactory {
            if (INSTANCE == null) {
                synchronized(AuthenticationViewModelFactory::class.java) {
                    INSTANCE = AuthenticationViewModelFactory(UserInjection.provideRepository(context))
                }
            }
            return INSTANCE as AuthenticationViewModelFactory
        }
    }
}