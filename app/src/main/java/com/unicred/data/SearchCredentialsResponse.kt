package com.unicred.data

import com.google.gson.annotations.SerializedName

/**
 * Data class for the response from GET /api/credentials/search/:query.
 */
data class SearchCredentialsResponse(
    @SerializedName("results")
    val results: List<Credential>
)
