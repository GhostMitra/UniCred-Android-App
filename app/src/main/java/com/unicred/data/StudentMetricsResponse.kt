package com.unicred.data

import com.google.gson.annotations.SerializedName

/**
 * Data class for the response from GET /api/metrics/student/:id.
 */
data class StudentMetricsResponse(
    @SerializedName("student")
    val student: StudentMetricsInfo,
    @SerializedName("stats")
    val stats: StudentStats,
    @SerializedName("recentCredentials")
    val recentCredentials: List<Credential> // Uses Credential.kt
)
