package com.unicred.data

import com.google.gson.annotations.SerializedName

/**
 * Data class for the response from POST /api/credentials/:id/student-accept.
 */
data class StudentAcceptResponse(
    @SerializedName("ok")
    val ok: Boolean,
    @SerializedName("credentialId")
    val credentialId: String,
    @SerializedName("studentAccepted")
    val studentAccepted: Boolean
)
