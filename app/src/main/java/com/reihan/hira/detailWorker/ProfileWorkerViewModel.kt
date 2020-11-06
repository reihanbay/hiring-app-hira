package com.reihan.hira.detailWorker

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.reihan.hira.utils.api.model.SkillModel
import com.reihan.hira.utils.api.response.ListSkillResponse
import com.reihan.hira.utils.api.response.WorkerByIdResponse
import com.reihan.hira.utils.api.service.SkillService
import com.reihan.hira.utils.api.service.WorkerApiService
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class ProfileWorkerViewModel : ViewModel(), CoroutineScope {
    private lateinit var service: WorkerApiService
    private lateinit var serviceSkill: SkillService
    val isProgessLiveData = MutableLiveData<Boolean>()
    val workerLiveData = MutableLiveData<WorkerByIdResponse>()
    val listSkillLiveData = MutableLiveData<List<SkillModel>>()
    val isSuccessSkillLiveData = MutableLiveData<Boolean>()
    override val coroutineContext: CoroutineContext
        get() = Job() + Dispatchers.Main

    fun setWorkerService(service: WorkerApiService) {
        this.service = service
    }

    fun setSkillService(service: SkillService) {
        this.serviceSkill = service
    }

    fun workerProfileApi(id: Int) {
        launch {
            isProgessLiveData.value = true
            val response = withContext(Job() + Dispatchers.IO) {
                try {
                    service?.getWorkerById(id)
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            }
            Log.d("checkId", (response is WorkerByIdResponse).toString())

            if (response is WorkerByIdResponse) {
                workerLiveData.value = response
            } else if (response is Throwable) {
                Log.e("and", response.message ?: "Error")
            }
            isProgessLiveData.value = false
        }
    }

    fun getSkill(id: Int?) {
        launch {
            val response = withContext(Dispatchers.IO) {
                try {
                    serviceSkill.getSkill(id)
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            }
            if (response is ListSkillResponse) {

                if (response.messages == "There is no Skill on list") {
                    isSuccessSkillLiveData.value = false

                } else {
                    isSuccessSkillLiveData.value = true
                    val list = response.data.map {
                        SkillModel(it.idSkill.toString().toInt(), it.idWorker.toString().toInt(), it.skill.orEmpty())
                    }
                    listSkillLiveData.value = list
                }

            }
        }
    }

}