package com.unicred.data

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

/**
 * Represents a user in the system.
 * This data class is used for user information obtained after login and for displaying user details.
 * Note: The API response for user login provides `id`, `username`, `accessType`, `email`, `fullName`,
 * and `studentId` (if the user is a student).
 */
@Parcelize
data class User(
    @SerializedName("id")
    val id: String,
    @SerializedName("username")
    val username: String,
    // This is the raw string value from the API, used for deserialization.
    @SerializedName("accessType")
    val accessType: String,
    @SerializedName("email")
    val email: String? = null, // Email might be optional or not available in all contexts
    @SerializedName("fullName")
    val fullName: String? = null, // FullName might be optional
    @SerializedName("studentId")
    val studentId: String? = null, // studentId is specific to students and nullable for others
    
    // Fields that might be part of a user session but not directly from login user details API
    val authToken: String? = null,
    val refreshToken: String? = null,

    // Client-side field to hold the enum representation of the access type.
    // This field is not directly deserialized from JSON by this name.
    // It will be populated in the AuthRepository.
    var role: AccessType? = null

) : Parcelable
