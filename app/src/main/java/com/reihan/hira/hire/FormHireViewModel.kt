package com.reihan.hira.hire

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.reihan.hira.utils.api.model.SpinnerProjectModel
import com.reihan.hira.utils.api.response.GetProjectsResponse
import com.reihan.hira.utils.api.response.HireResponse
import com.reihan.hira.utils.api.service.HireApiService
import com.reihan.hira.utils.api.service.ProjectApiService
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class FormHireViewModel : ViewModel(), CoroutineScope {

    private lateinit var servicePost: HireApiService
    private lateinit var serviceListProject: ProjectApiService
    val toastHireLiveData = MutableLiveData<Boolean>()
    val spinnerLiveData = MutableLiveData<List<SpinnerProjectModel>>()

    override val coroutineContext: CoroutineContext
        get() = Job() + Dispatchers.Main

    fun setHireApiService(service: HireApiService) {
        this.servicePost = service
    }

    fun setListProjectService(service: ProjectApiService) {
        this.serviceListProject = service
    }

    fun callHireApi(
        projectJob: String,
        message: String,
        price: Int,
        idWorker: Int,
        idProject: Int
    ) {
        launch {
            val response = withContext(Dispatchers.IO) {
                try {
                    servicePost?.postHire(
                        projectJob,
                        message,
                        0,
                        "2020-12-12",
                        price,
                        idWorker,
                        idProject
                    )
                } catch (e: Throwable) {
                    e.printStackTrace()
                    withContext(Dispatchers.Main) {
                        toastHireLiveData.value = false
                    }
                }
            }
            toastHireLiveData.value = response is HireResponse
        }
    }

    fun listSpinnerApi(uid: String) {
        launch {
            val response = withContext(Dispatchers.IO) {
                try {
                    serviceListProject?.getAllProjectById(uid)
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            }
            if (response is GetProjectsResponse) {
                val list = response.data.map {
                    SpinnerProjectModel(it.idProject.orEmpty().toString(), it.name.orEmpty())
                } ?: listOf()

                spinnerLiveData.value = list
            }
        }
    }
}