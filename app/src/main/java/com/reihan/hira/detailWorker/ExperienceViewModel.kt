package com.reihan.hira.detailWorker

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.reihan.hira.utils.api.model.ExperienceModel
import com.reihan.hira.utils.api.response.ExperienceResponse
import com.reihan.hira.utils.api.service.ExperienceApiService
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class ExperienceViewModel: ViewModel(), CoroutineScope {
    override val coroutineContext: CoroutineContext
        get() = Job() + Dispatchers.Main

    private lateinit var service: ExperienceApiService
    val experienceLiveData = MutableLiveData<List<ExperienceModel>>()
    val isEmptyLiveData = MutableLiveData<Boolean>()

    fun setServiceExperience(service: ExperienceApiService){
        this.service = service
    }

    fun getExperience(id: Int){
        launch {
            val response = withContext(Dispatchers.IO){
                try {
                    service.getAllExperienceById(id)
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            }
            if (response is ExperienceResponse) {
                if(response.data == null || response.message == "There is no Experience on list") {
                    isEmptyLiveData.value = true
                } else {
                    isEmptyLiveData.value = false
                    val list = response.data.map {
                        ExperienceModel(it.idExperience,it.companyName,it.description,it.workPosition,it.start,it.end,it.idWorker)
                    }

                    experienceLiveData.value = list
                }
            }
        }
    }
}