package com.reihan.hira.register

import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.reihan.hira.BaseActivity
import com.reihan.hira.R
import com.reihan.hira.databinding.ActivitySignUpBinding
import com.reihan.hira.login.LoginActivity
import com.reihan.hira.utils.api.APIClient
import com.reihan.hira.utils.api.service.AccountApiService
import kotlinx.coroutines.*


class Sign_up : BaseActivity() {

    private lateinit var binding: ActivitySignUpBinding
    private lateinit var viewModel: SignUpViewModel
    lateinit var coroutineScope: CoroutineScope

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, layoutId())

        val service = APIClient.getApiClientToken(this)?.create(AccountApiService::class.java)

        viewModel = ViewModelProvider(this).get(SignUpViewModel::class.java)
        if (service != null) {
            viewModel.setRegisterService(service)
        }

        binding.btnCreate.setOnClickListener {
            viewModel.registerAccountApi(
                binding.etEmail.text.toString(),
                binding.etUsername.text.toString(),
                binding.etNoHp.text.toString(),
                binding.etPassword.text.toString()
            )
        }
        subscribeLiveData()
        initListener()
    }

    override fun layoutId(): Int {
        return R.layout.activity_sign_up
    }


    override fun initListener() {
        binding.tvToLogin?.setOnClickListener {
            Intent<LoginActivity>(this)
            startActivity(start)
        }
    }


    override fun onDestroy() {
        coroutineScope = CoroutineScope(Job() + Dispatchers.IO)
        coroutineScope.cancel()
        super.onDestroy()
    }

    private fun subscribeLiveData() {
        viewModel.isSignUpLiveData.observe(this, Observer {
            if (it) {
                Toast.makeText(this@Sign_up, "Register Success", Toast.LENGTH_LONG).show()
                IntentStart<LoginActivity>(this@Sign_up)
                finish()
            } else {
                Toast.makeText(this@Sign_up, "Register Failed", Toast.LENGTH_LONG).show()
            }
        })
        viewModel.toastLiveData.observe(this, Observer {
            if (it) {
                Toast.makeText(this@Sign_up, "Wrong Role!", Toast.LENGTH_LONG).show()
            }
        })

    }
}