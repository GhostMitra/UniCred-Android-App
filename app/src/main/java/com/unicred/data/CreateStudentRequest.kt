package com.unicred.data

import com.google.gson.annotations.SerializedName

/**
 * Data class for the request body when creating a new student (POST /api/students/).
 * Based on the new, complete API documentation.
 */
data class CreateStudentRequest(
    @SerializedName("username")
    val username: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("fullName")
    val fullName: String
)
