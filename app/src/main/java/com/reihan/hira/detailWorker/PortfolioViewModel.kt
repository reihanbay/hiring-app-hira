package com.reihan.hira.detailWorker

import androidx.lifecycle.ViewModel
import com.reihan.hira.utils.api.service.PortfolioApiService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

class PortfolioViewModel : ViewModel(), CoroutineScope {
    private lateinit var service: PortfolioApiService
    override val coroutineContext: CoroutineContext
        get() = Job() + Dispatchers.Main

    fun setPortfolioService() {

    }
}