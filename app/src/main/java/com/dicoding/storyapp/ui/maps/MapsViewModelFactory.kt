package com.dicoding.storyapp.ui.maps

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dicoding.storyapp.data.remote.retrofit.ApiConfig
import com.dicoding.storyapp.data.repository.StoryRepository
import com.dicoding.storyapp.di.StoryInjection

class MapsViewModelFactory(private val storyRepository: StoryRepository) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(MapsViewModel::class.java) -> {
                MapsViewModel(storyRepository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: MapsViewModelFactory? = null
        @JvmStatic
        fun getInstance(): MapsViewModelFactory {
            if (INSTANCE == null) {
                synchronized(MapsViewModelFactory::class.java) {
                    INSTANCE = MapsViewModelFactory(StoryInjection.provideRepository(ApiConfig.getApiService()))
                }
            }
            return INSTANCE as MapsViewModelFactory
        }
    }
}