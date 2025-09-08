package com.unicred.data

import com.google.gson.annotations.SerializedName

/**
 * A generic data class for API responses that only contain a message,
 * typically for success confirmations of operations like DELETE or some POSTs.
 */
data class SuccessMessage(
    @SerializedName("message")
    val message: String
)
