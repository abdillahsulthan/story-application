package com.dicoding.storyapp.data.preference

data class User (
    val email: String,
    val name: String,
    val userId: String,
    val token: String,
    val isLogin: Boolean = false
)