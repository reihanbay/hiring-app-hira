package com.reihan.hira.utils.api.response

import com.google.gson.annotations.SerializedName

data class ProjectAddResponse(val success: String?, val message: String?, val data: Project) {

    data class Project(
        @SerializedName("nameProject") val nameProject: String?,
        @SerializedName("description") val description: String?,
        @SerializedName("deadline") val deadline: String?,
        @SerializedName("idRecruiter") val idRecruiter: String?,
        val image: String?
    )
}