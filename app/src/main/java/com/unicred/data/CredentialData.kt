package com.unicred.data

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

// Response for POST /credentials/
@Parcelize
data class CreateCredentialResponse(
    @SerializedName("id") // Assuming API returns 'id'
    val id: String,
    @SerializedName("status") // Assuming API returns 'status'
    val status: String, // e.g., "issued". Consider mapping to CredentialStatus enum if appropriate
    @SerializedName("studentId") // Assuming API returns 'studentId'
    val studentId: String
) : Parcelable

// Response for GET /credentials/issuerDid
@Parcelize
data class IssuerDidResponse(
    @SerializedName("did") // Assuming API returns 'did'
    val did: String
) : Parcelable

// Response for GET /credentials/issuerJwk
@Parcelize
data class IssuerJwkResponse(
    @SerializedName("kty") // Assuming API returns 'kty'
    val kty: String, // Key Type
    @SerializedName("crv") // Assuming API returns 'crv'
    val crv: String? = null, // Curve
    @SerializedName("x") // Assuming API returns 'x'
    val x: String? = null,   // X Coordinate
    @SerializedName("y") // Assuming API returns 'y'
    val y: String? = null    // Y Coordinate
    // Add other relevant JWK fields if needed
) : Parcelable

// Note: Your existing `Credential.kt` defines `Credential`, `CredentialType`, and `CredentialStatus`.
// The `Credential` data class seems suitable for representing a full credential object
// returned by GET /credentials/ or GET /credentials/search/:query.
// The fields in the API\'s search example (`id`, `title`, `studentId`) are a subset
// of your `Credential` data class, which is fine if other fields are nullable.
