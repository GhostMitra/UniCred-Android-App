package com.unicred.data

import com.google.gson.annotations.SerializedName

/**
 * Data class for the response from GET /api/credentials/verify/:hash.
 */
data class VerifyCredentialResponse(
    @SerializedName("exists")
    val exists: Boolean,
    @SerializedName("method")
    val method: String? = null, // Nullable if not present when exists is false (though API shows 404 for that)
    @SerializedName("credentialId")
    val credentialId: String? = null // Nullable if not present when exists is false
)
