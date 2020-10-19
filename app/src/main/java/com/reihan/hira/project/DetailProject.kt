package com.reihan.hira.project

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.reihan.hira.BaseActivity
import com.reihan.hira.R
import com.reihan.hira.databinding.ActivityDetailProjectBinding
import com.reihan.hira.utils.api.service.ProjectApiService
import com.reihan.hira.utils.api.APIClient

class DetailProject : BaseActivity() {

    private lateinit var binding: ActivityDetailProjectBinding
    private lateinit var viewModel: DetailProjectViewModel
    override fun layoutId(): Int {
        return R.layout.activity_detail_project
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, layoutId())

        val toolbar = binding.toolbarMain
        this.setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setTitle(R.string.detailProjectToolbar)
        toolbar.setNavigationOnClickListener { onBackPressed() }

        viewModel = ViewModelProvider(this).get(DetailProjectViewModel::class.java)
        val service = APIClient.getApiClientToken(this)?.create(ProjectApiService::class.java)
        if (service != null) {
            viewModel.setProjectService(service)
        }
        val id = intent.getStringExtra(ProjectFragment.ID_PROJECT)
        viewModel.getProjectApi(id)
        subscribeLiveData()
    }

    private fun subscribeLiveData() {
        viewModel.projectLiveData.observe(this, Observer {
            Glide.with(this@DetailProject)
                .load("http://34.229.16.81:8080/uploads/${it.data.image.toString()}")
                .placeholder(R.drawable.ava)
                .into(binding.imageDetailProject)
            binding.tvTitleProject.text = it.data.nameProject.toString()
            binding.tvDeadlineProject.text = it.data.deadline.toString()
            binding.tvContentDescription.text = it.data.description.toString()
            if (it.data.worker == null) {
                binding.tvContentWorker.text
            } else {
                binding.tvContentWorker.text = it.data.worker
            }

        })
        viewModel.toastLiveData.observe(this, Observer {
            if (it) {
                Toast.makeText(this@DetailProject, "Get Project Success", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this@DetailProject, "Failed To Get Project", Toast.LENGTH_SHORT)
                    .show()
            }
        })

        viewModel.isProgressLiveData.observe(this, Observer {
            if (it) {
                binding.progressBar.visibility = View.VISIBLE
            } else {
                binding.progressBar.visibility = View.GONE
            }
        })
    }
}