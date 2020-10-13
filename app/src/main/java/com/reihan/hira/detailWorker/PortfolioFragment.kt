package com.reihan.hira.detailWorker

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.reihan.hira.R
import com.reihan.hira.databinding.FragmentPortfolioBinding
import com.reihan.hira.home.HomeFragment
import com.reihan.hira.utils.recyclerview.PortfolioAdapter
import com.reihan.hira.utils.api.APIClient
import com.reihan.hira.utils.api.model.PortfolioModel
import com.reihan.hira.utils.api.response.PortfolioResponse
import com.reihan.hira.utils.api.service.PortfolioApiService
import kotlinx.coroutines.*

@Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class PortfolioFragment : Fragment() {
    private lateinit var binding: FragmentPortfolioBinding
    private var list: List<PortfolioModel> = listOf()
    private lateinit var RecyclerAdapter: PortfolioAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_portfolio, container, false)
        setUpRecycler()
        callPortfolioApi()
        return binding.root
    }

    private fun callPortfolioApi() {
        val service = APIClient.getApiClientToken(activity as AppCompatActivity)
            ?.create(PortfolioApiService::class.java)
        val coroutineScope = CoroutineScope(Job() + Dispatchers.Main)

        coroutineScope.launch {
            val response = withContext(Job() + Dispatchers.IO) {
                try {
                    service?.getAllPortfolioById(
                        (activity as AppCompatActivity).intent.getStringExtra(
                            HomeFragment.ID_WORKER
                        ).toInt()
                    )
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            }
            Log.d("checking", "port : ${response is PortfolioResponse}")
            if (response is PortfolioResponse) {
                list = response.data.map {
                    PortfolioModel(it.idPortfolio, it.imagePortfolio.orEmpty())
                } ?: listOf()
                (binding.rvPortfolio.adapter as PortfolioAdapter).addList(list)
            } else {
                Toast.makeText(activity as AppCompatActivity, "Failed Get Data", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun setUpRecycler() {
        RecyclerAdapter =
            PortfolioAdapter(arrayListOf(), object : PortfolioAdapter.onClickViewListener {
                override fun onClick(id: Int) {
                    Toast.makeText(activity as AppCompatActivity, "Clicked!", Toast.LENGTH_SHORT)
                        .show()
                }
            })
        binding.rvPortfolio.adapter = RecyclerAdapter
        binding.rvPortfolio.layoutManager =
            LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
    }
}