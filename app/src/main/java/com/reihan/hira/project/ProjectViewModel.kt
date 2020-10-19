package com.reihan.hira.project

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.reihan.hira.utils.api.model.ProjectModel
import com.reihan.hira.utils.api.response.GetProjectsResponse
import com.reihan.hira.utils.api.service.ProjectApiService
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class ProjectViewModel : ViewModel(), CoroutineScope {
    private lateinit var service: ProjectApiService
    val inSuccessLiveData = MutableLiveData<Boolean>()
    val listProjectLiveData = MutableLiveData<List<ProjectModel>>()
    val isProgressLiveData = MutableLiveData<Boolean>()


    override val coroutineContext: CoroutineContext
        get() = Job() + Dispatchers.Main

    fun setProjectService(service: ProjectApiService) {
        this.service = service
    }

    fun getProjectApi(id: String) {
        launch {
            isProgressLiveData.value = true
            val response = withContext(Job() + Dispatchers.IO) {
                try {
                    service?.getAllProjectById(id)
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            }
            if (response is GetProjectsResponse) {
            Log.d("Check res", response.toString())
                if (response.data == null) {
                    inSuccessLiveData.value = false
                } else {
                    val list = response?.data?.map {
                        ProjectModel(
                            it.idProject.orEmpty(),
                            it.image.orEmpty(),
                            it.name.orEmpty(),
                            it.deadline.orEmpty()
                        )
                    }
                    listProjectLiveData.value = list
                    inSuccessLiveData.value = true
                }
            } else {
                inSuccessLiveData.value = false
            }
            isProgressLiveData.value = false
        }
    }
}