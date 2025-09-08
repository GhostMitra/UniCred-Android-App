package com.unicred.data

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

/**
 * Represents a student, based on API documentation for /api/students/ endpoints.
 */
@Parcelize
data class Student(
    @SerializedName("id")
    val id: String, // Student's unique ID (e.g., "stu_xxx")
    @SerializedName("userId")
    val userId: String? = null, // Associated user account ID (e.g., "usr_xxx"), present in create response
    @SerializedName("name")
    val name: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("status")
    val status: StudentStatus? = null, // To represent student status (e.g., ACTIVE, GRADUATED)
    @SerializedName("did")
    val did: String? = null,
    @SerializedName("walletJwk")
    val walletJwk: WalletJwk? = null,
    @SerializedName("credentials")
    val credentials: List<Credential>? = null, // List of credential summaries

    // Fields from StudentDirectory mock data that might be relevant for the UI model
    // These are not explicitly in the /api/students list response but might come from other endpoints or UI needs.
    val studentId: String? = null, // Often the same as 'username' or a specific student system ID
    val program: String? = null,
    val enrollmentDate: String? = null,
    val gpa: Double? = null,
    val creditsCompleted: Int? = null,
    val totalCredits: Int? = null,
    val advisor: String? = null,
    val phone: String? = null,
    val address: String? = null
) : Parcelable

// The StudentStatus enum is defined in StudentStatus.kt
// enum class StudentStatus { ACTIVE, GRADUATED, INACTIVE, UNKNOWN }
