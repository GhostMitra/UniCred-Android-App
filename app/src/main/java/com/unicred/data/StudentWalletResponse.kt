package com.unicred.data

import com.google.gson.annotations.SerializedName

/**
 * Data class for the response from GET /api/students/:id/wallet.
 */
data class StudentWalletResponse(
    @SerializedName("did")
    val did: String,
    @SerializedName("walletJwk")
    val walletJwk: WalletJwk // Using the WalletJwk.kt data class
)
