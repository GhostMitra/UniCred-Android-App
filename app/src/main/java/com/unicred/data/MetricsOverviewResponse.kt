package com.unicred.data

import com.google.gson.annotations.SerializedName

/**
 * Data class for the response from GET /api/metrics/ (dashboard metrics).
 */
data class MetricsOverviewResponse(
    @SerializedName("totalStudents")
    val totalStudents: Int,
    @SerializedName("totalCredentials")
    val totalCredentials: Int,
    @SerializedName("verifiedCount")
    val verifiedCount: Int,
    @SerializedName("pendingCount")
    val pendingCount: Int,
    @SerializedName("recentCredentials")
    val recentCredentials: List<Credential>, // Uses Credential.kt
    @SerializedName("recentEvents")
    val recentEvents: List<LedgerEvent> // Uses LedgerEvent.kt
)
