package com.reihan.hira.utils.api.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ProjectModel (
    val idProject: String,
    val image: String,
    val name: String,
    val deadline: String,
    val description : String,
) : Parcelable