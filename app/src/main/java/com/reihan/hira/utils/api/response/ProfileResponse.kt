package com.reihan.hira.utils.api.response

import com.google.gson.annotations.SerializedName

data class ProfileResponse(val success: Boolean?, val message: String?, val data: Profile) {
    data class Profile(
        @SerializedName("idRecruiter") val idRecruiter: String?,
        @SerializedName("nameRecruiter") val name: String?,
        @SerializedName("nameCompany") val company: String?,
        @SerializedName("position") val position: String?,
        @SerializedName("sectorCompany") val sector: String?,
        @SerializedName("city") val city: String?,
        @SerializedName("description") val description: String?,
        @SerializedName("image") val image: String?,
        @SerializedName("instagram") val instagram: String?,
        @SerializedName("linkedin") val linkedin: String?,
        @SerializedName("website") val web: String?,
        @SerializedName("idAccount") val idAccount: String?,
        @SerializedName("createdAt") val created: String?,
        @SerializedName("updatedAt") val updated: String?
    )
}