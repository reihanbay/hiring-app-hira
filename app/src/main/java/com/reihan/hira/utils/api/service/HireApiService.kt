package com.reihan.hira.utils.api.service

import com.reihan.hira.utils.api.response.HireResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface HireApiService {
    @FormUrlEncoded
    @POST("hire ")
    suspend fun postHire(
        @Field("projectJob") projectJob: String?,
        @Field("message") message: String?,
        @Field("statusConfirm") statusConfirm: Int?,
        @Field("dateConfirm") dateConfirm: String?,
        @Field("price") price: Int?,
        @Field("idWorker") idWorker: Int?,
        @Field("idProject") idProject: Int?
    ): HireResponse
}