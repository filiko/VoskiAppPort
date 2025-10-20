package com.voskiapp.data.models

data class User(
    val id: String,
    val email: String,
    val name: String,
    val phone: String?,
    val profileImageUrl: String?,
    val isVerified: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)

data class UserPreferences(
    val rememberMe: Boolean = false,
    val biometricEnabled: Boolean = false,
    val notificationsEnabled: Boolean = true,
    val darkModeEnabled: Boolean = false
)



