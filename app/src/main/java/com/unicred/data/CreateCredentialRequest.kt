package com.unicred.data

import com.google.gson.annotations.SerializedName

/**
 * Data class for the request body when issuing a new credential (POST /api/credentials/).
 */
data class CreateCredentialRequest(
    @SerializedName("title")
    val title: String,
    @SerializedName("type")
    val type: String, // API expects string enum: bachelor, master, etc.
    @SerializedName("institution")
    val institution: String,
    @SerializedName("dateIssued")
    val dateIssued: String, // Format like "2024-06-01"
    @SerializedName("studentId")
    val studentId: String? = null,
    @SerializedName("studentName")
    val studentName: String? = null
)
