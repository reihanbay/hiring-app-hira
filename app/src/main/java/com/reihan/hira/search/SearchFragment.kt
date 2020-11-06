package com.reihan.hira.search

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.reihan.hira.detailWorker.ProfileWorkerActivity
import com.reihan.hira.R
import com.reihan.hira.databinding.FragmentSearchBinding
import com.reihan.hira.home.HomeFragment
import com.reihan.hira.utils.recyclerview.RecycleWorkerAdapter
import com.reihan.hira.utils.api.APIClient
import com.reihan.hira.utils.api.model.WorkerModel
import com.reihan.hira.utils.api.service.WorkerApiService
import com.reihan.hira.utils.sharedpreferences.PreferenceHelper

class SearchFragment : Fragment() {

    private lateinit var binding : FragmentSearchBinding
    private lateinit var sharedPref: PreferenceHelper
    private lateinit var RecycleWorker: RecycleWorkerAdapter
    private lateinit var viewModel: SearchViewModel
    private var list : List<WorkerModel> = listOf()
    lateinit var sv: SearchView


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_search, container, false)
        sharedPref = PreferenceHelper(activity as AppCompatActivity)

        val toolbar = binding.toolbarMain
        (activity as AppCompatActivity).setSupportActionBar(toolbar)

        val supportActionBar = (activity as AppCompatActivity).supportActionBar
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        supportActionBar?.setDisplayShowTitleEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setTitle(R.string.searchToolbar)

        val service = APIClient.getApiClientToken(activity as AppCompatActivity)?.create(WorkerApiService::class.java)
        viewModel = ViewModelProvider(this).get(SearchViewModel::class.java)
        if (service != null) {
            viewModel.setWorkerService(service)
        }
        viewModel.callWorkerApi("", "nameWorker", "asc")
        binding.btnFilter.setOnClickListener {
            dialogFilter()
        }
        subsribeLiveData()

        setupRecyclerView()
        return binding.root
    }


    private fun setupRecyclerView( ) {
        RecycleWorker = RecycleWorkerAdapter(arrayListOf(), object: RecycleWorkerAdapter.OnClickViewListener{
            override fun OnClick(id: Int?) {
                val intent = Intent(activity as AppCompatActivity, ProfileWorkerActivity:: class.java)
                intent.putExtra(HomeFragment.ID_WORKER, id.toString())
                startActivity(intent)
            }
        })
        binding.rvSearch.adapter = RecycleWorker
        binding.rvSearch.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
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
                Toast.makeText(activity as AppCompatActivity, "Failed Get" , Toast.LENGTH_SHORT).show()
            }
        })

        viewModel.workerLiveData.observe(activity as AppCompatActivity, Observer {
            (binding.rvSearch.adapter as RecycleWorkerAdapter).addList(it)

            sv = binding.svSearch

            sv.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String): Boolean {
                    var result: ArrayList<WorkerModel> = ArrayList()
                    for (find in it) {
                        if (find.name.contains(query) || find.title.contains(query) || find.city.contains(query) || find.status.contains(query) || find.skill.contains(query)) {
                            result.add(find)
                        }
                    }
                    (binding.rvSearch.adapter as RecycleWorkerAdapter).addList(result)
                    return false
                }

                override fun onQueryTextChange(newText: String): Boolean {
                    var result: ArrayList<WorkerModel> = ArrayList()
                    for (find in it) {
                        if (find.name.contains(newText) || find.title.contains(newText) || find.city.contains(newText) || find.status.contains(newText) || find.skill.contains(newText)) {
                            result.add(find)
                        }
                    }
                    (binding.rvSearch.adapter as RecycleWorkerAdapter).addList(result)
                    return false
                }
            })
        })
    }

    private fun dialogFilter(){
        val singleItems = arrayOf("Filter Berdasar Nama", "Filter Berdasar Skill", "Filter Berdasar Lokasi", "Filter Berdasar Freelance", "Filter Berdasar Fulltime")
        AlertDialog.Builder(activity as AppCompatActivity)
            // Single-choice items (initialized with checked item)
            .setCancelable(true)
            .setItems(singleItems) { dialog, which ->
                when (which) {
                    0 -> {
                        viewModel.callWorkerApi("","nameWorker", "asc")
                        dialog.dismiss()
                    }
                    1 -> {
                        viewModel.callWorkerApi("","skill", "asc")
                        dialog.dismiss()
                    }
                    2 -> {
                        viewModel.callWorkerApi("","city", "asc")
                        dialog.dismiss()
                    }
                    3 -> {
                        viewModel.callWorkerApi("freelance","nameWorker", "asc")
                        dialog.dismiss()
                    }
                    4 -> {
                        viewModel.callWorkerApi("full","nameWorker", "asc")
                        dialog.dismiss()
                    }
                }
            }
            .create()
            .show()
    }

}