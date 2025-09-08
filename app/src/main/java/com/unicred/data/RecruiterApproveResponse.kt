package com.unicred.data

import com.google.gson.annotations.SerializedName

/**
 * Data class for the response from POST /api/credentials/:id/recruiter-approve.
 */
data class RecruiterApproveResponse(
    @SerializedName("ok")
    val ok: Boolean,
    @SerializedName("credentialId")
    val credentialId: String,
    @SerializedName("recruiterApproved")
    val recruiterApproved: Boolean
)
