package com.dicoding.storyapp.ui.main

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dicoding.storyapp.data.remote.retrofit.ApiConfig
import com.dicoding.storyapp.data.repository.StoryRepository
import com.dicoding.storyapp.di.StoryInjection

class MainViewModelFactory(private val storyRepository: StoryRepository) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                MainViewModel(storyRepository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: MainViewModelFactory? = null
        @JvmStatic
        fun getInstance(context: Context, token: String): MainViewModelFactory {
            if (INSTANCE == null) {
                synchronized(MainViewModelFactory::class.java) {
                    INSTANCE = MainViewModelFactory(StoryInjection.provideRepository(context, ApiConfig.getApiService(), token))
                }
            }
            return INSTANCE as MainViewModelFactory
        }
    }
}