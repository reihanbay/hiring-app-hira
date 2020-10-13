package com.reihan.hira.utils.api.service

import com.reihan.hira.utils.api.response.ExperienceResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ExperienceApiService {
    @GET("experience/")
    suspend fun getAllExperienceById(@Query("search[idWorker]") idWorker: Int): ExperienceResponse
}