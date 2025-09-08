package com.unicred.data

import com.google.gson.annotations.SerializedName

/**
 * Data class for the health check API response.
 */
data class HealthResponse(
    @SerializedName("ok")
    val ok: Boolean
)
