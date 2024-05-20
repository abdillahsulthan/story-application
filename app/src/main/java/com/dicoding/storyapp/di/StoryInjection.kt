package com.dicoding.storyapp.di

import com.dicoding.storyapp.data.remote.retrofit.ApiService
import com.dicoding.storyapp.data.repository.StoryRepository

object StoryInjection {
    fun provideRepository(apiService: ApiService): StoryRepository {
        return StoryRepository.getInstance(apiService)
    }
}