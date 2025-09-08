package com.unicred.data

import com.google.gson.annotations.SerializedName

/**
 * Data class for the response from POST /api/credentials/:id/revoke.
 */
data class RevokeCredentialResponse(
    @SerializedName("revoked")
    val revoked: Boolean,
    @SerializedName("credentialId")
    val credentialId: String
)
