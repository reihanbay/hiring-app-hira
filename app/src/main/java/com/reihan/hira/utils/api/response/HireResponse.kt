package com.reihan.hira.utils.api.response

import com.google.gson.annotations.SerializedName

data class HireResponse(val success: Boolean?, val message: String?, val data: DataResult?) {
    data class DataResult(
        @SerializedName("projectJob") val projectJob: String?,
        @SerializedName("message") val message: String?,
        @SerializedName("statusConfirm") val statusConfirm: Int?,
        @SerializedName("dateConfirm") val dateConfirm: String?,
        @SerializedName("price") val price: Int?,
        @SerializedName("idWorker") val idWorker: Int?,
        @SerializedName("idProject") val idProject: Int?
    )
}