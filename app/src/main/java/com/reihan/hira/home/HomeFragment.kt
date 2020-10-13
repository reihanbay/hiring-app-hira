package com.reihan.hira.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.reihan.hira.detailWorker.ProfileWorkerActivity
import com.reihan.hira.R
import com.reihan.hira.databinding.FragmentHomeBinding
import com.reihan.hira.utils.recyclerview.RecycleWorkerAdapter
import com.reihan.hira.utils.api.APIClient
import com.reihan.hira.utils.api.service.WorkerApiService
import com.reihan.hira.utils.sharedpreferences.Constants
import com.reihan.hira.utils.sharedpreferences.PreferenceHelper

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var sharedPref: PreferenceHelper
    private lateinit var RecycleWorker: RecycleWorkerAdapter
    private lateinit var viewModel: HomeViewModel

    companion object {
        const val ID_WORKER = "idWorker"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)

        sharedPref = PreferenceHelper(activity as AppCompatActivity)
        val toolbar = binding.toolbarMain
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        binding.tvUsernameHeader.text = "Hai, ${sharedPref.getString(Constants.PREF_USERNAME)}!"

        val supportActionBar = (activity as AppCompatActivity).supportActionBar

        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setTitle(R.string.homeToolbar)

        val service = APIClient.getApiClientToken(activity as AppCompatActivity)
            ?.create(WorkerApiService::class.java)
        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        if (service != null) {
            viewModel.setWorkerService(service)
        }
        viewModel.callWorkerApi()
        subsribeLiveData()
        setUpRecyclerView()
        return binding.root
    }

    private fun setUpRecyclerView() {
        RecycleWorker =
            RecycleWorkerAdapter(arrayListOf(), object : RecycleWorkerAdapter.OnClickViewListener {
                override fun OnClick(id: String) {
                    Toast.makeText(activity, id, Toast.LENGTH_SHORT).show()
                    val intent =
                        Intent(activity as AppCompatActivity, ProfileWorkerActivity::class.java)
                    intent.putExtra(ID_WORKER, id)
                    startActivity(intent)
                }
            })
        binding.rvWorker.adapter = RecycleWorker
        binding.rvWorker.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
    }

    private fun subsribeLiveData() {
        viewModel.isLoadingProgressBarLiveData.observe(activity as AppCompatActivity, Observer {
            if (it) {
                binding.progressBar.visibility = View.VISIBLE
            } else {
                binding.progressBar.visibility = View.GONE
            }
        })

        viewModel.toastLiveData.observe(activity as AppCompatActivity, Observer {
            if (it) {
                Toast.makeText(activity as AppCompatActivity, "Failed Get", Toast.LENGTH_SHORT)
                    .show()
            }
        })

        viewModel.workerLiveData.observe(activity as AppCompatActivity, Observer {
            (binding.rvWorker.adapter as RecycleWorkerAdapter).addList(it)
        })
    }
}