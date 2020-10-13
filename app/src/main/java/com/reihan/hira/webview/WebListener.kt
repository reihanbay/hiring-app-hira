package com.reihan.hira.webview

interface WebListener {
    fun onPageStarted()
    fun onPageFinish()
    fun onShouldOverrideUrl(redirectUrl: String)
    fun onProgressChange(progress: Int)
}