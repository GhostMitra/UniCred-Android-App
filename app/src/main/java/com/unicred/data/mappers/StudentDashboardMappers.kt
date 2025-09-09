package com.unicred.data.mappers

import com.unicred.data.Credential
import com.unicred.data.CredentialStatus
import com.unicred.data.CredentialType
import com.unicred.data.User
import com.unicred.data.remote.responses.ApiCredential
import com.unicred.data.remote.responses.StudentDetails
import com.unicred.data.remote.responses.StudentMetricsResponse
import com.unicred.ui.viewmodel.StudentDashboardUiState

fun ApiCredential.toDomainCredential(studentFullName: String): Credential {
    return Credential(
        id = this.id,
        title = this.title,
        type = mapCredentialType(this.type),
        institution = this.institution,
        dateIssued = this.dateIssued.substringBefore("T"), // Assuming you want to display only the date part
        status = mapCredentialStatus(this.status),
        studentId = this.studentId,
        studentName = studentFullName // Use the student's full name from the StudentDetails
    )
}

fun mapCredentialType(apiType: String): CredentialType {
    return when (apiType.lowercase()) {
        "bachelor" -> CredentialType.BACHELOR
        "master" -> CredentialType.MASTER
        "phd" -> CredentialType.PHD
        "diploma" -> CredentialType.DIPLOMA
        "certificate" -> CredentialType.CERTIFICATE
        "degree" -> CredentialType.DEGREE
        "honors" -> CredentialType.HONORS
        "microcredential" -> CredentialType.MICROCREDENTIAL
        "skill_badge" -> CredentialType.SKILL_BADGE
        "professional_license" -> CredentialType.PROFESSIONAL_LICENSE
        "other" -> CredentialType.OTHER
        else -> CredentialType.UNKNOWN
    }
}

fun mapCredentialStatus(apiStatus: String): CredentialStatus {
    return when (apiStatus.lowercase()) {
        "verified" -> CredentialStatus.VERIFIED
        "pending" -> CredentialStatus.PENDING
        "expired" -> CredentialStatus.EXPIRED
        "revoked" -> CredentialStatus.REVOKED
        "anchored" -> CredentialStatus.ANCHORED
        "accepted" -> CredentialStatus.ACCEPTED
        else -> CredentialStatus.UNKNOWN
    }
}

// Maps the StudentDetails from the API to your app's User domain model
// The User from login (AuthViewModel) is the source of truth for username, full accessType etc.
// This mapper focuses on creating a User object for display within the StudentDashboardUiState
// using information from the /metrics API.
fun StudentDetails.toDomainUserForDashboard(loggedInUser: User?): User {
    return User(
        id = loggedInUser?.id ?: this.id, // Prefer ID from logged-in user if available, fallback to metrics studentId
        username = loggedInUser?.username ?: "", // Username comes from login
        accessType = loggedInUser?.accessType ?: "student", // AccessType from login
        email = this.email, // Email from metrics
        fullName = this.name, // FullName from metrics
        studentId = this.id // This is the crucial ID, e.g., "cmeyt1zxl000dy5ie2yc1zr93"
        // other fields like tokens, etc., would come from the loggedInUser session
    )
}

fun StudentMetricsResponse.toStudentDashboardUiState(loggedInUser: User?): StudentDashboardUiState {
    val mappedUser = this.student.toDomainUserForDashboard(loggedInUser)
    val mappedCredentials = this.recentCredentials.map { 
        it.toDomainCredential(studentFullName = this.student.name) 
    }
    return StudentDashboardUiState(
        user = mappedUser,
        credentials = mappedCredentials,
        totalCredentialsCount = this.stats.totalCredentials,
        verifiedCredentialsCount = this.stats.verified,
        pendingCredentialsCount = this.stats.pending,
        isLoading = false,
        errorMessage = null
    )
}
