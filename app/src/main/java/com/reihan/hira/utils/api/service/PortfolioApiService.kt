package com.reihan.hira.utils.api.service

import com.reihan.hira.utils.api.response.PortfolioResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface PortfolioApiService {
    @GET("portofolio/")
    suspend fun getAllPortfolioById(@Query("search[idWorker]") idWorker: Int): PortfolioResponse
}