package com.unicred.data

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

/**
 * Represents the credential statistics for a student within the student metrics response.
 */
@Parcelize
data class StudentStats(
    @SerializedName("totalCredentials")
    val totalCredentials: Int,
    @SerializedName("verified")
    val verified: Int,
    @SerializedName("pending")
    val pending: Int
) : Parcelable
