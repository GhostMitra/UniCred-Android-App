package com.unicred.data.remote.responses

    // These imports will be needed if you use the enums directly in mapping,
    // otherwise, the mapper function will handle it.
    // import com.unicred.data.CredentialStatus
    // import com.unicred.data.CredentialType

    data class StudentMetricsResponse(
        val student: StudentDetails,
        val stats: MetricsStats,
        val recentCredentials: List<ApiCredential>
    )

    data class StudentDetails(
        val id: String, // This is the studentId used in the API path, e.g., "cmeyt1zxl000dy5ie2yc1zr93"
        val name: String,
        val email: String,
        val status: String // e.g., "active"
    )

    data class MetricsStats(
        val totalCredentials: Int,
        val verified: Int,
        val pending: Int
    )

    data class ApiCredential(
        val id: String,
        val title: String,
        val type: String, // e.g., "master", "certificate". Needs mapping to CredentialType enum
        val institution: String,
        val dateIssued: String, // ISO 8601 date string
        val status: String, // e.g., "verified", "pending". Needs mapping to CredentialStatus enum
        val studentId: String
        // Optional fields from your API if you want to include them:
        // val vcJwt: String? = null,
        // val vcHash: String? = null,
        // val createdAt: String? = null,
        // val updatedAt: String? = null,
        // val recruiterApproved: Boolean? = false,
        // val studentAccepted: Boolean? = false
    )