package com.dicoding.storyapp.ui.uploadstory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class UploadViewModelFactory (private val token: String) : ViewModelProvider.NewInstanceFactory(){
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UploadStoryViewModel::class.java)) {
            return UploadStoryViewModel(token) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }

    companion object {
        @Volatile
        private var INSTANCE: UploadViewModelFactory? = null
        @JvmStatic
        fun getInstance(token: String): UploadViewModelFactory {
            if (INSTANCE == null) {
                synchronized(UploadViewModelFactory::class.java) {
                    INSTANCE = UploadViewModelFactory(token)
                }
            }
            return INSTANCE as UploadViewModelFactory
        }
    }

}