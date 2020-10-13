package com.reihan.hira.utils.api.service

import com.reihan.hira.utils.api.response.LoginResponse
import com.reihan.hira.utils.api.response.RegisterResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface AccountApiService {
    @FormUrlEncoded
    @POST("user/login")
    suspend fun loginRequest(
        @Field("email") email: String?,
        @Field("password") password: String?
    ): LoginResponse

    @FormUrlEncoded
    @POST("user/register")
    suspend fun registerRequest(
        @Field("name") name: String?,
        @Field("email") email: String?,
        @Field("password") password: String?,
        @Field("noHp") noHp: String?,
        @Field("role") role: String?,
        @Field("status") status: Int?,
    ): RegisterResponse
}