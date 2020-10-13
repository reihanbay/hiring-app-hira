package com.reihan.hira.login

interface LoginContract {

    interface View {
        fun hideProgressBar()
        fun showProgressBar()
        fun serviceParamaterEmail(): String?
        fun serviceParamaterPassword(): String?
        fun toast(message: String)
        fun putSharedPreference(token: String, idAccount: String, username: String, access: Boolean)
        fun intentToHome()
    }

    interface Presenter {
        fun bindToView(view: View)
        fun unbind()
        fun callLoginApi()
    }
}