package com.unicred.data

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

/**
 * Represents a credential record based on API documentation (GET /api/credentials/).
 */
@Parcelize
data class Credential(
    @SerializedName("id")
    val id: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("type")
    val type: CredentialType, // Changed from String
    @SerializedName("institution")
    val institution: String,
    @SerializedName("dateIssued")
    val dateIssued: String, // ISO String format
    @SerializedName("status")
    val status: CredentialStatus, // Changed from String
    @SerializedName("studentId")
    val studentId: String? = null, // studentId can be optional in some contexts
    @SerializedName("vcJwt")
    val vcJwt: String? = null,
    @SerializedName("vcHash") // JSON key from API
    val blockchainHash: String? = null, // Kotlin property name changed from vcHash
    @SerializedName("recruiterApproved")
    val recruiterApproved: Boolean? = null,
    @SerializedName("studentAccepted")
    val studentAccepted: Boolean? = null,
    @SerializedName("createdAt")
    val createdAt: String? = null, // ISO String format
    @SerializedName("updatedAt")
    val updatedAt: String? = null, // ISO String format

    // Retaining studentName from previous version for client-side convenience if needed,
    // though not directly in GET /api/credentials/ list response per field list.
    // POST /api/credentials request does have studentName.
    @SerializedName("studentName")
    val studentName: String? = null
) : Parcelable

// Enums for client-side categorization/logic. Values should align with API string enums.
@Parcelize
enum class CredentialType : Parcelable {
    BACHELOR, MASTER, CERTIFICATE, DIPLOMA, UNKNOWN
}

@Parcelize
enum class CredentialStatus : Parcelable {
    VERIFIED, PENDING, EXPIRED, REVOKED, ANCHORED, ACCEPTED, UNKNOWN
}
