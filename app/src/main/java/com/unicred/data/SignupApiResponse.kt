package com.unicred.data

import com.google.gson.annotations.SerializedName

/**
 * Data class for the response from the (optional) signup API (POST /signup).
 */
data class SignupApiResponse(
    @SerializedName("message")
    val message: String,
    @SerializedName("user")
    val user: User // Using the updated User.kt data class
)
