package com.reihan.hira.project

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.reihan.hira.R
import com.reihan.hira.databinding.FragmentProjectBinding
import com.reihan.hira.utils.recyclerview.RecyclerProjectAdapter
import com.reihan.hira.utils.api.service.ProjectApiService
import com.reihan.hira.utils.api.APIClient
import com.reihan.hira.utils.sharedpreferences.Constants
import com.reihan.hira.utils.sharedpreferences.PreferenceHelper

class ProjectFragment : Fragment() {
    private lateinit var binding : FragmentProjectBinding
    private lateinit var sharedPref : PreferenceHelper
    private lateinit var RecyclerProject : RecyclerProjectAdapter
    private lateinit var viewModel: ProjectViewModel

    companion object {
        const val ID_PROJECT = "ID_PROJECT"
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_project, container, false)

        val toolbar = binding.toolbarMain
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        sharedPref = PreferenceHelper(activity as AppCompatActivity)
        val supportActionBar = (activity as AppCompatActivity).supportActionBar

        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        supportActionBar?.setDisplayShowTitleEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setTitle(R.string.projectToolbar)
        viewModel = ViewModelProvider(this).get(ProjectViewModel::class.java)
        val service = APIClient.getApiClientToken(activity as AppCompatActivity)?.create(
            ProjectApiService::class.java)
        if (service != null) {
            viewModel.setProjectService(service)
        }

        val id = sharedPref.getString(Constants.KEY_ID_RECRUITER).toString()
        viewModel.getProjectApi(id)
        subsribeLiveData()
        setupRecyclerView()
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        viewModel = ViewModelProvider(this).get(ProjectViewModel::class.java)
        val service = APIClient.getApiClientToken(activity as AppCompatActivity)?.create(
            ProjectApiService::class.java)
        if (service != null) {
            viewModel.setProjectService(service)
        }
        val id = sharedPref.getString(Constants.KEY_ID_RECRUITER).toString()

        if (resultCode == Activity.RESULT_OK && requestCode == FormProjectActivity.CODE_RESULT || requestCode == DetailProject.CODE_DETAIL){
            viewModel.getProjectApi(id)
            subsribeLiveData()
            setupRecyclerView()
        }

    }
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_project, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.project_last -> {
                Toast.makeText(activity as AppCompatActivity, "Project Sort By Last", Toast.LENGTH_LONG).show()
                return true
            }
            R.id.project_new -> {
                Toast.makeText(activity as AppCompatActivity, "Project Sort By Newest", Toast.LENGTH_LONG).show()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setupRecyclerView() {
        RecyclerProject= RecyclerProjectAdapter(arrayListOf(), object : RecyclerProjectAdapter.onClickViewListener {
            override fun OnClick(id: String) {
                Toast.makeText(activity, id, Toast.LENGTH_SHORT).show()
                val intent = Intent(activity as AppCompatActivity, DetailProject:: class.java)
                intent.putExtra(ID_PROJECT, id)
                startActivityForResult(intent, DetailProject.CODE_DETAIL)
            }
        })
        binding.btnAddWord.setOnClickListener {
            val intent = Intent(activity as AppCompatActivity, FormProjectActivity::class.java)
            startActivityForResult(intent, FormProjectActivity.CODE_RESULT)
        }
        binding.recycleView.adapter = RecyclerProject
        binding.recycleView.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
    }

    private fun subsribeLiveData() {
        viewModel.inSuccessLiveData.observe(activity as AppCompatActivity, Observer {
            if(it) {
                binding.vectorEmpty.visibility = View.GONE
                binding.recycleView.visibility = View.VISIBLE
                Toast.makeText(activity, "Success get data", Toast.LENGTH_SHORT).show()
            } else {
                binding.vectorEmpty.visibility = View.VISIBLE
                binding.recycleView.visibility = View.GONE
            }
        })
        viewModel.listProjectLiveData.observe(activity as AppCompatActivity, Observer {
            (binding.recycleView.adapter as RecyclerProjectAdapter).addList(it)
        })
        viewModel.isProgressLiveData.observe(activity as AppCompatActivity, Observer {
            if (it) {
                binding.progressBar.visibility = View.VISIBLE
            } else {
                binding.progressBar.visibility = View.GONE
            }
        })
    }

}