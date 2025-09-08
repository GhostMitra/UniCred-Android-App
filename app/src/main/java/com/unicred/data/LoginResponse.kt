package com.unicred.data

import com.google.gson.annotations.SerializedName

/**
 * Data class for the login response.
 * Based on API response for POST /api/auth/login.
 */
data class LoginResponse(
    @SerializedName("token")
    val token: String,
    @SerializedName("user")
    val user: User
)
