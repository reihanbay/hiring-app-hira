package com.reihan.hira

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.reihan.hira.databinding.ActivityFormProfileBinding

class FormProfileActivity : BaseActivity() {


    private lateinit var binding: ActivityFormProfileBinding
    override fun layoutId(): Int {
        return R.layout.activity_form_profile
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, layoutId())

        binding.imgProfile.clipToOutline = true
    }
}

