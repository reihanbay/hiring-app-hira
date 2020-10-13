package com.reihan.hira.project

import android.util.Log
import androidx.lifecycle.ViewModel
import com.reihan.hira.utils.api.response.ProjectAddResponse
import com.reihan.hira.utils.api.service.ProjectApiService
import kotlinx.coroutines.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import kotlin.coroutines.CoroutineContext

class FormProjectViewModel : ViewModel(), CoroutineScope {

    private lateinit var service: ProjectApiService

    override val coroutineContext: CoroutineContext
        get() = Job() + Dispatchers.Main

    fun setProjectService(service: ProjectApiService) {
        this.service = service
    }

    fun postProjectApi(
        name: RequestBody, description: RequestBody, deadline: RequestBody,
        idRecruiter: RequestBody, image: MultipartBody.Part
    ) {
        launch {
            val response = withContext(Dispatchers.IO) {
                try {
                    service.postProject(name, description, deadline, idRecruiter, image)
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            }
            Log.d("cek", (response is ProjectAddResponse).toString())
            if (response is ProjectAddResponse) {
            }
        }
    }
}