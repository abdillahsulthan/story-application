package com.dicoding.storyapp.data.repository

import android.util.Log
import com.dicoding.storyapp.data.remote.response.Story
import com.dicoding.storyapp.data.remote.retrofit.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class StoryRepository (
    private val apiService: ApiService,
) {

    suspend fun getStories(token: String) : List<Story> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.getStories("Bearer $token")
                response.listStory
            } catch (e: Exception) {
                Log.e(TAG, "getStories: ${e.message}", e)
                emptyList()
            }
        }
    }

    suspend fun getStoriesWithLocation(token: String): List<Story> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.getStoriesWithLocation("Bearer $token")
                response.listStory
            } catch (e: Exception) {
                Log.e(TAG, "getStoriesWithLocation: ${e.message}", e)
                emptyList()
            }
        }
    }

    companion object {
        private const val TAG = "StoryRepository"

        @Volatile
        private var instance: StoryRepository? = null
        fun getInstance(apiService: ApiService) : StoryRepository = instance ?: synchronized(this) {
            instance ?: StoryRepository(apiService) }.also { instance = it }
    }
}