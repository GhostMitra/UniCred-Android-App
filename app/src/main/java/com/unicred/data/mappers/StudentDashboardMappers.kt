package com.unicred.data.mappers

import com.unicred.data.Credential
import com.unicred.data.CredentialStatus
import com.unicred.data.CredentialType
import com.unicred.data.User
import com.unicred.data.remote.responses.ApiCredential // Existing import
import com.unicred.data.remote.responses.ApiCredentialEntry // New import for wallet credentials
import com.unicred.data.StudentMetricsInfo
import com.unicred.data.StudentMetricsResponse
import com.unicred.ui.viewmodel.StudentDashboardUiState

// Existing mapper for a general ApiCredential (if still used elsewhere)
fun ApiCredential.toDomainCredential(studentFullName: String): Credential {
    return Credential(
        id = this.id,
        title = this.title,
        type = mapCredentialType(this.type),
        institution = this.institution,
        dateIssued = this.dateIssued.substringBefore("T"),
        status = mapCredentialStatus(this.status),
        studentId = this.studentId,
        studentName = studentFullName
        // Note: This mapper doesn't include fields like vcJwt, blockchainHash, etc.
        // If ApiCredential is similar to ApiCredentialEntry, consider consolidating or enhancing this.
    )
}

/**
 * Maps an ApiCredentialEntry from the student wallet API to a domain Credential object.
 */
fun ApiCredentialEntry.toDomainCredential(studentFullName: String?): Credential {
    return Credential(
        id = this.id,
        title = this.title,
        type = mapCredentialType(this.type),
        institution = this.institution,
        dateIssued = this.dateIssued.substringBefore("T"), // Assuming date-only display
        status = mapCredentialStatus(this.status), // Credential.status is nullable
        studentId = this.studentId,
        vcJwt = this.vcJwt,
        blockchainHash = this.vcHash, // Mapping vcHash to blockchainHash
        recruiterApproved = this.recruiterApproved,
        studentAccepted = this.studentAccepted,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt,
        studentName = studentFullName
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

fun StudentMetricsInfo.toDomainUserForDashboard(loggedInUser: User?): User {
    return User(
        id = loggedInUser?.id ?: this.id,
        username = loggedInUser?.username ?: "",
        accessType = loggedInUser?.accessType ?: "student",
        email = this.email,
        fullName = this.name,
        studentId = this.id
    )
}

fun StudentMetricsResponse.toStudentDashboardUiState(loggedInUser: User?): StudentDashboardUiState {
    val mappedUser = this.student.toDomainUserForDashboard(loggedInUser)
    val mappedCredentials = this.recentCredentials
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
