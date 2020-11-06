package com.reihan.hira.project

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.reihan.hira.utils.api.response.ProjectByIdResponse
import com.reihan.hira.utils.api.service.ProjectApiService
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class DetailProjectViewModel : ViewModel(), CoroutineScope {
    private lateinit var service: ProjectApiService
    val projectLiveData = MutableLiveData<ProjectByIdResponse>()
    val isProgressLiveData = MutableLiveData<Boolean>()
    val toastLiveData = MutableLiveData<Boolean>()
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
                    service?.getAllProjectByIdProject(id)
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            }
            if (response is ProjectByIdResponse) {
                Log.d("Check Res", response.data.toString())

                projectLiveData.value = response
                toastLiveData.value = true
            } else {
                toastLiveData.value = false
            }
            isProgressLiveData.value = false
        }
    }

    fun deleteProjectApi(id: Int) {
        launch {
            isProgressLiveData.value = true
            val response = withContext(Job() + Dispatchers.IO) {
                try {
                    service?.deleteProject(id)
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            }
            isProgressLiveData.value = false
        }
    }

}