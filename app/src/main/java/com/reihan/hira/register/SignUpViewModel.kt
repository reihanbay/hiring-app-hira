package com.reihan.hira.register

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.reihan.hira.utils.api.response.RegisterResponse
import com.reihan.hira.utils.api.service.AccountApiService
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class SignUpViewModel : ViewModel(), CoroutineScope {

    private lateinit var service : AccountApiService
    val isSignUpLiveData = MutableLiveData<Boolean>()
    val toastLiveData = MutableLiveData<Boolean>()
    override val coroutineContext: CoroutineContext
        get() = Job() + Dispatchers.Main

    fun setRegisterService(service: AccountApiService) {
        this.service = service
    }

    fun registerAccountApi(email: String, name: String, noHp: String,password: String){
        launch {
            val response = withContext(Dispatchers.IO) {
                try {
                    service.registerRequest(name, email, password, noHp, "recruiter", 0)
                } catch (e: Throwable) {
                    e.printStackTrace()
                    withContext(Dispatchers.Main) {
                        isSignUpLiveData.value = false
                    }
                }
            }

            if (response is RegisterResponse) {
                if (response.data?.role == "worker") {
                    toastLiveData.value = true
                } else isSignUpLiveData.value = response.data?.role == "recruiter"
                Log.d("android1", response.toString())
            }
        }
    }

}