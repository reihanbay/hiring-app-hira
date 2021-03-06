package com.reihan.hira

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

abstract class BaseActivity : AppCompatActivity() {
    abstract fun layoutId(): Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    open fun findView() {} //untuk mengambil Id dari Xml
    open fun initListener() {} // untuk Melistener / menjalankan proses

    //Intent
    var start: Intent? = null
    inline fun <reified ClassActivity> Intent(context: Context) {
        start = Intent(context, ClassActivity::class.java)
    }

    inline fun <reified ClassActivity> IntentStart(context: Context) {
        start = Intent(context, ClassActivity::class.java)
        startActivity(start)
    }

    open fun put(name: String, value: String) {
        start?.putExtra(name, value)
    }
}