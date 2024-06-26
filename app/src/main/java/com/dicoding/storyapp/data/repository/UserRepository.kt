package com.dicoding.storyapp.data.repository

import com.dicoding.storyapp.data.preference.User
import com.dicoding.storyapp.data.preference.UserPreference
import kotlinx.coroutines.flow.Flow

class UserRepository private constructor(
    private val userPreference: UserPreference
) {

    suspend fun saveSession(user: User) {
        userPreference.saveSession(user)
    }

    fun getSession(): Flow<User> {
        return userPreference.getSession()
    }

    suspend fun logout() {
        userPreference.logout()
    }

    companion object {
        @Volatile
        private var instance: UserRepository? = null
        fun getInstance(userPreference: UserPreference): UserRepository = instance ?: synchronized(this) {
            instance ?: UserRepository(userPreference) }.also { instance = it }
    }
}