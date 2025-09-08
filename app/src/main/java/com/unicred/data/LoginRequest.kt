package com.unicred.data

import com.google.gson.annotations.SerializedName

/**
 * Data class for the login request body.
 * API expects: id (username), password, accessType (as String)
 */
data class LoginRequest(
    @SerializedName("id")
    val id: String, // This is the username/login identifier, e.g., "STU001"
    @SerializedName("password")
    val password: String,
    @SerializedName("accessType")
    val accessType: String // e.g., "student", "recruiter", "university"
)
