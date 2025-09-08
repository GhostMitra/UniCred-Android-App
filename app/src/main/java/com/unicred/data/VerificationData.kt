package com.unicred.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class VerifiedCredentialSummary(
    val title: String,
    val studentName: String,
    val studentId: String,
    val verifiedDate: String, // e.g., "2024-01-15"
    val status: VerificationStatus // "Verified", "Pending"
) : Parcelable

enum class VerificationStatus {
    VERIFIED, PENDING, FAILED
}

@Parcelize
data class CredentialVerificationResult(
    // Candidate Information
    val candidateName: String,
    val candidateId: String, // e.g., STU001
    val candidateEmail: String,
    // Credential Details
    val credentialTitle: String,
    val credentialType: String, // e.g., "bachelor"
    val credentialInstitution: String,
    val credentialDateIssued: String, // e.g., "2023-05-15"
    val credentialStatus: String, // e.g., "verified"
    // Verification Details
    val verificationStatusDisplay: String, // e.g., "Authentic"
    val verificationConfidence: String, // e.g., "99%"
    val blockchainHash: String, // e.g., "0x1234567890abcdef"
    val recruiterApproved: Boolean = false
) : Parcelable