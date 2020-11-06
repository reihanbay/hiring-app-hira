package com.reihan.hira.detailWorker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.reihan.hira.R
import com.reihan.hira.databinding.FragmentExperienceBinding
import com.reihan.hira.home.HomeFragment
import com.reihan.hira.utils.recyclerview.ExperienceAdapter
import com.reihan.hira.utils.api.APIClient
import com.reihan.hira.utils.api.model.ExperienceModel
import com.reihan.hira.utils.api.service.ExperienceApiService
import com.reihan.hira.utils.sharedpreferences.PreferenceHelper

class ExperienceFragment : Fragment() {
    private lateinit var binding : FragmentExperienceBinding
    private lateinit var viewModel: ExperienceViewModel
    private lateinit var recyclerView: ExperienceAdapter
    private lateinit var sharedPref : PreferenceHelper


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_experience, container, false)
        sharedPref = PreferenceHelper(activity as AppCompatActivity)
        viewModel = ViewModelProvider(this).get(ExperienceViewModel::class.java)

        val service = APIClient.getApiClientToken(activity as AppCompatActivity)?.create(ExperienceApiService::class.java)
        if (service != null) {
            viewModel.setServiceExperience(service)
        }

        val id = (activity as AppCompatActivity).intent.getStringExtra(HomeFragment.ID_WORKER)

        if(id != null) {
            viewModel.getExperience(id.toInt())
        }
        subsribceLiveData()
        setRecyclerView()
        return binding.root
    }

    private fun subsribceLiveData(){
        viewModel.experienceLiveData.observe(viewLifecycleOwner, Observer {
            (binding.rvExperience.adapter as ExperienceAdapter).addList(it)
        })
        viewModel.isEmptyLiveData.observe(viewLifecycleOwner, Observer {
            if(it){
                binding.llEmpty.visibility = View.VISIBLE
                binding.rvExperience.visibility = View.GONE
            }else {
                binding.llEmpty.visibility = View.GONE
                binding.rvExperience.visibility = View.VISIBLE
            }
        })
    }

    private fun setRecyclerView(){
        recyclerView = ExperienceAdapter(arrayListOf(), object : ExperienceAdapter.OnClickViewListener{
            override fun OnClick(data: ExperienceModel?) {
                if (data != null) {

                }
            }

        })
        binding.rvExperience.adapter = recyclerView
        binding.rvExperience.layoutManager = LinearLayoutManager(activity,RecyclerView.VERTICAL, false)
    }
}