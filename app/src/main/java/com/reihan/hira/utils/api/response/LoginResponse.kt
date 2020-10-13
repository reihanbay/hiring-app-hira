package com.reihan.hira.utils.api.response

import com.google.gson.annotations.SerializedName

data class LoginResponse(val success: Boolean?, val message: String?, val data: DataResult?) {
    data class DataResult(
        @SerializedName("idAccount") val id: String?,
        @SerializedName("email") val email: String?,
        @SerializedName("name") val name: String?,
        @SerializedName("password") val password: String?,
        @SerializedName("status") val status: String?,
        @SerializedName("role") val role: String?,
        @SerializedName("token") val token: String?

    )
}