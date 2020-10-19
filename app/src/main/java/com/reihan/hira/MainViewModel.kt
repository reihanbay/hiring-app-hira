package com.reihan.hira

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.reihan.hira.utils.api.response.CheckProfileResponse
import com.reihan.hira.utils.api.service.ProfileApiService
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class MainViewModel: ViewModel(), CoroutineScope {
    private lateinit var serviceCheck : ProfileApiService
    val intentToFormLiveData = MutableLiveData<Boolean>()
    val isProgressLiveData = MutableLiveData<Boolean>()
    override val coroutineContext: CoroutineContext
        get() = Job() +Dispatchers.Main

    fun setServiceCheck(service: ProfileApiService) {
        this.serviceCheck = service
    }

    fun checkDataApi(id: Int){
        launch {
            isProgressLiveData.value = true
            val response = withContext(Dispatchers.IO) {
                try {
                    serviceCheck.checkProfileById(id)
                } catch (e : Throwable) {
                    e.printStackTrace()
                }
            }
            if (response is CheckProfileResponse) {
                intentToFormLiveData.value = response.success == false
            }
            isProgressLiveData.value = false
        }
    }
}