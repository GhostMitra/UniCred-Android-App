package com.unicred.data

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

/**
 * Represents a user in the system.
 * Based on API response for POST /api/auth/login.
 */
@Parcelize
data class User(
    @SerializedName("id")
    val id: String, // User's unique ID (e.g., "usr_xxx")
    @SerializedName("username")
    val username: String,
    @SerializedName("email")
    val email: String? = null, // Email can be optional based on some User contexts
    @SerializedName("fullName")
    val fullName: String? = null,
    @SerializedName("studentId")
    val studentId: String? = null, // Specific to students (e.g., "stu_xxx")

    // Client-side field to hold the role determined at login/signup
    // Not directly from user object in /auth/login response, but useful for app logic
    var role: AccessType? = null
) : Parcelable
