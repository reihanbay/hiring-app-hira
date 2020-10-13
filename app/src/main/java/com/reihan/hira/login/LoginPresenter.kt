package com.reihan.hira.login

import com.reihan.hira.utils.api.response.LoginResponse
import com.reihan.hira.utils.api.service.AccountApiService
import kotlinx.coroutines.*

class LoginPresenter(
    private val coroutineScope: CoroutineScope,
    private val service: AccountApiService?
) : LoginContract.Presenter {

    private var view: LoginContract.View? = null

    override fun bindToView(view: LoginContract.View) {
        this.view = view
    }

    override fun unbind() {
        this.view = null
    }

    override fun callLoginApi() {
        coroutineScope.launch {
            view?.showProgressBar()
            val response = withContext(Dispatchers.IO) {
                try {
                    service?.loginRequest(
                        view?.serviceParamaterEmail().toString(),
                        view?.serviceParamaterPassword().toString()
                    )
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            }

            if (response is LoginResponse) {
                if (response.data?.role == "worker") {
                    view?.toast("Access Denied")
                } else if (response.data?.role == "recruiter") {
                    view?.putSharedPreference(
                        response.data.token.toString(),
                        response.data.id.toString(),
                        response.data.name.toString(),
                        true
                    )
                    view?.toast("Login Success")
                    view?.intentToHome()
                } else {
                    view?.toast(response.message.toString())
                }
                view?.hideProgressBar()
            }
        }
    }
}