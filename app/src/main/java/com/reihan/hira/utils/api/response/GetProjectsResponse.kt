package com.reihan.hira.utils.api.response

import com.google.gson.annotations.SerializedName

data class GetProjectsResponse(
    val success: Boolean?,
    val message: String?,
    val data: List<project>
) {
    data class project(
        @SerializedName("idProject") val idProject: String?,
        @SerializedName("image") val image: String?,
        @SerializedName("nameProject") val name: String?,
        @SerializedName("description") val description: String?,
        @SerializedName("deadline") val deadline: String?,
        @SerializedName("idRecruiter") val idRecruiter: Int?,
        @SerializedName("createdAt") val created: String?,
        @SerializedName("updatedAt") val updated: String?
    )
}