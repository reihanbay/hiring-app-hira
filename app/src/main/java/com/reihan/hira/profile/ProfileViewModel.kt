package com.reihan.hira.profile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.reihan.hira.utils.api.response.ProfileResponse
import com.reihan.hira.utils.api.service.ProfileApiService
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class ProfileViewModel : ViewModel(), CoroutineScope {
    private lateinit var service: ProfileApiService
    val profileLiveData = MutableLiveData<ProfileResponse>()
    val isProgressLiveData = MutableLiveData<Boolean>()
    val toastLiveData = MutableLiveData<Boolean>()

    override val coroutineContext: CoroutineContext
        get() = Job() + Dispatchers.Main

    fun setProfileService(service: ProfileApiService) {
        this.service = service
    }

    fun getProfileApi(id: Int) {
        launch {
            isProgressLiveData.value = true
            val response = withContext(Dispatchers.IO) {
                try {
                    service.getProfileById(id)
                } catch (e: Throwable) {

                }
            }
            if (response is ProfileResponse) {
                profileLiveData.value = response
            } else {
                toastLiveData.value = false
            }
            isProgressLiveData.value = false
        }
    }
}