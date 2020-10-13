package com.reihan.hira.detailWorker

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.reihan.hira.utils.api.response.WorkerByIdResponse
import com.reihan.hira.utils.api.service.WorkerApiService
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class ProfileWorkerViewModel : ViewModel(), CoroutineScope {
    private lateinit var service: WorkerApiService
    val isProgessLiveData = MutableLiveData<Boolean>()
    val workerLiveData = MutableLiveData<WorkerByIdResponse>()
    override val coroutineContext: CoroutineContext
        get() = Job() + Dispatchers.Main

    fun setWorkerService(service: WorkerApiService) {
        this.service = service
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

}