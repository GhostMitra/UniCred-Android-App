package com.unicred.data

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

/**
 * Data class for the signup request body.
 * API expects: username, email, password, accessType (as String)
 */
@Parcelize
data class SignupData(
    @SerializedName("username")
    val username: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("password")
    val password: String,
    @SerializedName("accessType") // Changed from "role" to "accessType" to match API
    val accessType: String // e.g., "student", "recruiter"
) : Parcelable
