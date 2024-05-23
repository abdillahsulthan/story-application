package com.dicoding.storyapp.ui.main

import androidx.lifecycle.LiveData

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.dicoding.storyapp.data.remote.response.Story
import com.dicoding.storyapp.data.repository.StoryRepository

class MainViewModel(private val storyRepository: StoryRepository) : ViewModel() {
    fun getStories(): LiveData<PagingData<Story>> {
        return storyRepository.getAllStories().cachedIn(viewModelScope)
    }
}