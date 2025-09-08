package com.unicred.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
enum class StudentStatus : Parcelable {
    ACTIVE,
    GRADUATED,
    INACTIVE,
    UNKNOWN // Adding an UNKNOWN case for completeness
}
