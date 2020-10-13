package com.reihan.hira.utils.api.response

import com.google.gson.annotations.SerializedName

data class WorkerByIdResponse(val success: Boolean?, val message: String?, val data: Worker) {
    data class Worker(
        @SerializedName("idWorker") val idWorker: String?,
        @SerializedName("image") val image: String?,
        @SerializedName("nameWorker") val name: String?,
        @SerializedName("jobTitle") val title: String?,
        @SerializedName("statusJob") val statusJob: String?,
        @SerializedName("city") val city: String?,
        @SerializedName("workPlace") val workPlace: String?,
        @SerializedName("description") val description: String?,
        @SerializedName("skill") val skill: String?,
        @SerializedName("idAccount") val idAccount: String?,

        )
}