package com.reihan.hira.utils.api.service

import com.reihan.hira.utils.api.response.ProfileResponse
import retrofit2.http.*

interface ProfileApiService {
    @FormUrlEncoded
    @POST("recruiter")
    suspend fun createProfile(
        @Field("nameRecruiter") name: String?,
        @Field("nameCompany") company: String?,
        @Field("position") position: String?,
        @Field("sectorCompany") sector: String?,
        @Field("city") city: String?,
        @Field("description") description: String?,
        @Field("image") image: String?,
        @Field("instagram") instagram: String?,
        @Field("linkedin") linkedin: String?,
        @Field("website") web: String?,
        @Field("idAccount") id: String?,
    ): ProfileResponse

    @GET("recruiter/{id}")
    suspend fun getProfileById(@Path("id") id: Int?): ProfileResponse
}