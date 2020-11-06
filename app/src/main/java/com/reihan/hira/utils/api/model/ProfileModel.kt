package com.reihan.hira.utils.api.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ProfileModel (
    val idRecruiter: String,
    val name: String,
    val company: String,
    val position: String,
    val sector: String,
    val city: String,
    val description: String,
    val image: String,
    val instagram: String,
    val linkedin: String,
    val web: String,
    val idAccount: String
) : Parcelable