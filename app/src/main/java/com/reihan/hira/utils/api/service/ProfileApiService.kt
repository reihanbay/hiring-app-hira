package com.reihan.hira.utils.api.service

import com.reihan.hira.utils.api.response.CheckProfileResponse
import com.reihan.hira.utils.api.response.ProfileResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface ProfileApiService {
    @Multipart
    @POST("recruiter")
    suspend fun createProfile(
        @Part("nameRecruiter") name: RequestBody,
        @Part("nameCompany") company: RequestBody,
        @Part("position") position: RequestBody,
        @Part("sectorCompany") sector: RequestBody,
        @Part("city") city: RequestBody,
        @Part("description") description: RequestBody,
        @Part image: MultipartBody.Part,
        @Part("instagram") instagram: RequestBody,
        @Part("linkedin") linkedin: RequestBody,
        @Part("website") web: RequestBody,
        @Part("idAccount") idAccount: RequestBody,
    ): ProfileResponse

    @GET("recruiter/{id}")
    suspend fun getProfileById(@Path("id") id: Int?): ProfileResponse

    @GET("recruiter/{id}")
    suspend fun checkProfileById(@Path("id") id: Int?): CheckProfileResponse
}