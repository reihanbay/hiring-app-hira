package com.reihan.hira.profile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.reihan.hira.utils.api.response.ProfileResponse
import com.reihan.hira.utils.api.service.ProfileApiService
import kotlinx.coroutines.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import kotlin.coroutines.CoroutineContext

class FormProfileViewModel : ViewModel(), CoroutineScope {

    private lateinit var service: ProfileApiService
    val finishedFormLiveData = MutableLiveData<Boolean>()
    val inputIdLiveData = MutableLiveData<ProfileResponse>()

    override val coroutineContext: CoroutineContext
        get() = Job() + Dispatchers.Main

    fun setProfileService(service: ProfileApiService) {
        this.service = service
    }

    fun postProfileApi(
        name: RequestBody,
        company: RequestBody,
        position: RequestBody,
        sector: RequestBody,
        city: RequestBody,
        description: RequestBody,
        image: MultipartBody.Part,
        instagram: RequestBody,
        linkedin: RequestBody,
        web: RequestBody,
        idAccount: RequestBody
    ) {
        launch {
            val response = withContext(Dispatchers.IO) {
                try {
                    service.createProfile(
                        name,
                        company,
                        position,
                        sector,
                        city,
                        description,
                        image,
                        instagram,
                        linkedin,
                        web,
                        idAccount
                    )
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            }
            if (response is ProfileResponse) {
                inputIdLiveData.value= response
                finishedFormLiveData.value = true
            }
            finishedFormLiveData.value = false
        }
    }
}