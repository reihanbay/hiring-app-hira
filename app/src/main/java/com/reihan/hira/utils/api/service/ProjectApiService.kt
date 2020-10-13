package com.reihan.hira.utils.api.service

import com.reihan.hira.utils.api.response.GetProjectsResponse
import com.reihan.hira.utils.api.response.ProjectAddResponse
import com.reihan.hira.utils.api.response.ProjectByIdResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface ProjectApiService {
    @GET("project/?limit=3")
    suspend fun getAllProjectById(@Query("search[idRecruiter]") search : String): GetProjectsResponse

    @GET("project/{id}")
    suspend fun getAllProjectByIdProject(@Path("id") id : String): ProjectByIdResponse

    @Multipart
    @POST("project/")
    suspend fun postProject(
        @Part("nameProject") projectName: RequestBody,
        @Part("description") projectDesc: RequestBody,
        @Part("deadline") projectDeadline: RequestBody,
        @Part("idRecruiter") projectRecruiter: RequestBody,
        @Part image: MultipartBody.Part
    ): ProjectAddResponse

}