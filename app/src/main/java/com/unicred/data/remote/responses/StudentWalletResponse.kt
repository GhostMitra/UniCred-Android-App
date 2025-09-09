package com.unicred.data.remote.responses

import com.google.gson.annotations.SerializedName

/**
 * Represents the overall API response for a student's credentials wallet.
 */
data class StudentWalletApiResponse(
    @SerializedName("student")
    val student: ApiStudentWalletInfo,
    @SerializedName("credentials")
    val credentials: List<ApiCredentialEntry>,
    @SerializedName("count")
    val count: Int
)

/**
 * Represents the student information within the wallet API response.
 */
data class ApiStudentWalletInfo(
    @SerializedName("id")
    val id: String,
    @SerializedName("userId")
    val userId: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("status")
    val status: String,
    @SerializedName("did")
    val did: String? = null, // Based on example, this could be optional
    @SerializedName("walletJwk")
    val walletJwk: ApiWalletJwk? = null // Based on example, this could be optional
)

/**
 * Represents the JSON Web Key (JWK) for the student's wallet.
 */
data class ApiWalletJwk(
    @SerializedName("d")
    val d: String? = null,
    @SerializedName("x")
    val x: String? = null,
    @SerializedName("crv")
    val crv: String? = null,
    @SerializedName("kty")
    val kty: String? = null
)

/**
 * Represents a single credential entry from the wallet API response.
 * This includes all fields from the API.
 */
data class ApiCredentialEntry(
    @SerializedName("id")
    val id: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("type")
    val type: String,
    @SerializedName("institution")
    val institution: String,
    @SerializedName("dateIssued")
    val dateIssued: String, // ISO String format
    @SerializedName("status")
    val status: String,
    @SerializedName("studentId")
    val studentId: String,
    @SerializedName("vcJwt")
    val vcJwt: String? = null,
    @SerializedName("vcHash")
    val vcHash: String? = null,
    @SerializedName("createdAt")
    val createdAt: String,
    @SerializedName("updatedAt")
    val updatedAt: String,
    @SerializedName("recruiterApproved")
    val recruiterApproved: Boolean,
    @SerializedName("studentAccepted")
    val studentAccepted: Boolean
)
