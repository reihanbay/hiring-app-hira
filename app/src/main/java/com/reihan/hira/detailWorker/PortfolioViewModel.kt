package com.reihan.hira.detailWorker

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.reihan.hira.utils.api.model.PortfolioModel
import com.reihan.hira.utils.api.response.PortfolioResponse
import com.reihan.hira.utils.api.service.PortfolioApiService
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class PortfolioViewModel : ViewModel(), CoroutineScope {
    override val coroutineContext: CoroutineContext
        get() = Job() + Dispatchers.Main

    private lateinit var service: PortfolioApiService
    val portfolioLiveData = MutableLiveData<List<PortfolioModel>>()
    val isEmptyLiveData = MutableLiveData<Boolean>()


    fun setService(service: PortfolioApiService){
        this.service = service
    }

    fun getPortfolio(id: Int){
        launch {
            val response = withContext(Dispatchers.IO){
                try {
                    service.getAllPortfolioById(id)
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            }
            if (response is PortfolioResponse) {
                if(response.data == null || response.message == "There is no Portfolio on list") {
                    isEmptyLiveData.value = true
                } else {
                    isEmptyLiveData.value = false
                    val list = response.data.map {
                        PortfolioModel(it.idPortfolio, it.imagePortfolio, it.linkRepo, it.typePortfolio,it.namePortfolio, it.idWorker)
                    }
                    portfolioLiveData.value = list
                }
            }
        }
    }
}