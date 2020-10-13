package com.reihan.hira.utils.api.service

import com.reihan.hira.utils.api.response.GetWorkerResponse
import com.reihan.hira.utils.api.response.WorkerByIdResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface WorkerApiService {
    @GET("worker/")
    suspend fun getAllWorker(): GetWorkerResponse

    @GET("worker/{id}")
    suspend fun getWorkerById(@Path("id") id: Int): WorkerByIdResponse
}