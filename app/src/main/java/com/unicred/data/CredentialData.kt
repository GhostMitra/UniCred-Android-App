package com.unicred.data

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

// Request body for POST /credentials/
@Parcelize
data class CreateCredentialRequest(
    val studentId: String,
    val title: String,
    val issuer: String? = null, // Defaults to authenticated user, so nullable
    val metadata: CredentialMetadata? = null // Assuming metadata is optional
) : Parcelable

@Parcelize
data class CredentialMetadata(
    val course: String,
    val score: String
) : Parcelable

// Response for POST /credentials/
@Parcelize
data class CreateCredentialResponse(
    val id: String,
    val status: String, // e.g., "issued". Consider mapping to CredentialStatus enum if appropriate
    val studentId: String
) : Parcelable

// Response for GET /credentials/issuerDid
@Parcelize
data class IssuerDidResponse(
    val did: String
) : Parcelable

// Response for GET /credentials/issuerJwk
@Parcelize
data class IssuerJwkResponse(
    val kty: String, // Key Type
    val crv: String? = null, // Curve
    val x: String? = null,   // X Coordinate
    val y: String? = null    // Y Coordinate
    // Add other relevant JWK fields if needed
) : Parcelable

// Response for POST /credentials/:id/anchor
@Parcelize
data class AnchorCredentialResponse(
    val id: String,
    val anchored: Boolean,
    @SerializedName("txHash")
    val transactionHash: String
) : Parcelable

// Response for POST /credentials/:id/revoke
@Parcelize
data class RevokeCredentialResponse(
    val id: String,
    val revoked: Boolean
) : Parcelable

// Response for POST /credentials/:id/recruiter-approve
@Parcelize
data class RecruiterApproveResponse(
    val id: String,
    val approvedByRecruiter: Boolean
) : Parcelable

// Response for POST /credentials/:id/student-accept
@Parcelize
data class StudentAcceptResponse(
    val id: String,
    val acceptedByStudent: Boolean
) : Parcelable

// Response for GET /credentials/verify/:hash
// This replaces the old `VerificationResult` if you had one.
@Parcelize
data class VerifyCredentialResponse(
    val valid: Boolean,
    val credentialId: String,
    val issuer: String
) : Parcelable

// Note: Your existing `Credential.kt` defines `Credential`, `CredentialType`, and `CredentialStatus`.
// The `Credential` data class seems suitable for representing a full credential object
// returned by GET /credentials/ or GET /credentials/search/:query.
// The fields in the API's search example (`id`, `title`, `studentId`) are a subset
// of your `Credential` data class, which is fine if other fields are nullable.
