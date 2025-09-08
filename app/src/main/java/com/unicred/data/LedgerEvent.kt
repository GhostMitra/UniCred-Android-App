package com.unicred.data

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

/**
 * Represents a ledger event (structure is a placeholder based on common needs).
 * Used in the response for GET /api/metrics/.
 */
@Parcelize
data class LedgerEvent(
    @SerializedName("id")
    val id: String? = null, // Optional, if events have IDs
    @SerializedName("type")
    val type: String, // Type of event, e.g., "CREDENTIAL_ISSUED", "CREDENTIAL_ANCHORED"
    @SerializedName("timestamp")
    val timestamp: String, // ISO date-time string
    @SerializedName("description")
    val description: String? = null, // A human-readable description
    @SerializedName("details")
    val details: Map<String, Any>? = null // For any other event-specific data
) : Parcelable
