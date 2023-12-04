package com.example.jobsterific.pref

data class UserModel(
    val token: String,
    val userId: String,
    val firstName: String,
    val lastName: String,
    val email: String,
    val phone: Int,
    val profile: String,
    val job: String,
    val adress: String,
    val website: String,
    val description: String,
    val isLogin: Boolean = false,
    val isAdmin: Boolean = false,
    val isUser: Boolean = false,
    val isCostumer: Boolean = false
)