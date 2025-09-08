package com.unicred.data

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

/**
 * Represents the JWK (JSON Web Key) structure for a student's wallet.
 */
@Parcelize
data class WalletJwk(
    @SerializedName("kty")
    val kty: String,
    @SerializedName("crv")
    val crv: String,
    @SerializedName("x")
    val x: String
) : Parcelable
