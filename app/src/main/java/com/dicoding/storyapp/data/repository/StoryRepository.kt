package com.dicoding.storyapp.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.dicoding.storyapp.data.StoryRemoteMediator
import com.dicoding.storyapp.data.database.StoryDatabase
import com.dicoding.storyapp.data.remote.response.Story
import com.dicoding.storyapp.data.remote.retrofit.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class StoryRepository (
    private val storyDatabase: StoryDatabase,
    private val apiService: ApiService,
    private val token: String
) {

    fun getAllStories(): LiveData<PagingData<Story>> {
        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            remoteMediator = StoryRemoteMediator("Bearer $token", storyDatabase, apiService),
            pagingSourceFactory = {
                storyDatabase.storyDao().getAllStories()
            }
        ).liveData
    }

    suspend fun getStoriesWithLocation(): List<Story> {
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
        fun getInstance(storyDatabase: StoryDatabase, apiService: ApiService, token: String) : StoryRepository = instance ?: synchronized(this) {
            instance ?: StoryRepository(storyDatabase, apiService, token) }.also { instance = it }
    }
}