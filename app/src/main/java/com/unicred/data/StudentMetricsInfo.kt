package com.unicred.data

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

/**
 * Represents the simplified student information within the student metrics response.
 */
@Parcelize
data class StudentMetricsInfo(
    @SerializedName("id")
    val id: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("status")
    val status: String // e.g., "active"
) : Parcelable
