package com.reihan.hira.project

import android.app.Activity
import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import android.content.Intent
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.reihan.hira.BaseActivity
import com.reihan.hira.R
import com.reihan.hira.databinding.ActivityDetailProjectBinding
import com.reihan.hira.utils.api.service.ProjectApiService
import com.reihan.hira.utils.api.APIClient
import com.reihan.hira.utils.api.model.ProjectModel

class DetailProject : BaseActivity() {

    private lateinit var binding: ActivityDetailProjectBinding
    private lateinit var viewModel: DetailProjectViewModel
    private lateinit var dataParcelize : ProjectModel
    override fun layoutId(): Int {
        return R.layout.activity_detail_project
    }

    companion object {
        const val CODE_DETAIL = 2002
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
        binding.btnEdit.setOnClickListener {
            val intent = Intent(this, FormProjectActivity::class.java)
            intent.putExtra("SESSION", "EDIT")
            intent.putExtra("DataParcelize", dataParcelize)
            startActivityForResult(intent, FormProjectActivity.CODE_RESULT)
        }
        binding.btnDelete.setOnClickListener {
            showDialogLogout(id.toInt())

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == FormProjectActivity.CODE_RESULT) {
            val id = intent.getStringExtra(ProjectFragment.ID_PROJECT)
            viewModel.getProjectApi(id)
            subscribeLiveData()
        }
    }
    private fun subscribeLiveData() {
        viewModel.projectLiveData.observe(this, Observer {
            Glide.with(this@DetailProject)
                .load("http://34.229.16.81:8008/uploads/${it.data.image.toString()}")
                .placeholder(R.drawable.ava)
                .into(binding.imageDetailProject)
            binding.tvTitleProject.text = it.data.nameProject.toString()
            val time = it.data.deadline.toString()
            var date = ""
            for (a in 0..9) {
                date += time[a]
            }
            binding.tvDeadlineProject.text = date
            binding.tvContentDescription.text = it.data.description.toString()
            if (it.data.worker == null) {
                binding.tvContentWorker.text
            } else {
                binding.tvContentWorker.text = it.data.worker
            }
            dataParcelize = ProjectModel(it.data.idProject.toString(),it.data.image, it.data.nameProject.toString(), date, it.data.description.toString())

        })
        viewModel.toastLiveData.observe(this, Observer {
            if (it) {
                Toast.makeText(this@DetailProject, "Get Project Success", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this@DetailProject, "Failed", Toast.LENGTH_SHORT)
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

    private fun showDialogLogout(id: Int) {
        val dialog = AlertDialog.Builder(this)
            .setTitle("Delete Project")
            .setMessage("Are You Sure?")
            .setPositiveButton("Delete") { dialog: DialogInterface?, which: Int ->
                viewModel.deleteProjectApi(id)
                setResult(Activity.RESULT_OK)
                finish()
            }
            .setNegativeButton("No") { dialogInterface, i ->
                dialogInterface.dismiss()
            }
        dialog.show()
    }
}