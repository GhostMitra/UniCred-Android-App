package com.unicred.data

import com.google.gson.annotations.SerializedName

/**
 * Data class for the response from POST /api/credentials/:id/anchor.
 */
data class AnchorCredentialResponse(
    @SerializedName("anchored")
    val anchored: Boolean,
    @SerializedName("block")
    val block: BlockDetails
)

/**
 * Details of the block in which a credential was anchored.
 */
data class BlockDetails(
    @SerializedName("height")
    val height: Long, // Using Long for block height as it can grow
    @SerializedName("hash")
    val hash: String,
    @SerializedName("previousHash")
    val previousHash: String
)
