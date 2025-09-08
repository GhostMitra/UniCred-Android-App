package com.unicred.data

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class DashboardMetrics(
    @SerializedName("totalStudents")
    val totalStudents: Int,
    @SerializedName("totalCredentials")
    val totalCredentials: Int,
    @SerializedName("verifiedCount")
    val verifiedCount: Int,
    @SerializedName("pendingCount")
    val pendingCount: Int,
    @SerializedName("recentCredentials")
    val recentCredentials: List<DashboardCredentialSummary>,
    // We'll omit recentEvents for now for the Recruiter Dashboard simplicity
    // @SerializedName("recentEvents")
    // val recentEvents: List<DashboardLedgerEvent>
) : Parcelable

@Parcelize
data class DashboardCredentialSummary(
    @SerializedName("id")
    val id: String, // cred_xxx
    @SerializedName("title")
    val title: String, // e.g., "Bachelor of Computer Science"
    @SerializedName("institution")
    val institution: String, // From the main credential model
    @SerializedName("studentId")
    val studentId: String?, // stu_xxx
    // studentName is not directly in API metric for recentCredentials,
    // but useful. Assuming it might be fetched/resolved separately or added for mock.
    val studentName: String? = null,
    @SerializedName("status")
    val status: String, // "verified", "pending"
    @SerializedName("dateIssued")
    val dateIssued: String // "YYYY-MM-DDTHH:mm:ss.sssZ"
) : Parcelable

// If we were to use recentEvents:
// @Parcelize
// data class DashboardLedgerEvent(
//     val type: String, // e.g., "ANCHORED", "REVOKED"
//     val credentialId: String,
//     val timestamp: String
// ) : Parcelable