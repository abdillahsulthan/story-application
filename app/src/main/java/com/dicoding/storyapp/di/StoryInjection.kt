package com.dicoding.storyapp.di

import android.content.Context
import com.dicoding.storyapp.data.database.StoryDatabase
import com.dicoding.storyapp.data.remote.retrofit.ApiService
import com.dicoding.storyapp.data.repository.StoryRepository

object StoryInjection {
    fun provideRepository(context: Context, apiService: ApiService, token: String): StoryRepository {
        val database = StoryDatabase.getDatabase(context)
        return StoryRepository.getInstance(database, apiService, token)
    }
}