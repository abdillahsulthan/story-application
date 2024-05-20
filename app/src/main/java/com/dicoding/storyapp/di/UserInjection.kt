package com.dicoding.storyapp.di

import android.content.Context
import com.dicoding.storyapp.data.repository.UserRepository
import com.dicoding.storyapp.data.preference.UserPreference
import com.dicoding.storyapp.data.preference.dataStore

object UserInjection {
    fun provideRepository(context: Context): UserRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        return UserRepository.getInstance(pref)
    }
}