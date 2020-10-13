package com.reihan.hira.search

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.reihan.hira.utils.api.model.WorkerModel
import com.reihan.hira.utils.api.response.GetWorkerResponse
import com.reihan.hira.utils.api.service.WorkerApiService
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class SearchViewModel : ViewModel(), CoroutineScope {
    private lateinit var service: WorkerApiService
    val workerLiveData = MutableLiveData<List<WorkerModel>>()
    val isLoadingProgressBarLiveData = MutableLiveData<Boolean>()
    val toastLiveData = MutableLiveData<Boolean>()

    override val coroutineContext: CoroutineContext
        get() = Job() + Dispatchers.Main

    fun setWorkerService(service: WorkerApiService) {
        this.service = service
    }

    fun callWorkerApi() {
        launch {
            isLoadingProgressBarLiveData.value = true
            val response = withContext(Job() + Dispatchers.IO) {
                try {
                    service?.getAllWorker()
                } catch (e: Throwable) {
                    e.printStackTrace()
                    withContext(Dispatchers.Main) {
                        toastLiveData.value = true
                    }
                }
            }
            if (response is GetWorkerResponse) {
                val list = response.data?.map {
                    WorkerModel(
                        it.idWorker.orEmpty(),
                        it.image.orEmpty(),
                        it.name.orEmpty(),
                        it.title.orEmpty(),
                        it.skill.orEmpty()
                    )
                }
                workerLiveData.value = list
            } else {
                isLoadingProgressBarLiveData.value = false
                toastLiveData.value = true
            }
            isLoadingProgressBarLiveData.value = false
        }
    }
}