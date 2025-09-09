package com.unicred.data.mappers

import com.unicred.data.Credential // For mapping recent credentials from MetricsOverviewResponse
import com.unicred.data.DashboardCredentialSummary // UI model for recent credentials summary
import com.unicred.data.DashboardMetrics // UI model for the whole dashboard
import com.unicred.data.MetricsOverviewResponse // API response for university dashboard metrics

/**
 * Maps a [Credential] object (often from an API response like [MetricsOverviewResponse])
 * to a [DashboardCredentialSummary] object suitable for UI display on a dashboard.
 */
fun Credential.toDomainDashboardCredentialSummary(): DashboardCredentialSummary {
    return DashboardCredentialSummary(
        id = this.id,
        title = this.title,
        institution = this.institution,
        studentId = this.studentId,
        studentName = this.studentName,
        status = this.status?.name?.lowercase() ?: "unknown", // Convert CredentialStatus enum to a lowercase string
        dateIssued = this.dateIssued // Assumes dateIssued is already in a suitable string format or UI will format it
    )
}

/**
 * Maps a [MetricsOverviewResponse] (from the API)
 * to a [DashboardMetrics] object suitable for the University Dashboard UI.
 */
fun MetricsOverviewResponse.toDomainDashboardMetrics(): DashboardMetrics {
    return DashboardMetrics(
        totalStudents = this.totalStudents,
        totalCredentials = this.totalCredentials,
        verifiedCount = this.verifiedCount,
        pendingCount = this.pendingCount,
        recentCredentials = this.recentCredentials.map { it.toDomainDashboardCredentialSummary() } // Use the mapper above
    )
}
