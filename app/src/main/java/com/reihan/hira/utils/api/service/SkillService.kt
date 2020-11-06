package com.reihan.hira.utils.api.service

import com.reihan.hira.utils.api.response.ListSkillResponse
import retrofit2.http.*

interface SkillService {

    @GET("skill/")
    suspend fun getSkill(@Query("search[idWorker]") id: Int?) : ListSkillResponse

}