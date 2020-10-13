package com.reihan.hira

import android.os.Bundle
import android.os.Handler
import androidx.databinding.DataBindingUtil
import com.reihan.hira.databinding.ActivitySplashBinding

class Splash : BaseActivity() {

    private lateinit var binding : ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, layoutId())
        val time = 4000

        Handler().postDelayed(Runnable {
            Intent<AppIntroScreen>(this)
            startActivity(start)
            finish()
        }, time.toLong())
    }
    override fun layoutId(): Int {
        return R.layout.activity_splash
    }

}
