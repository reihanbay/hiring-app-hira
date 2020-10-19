package com.reihan.hira.login

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.reihan.hira.BaseActivity
import com.reihan.hira.MainActivity
import com.reihan.hira.R
import com.reihan.hira.register.Sign_up
import com.reihan.hira.databinding.ActivityLoginBinding
import com.reihan.hira.utils.api.APIClient
import com.reihan.hira.utils.api.service.AccountApiService
import com.reihan.hira.utils.api.service.ProfileApiService
import com.reihan.hira.utils.sharedpreferences.Constants
import com.reihan.hira.utils.sharedpreferences.PreferenceHelper
import kotlinx.coroutines.*

class LoginActivity : BaseActivity(), LoginContract.View {

    lateinit var sharedPref: PreferenceHelper
    lateinit var binding: ActivityLoginBinding
    lateinit var coroutineScope: CoroutineScope
    private var presenter: LoginPresenter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, layoutId())
        sharedPref = PreferenceHelper(this)
        coroutineScope = CoroutineScope(Job() + Dispatchers.Main)
        val serviceAccount = APIClient.getApiClientToken(this)?.create(AccountApiService::class.java)
        val serviceCheckData = APIClient.getApiClientToken(this)?.create(ProfileApiService::class.java)
        presenter = LoginPresenter(coroutineScope, serviceAccount)

        initListener()
    }

    override fun onStart() {
        super.onStart()

        if (sharedPref.getBoolean(Constants.PREF_IS_LOGIN)!!) {
            IntentStart<MainActivity>(this)
            startActivity(start)
        } else {
            presenter?.bindToView(this)
        }
    }

    override fun onStop() {
        presenter?.unbind()
        super.onStop()
    }

    override fun onDestroy() {
        coroutineScope.cancel()
        presenter = null
        super.onDestroy()
    }

    override fun layoutId(): Int {
        return R.layout.activity_login
    }

    override fun initListener() {
        binding.tvCreate.setOnClickListener {
            Intent<Sign_up>(this)
            startActivity(start)
        }
        binding.btnLogin.setOnClickListener {
            presenter?.callLoginApi()
        }
    }

    override fun hideProgressBar() {
        binding.pbLogin.visibility = View.GONE
    }

    override fun showProgressBar() {
        binding.pbLogin.visibility = View.VISIBLE
    }

    override fun serviceParamaterEmail(): String? {
        val email = binding.etEmail.text.toString()
        return email
    }

    override fun serviceParamaterPassword(): String? {
        val password = binding.etPassword.text.toString()
        return password
    }

    override fun toast(message: String) {
        Toast.makeText(this@LoginActivity, "$message", Toast.LENGTH_SHORT).show()
    }

    override fun putSharedPreference(
        token: String,
        idAccount: String,
        username: String,
        access: Boolean
    ) {
        sharedPref.put(Constants.KEY_TOKEN, token)
        sharedPref.put(Constants.KEY_ID, idAccount)
        sharedPref.put(Constants.PREF_USERNAME, username)
        sharedPref.put(Constants.PREF_IS_LOGIN, access)
    }

    override fun intentToHome() {
        IntentStart<MainActivity>(this@LoginActivity)
        startActivity(start)
    }


}