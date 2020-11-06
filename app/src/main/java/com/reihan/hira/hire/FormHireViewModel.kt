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
    val spinnerLiveData = MutableLiveData<List<SpinnerProjectModel>>()
    val isSuccessSpinnerLiveData = MutableLiveData<Boolean>()
    val finishSessionHire = MutableLiveData<Boolean>()

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
                    servicePost.postHire(
                        projectJob,
                        message,
                        "delayed",
                        "0000-00-00",
                        price,
                        idWorker,
                        idProject
                    )
                } catch (e: Throwable) {
                    e.printStackTrace()
                    withContext(Dispatchers.Main) {
                        finishSessionHire.value = false
                    }
                }
            }
            if (response is HireResponse) {
                finishSessionHire.value = response.success == true
            }
        }
    }

    fun listSpinnerApi(uid: String) {
        launch {
            val response = withContext(Dispatchers.IO) {
                try {
                    serviceListProject.getAllProjectById(uid)
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            }
            if (response is GetProjectsResponse) {
                if (response.success == false) {
                    isSuccessSpinnerLiveData.value = false
                } else {
                    val list = response.data.map {
                        SpinnerProjectModel(it.idProject.orEmpty().toString(), it.name.orEmpty())
                    }
                    spinnerLiveData.value = list
                }

            }
        }
    }
}