package com.reihan.hira.hire

import android.app.AlertDialog
import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.reihan.hira.BaseActivity
import com.reihan.hira.detailWorker.ProfileWorkerActivity
import com.reihan.hira.R
import com.reihan.hira.databinding.ActivityFormHireBinding
import com.reihan.hira.project.FormProjectActivity
import com.reihan.hira.utils.api.APIClient
import com.reihan.hira.utils.api.service.HireApiService
import com.reihan.hira.utils.api.service.ProjectApiService
import com.reihan.hira.utils.sharedpreferences.Constants
import com.reihan.hira.utils.sharedpreferences.PreferenceHelper


class FormHireActivity : BaseActivity() {
    private lateinit var binding: ActivityFormHireBinding
    private lateinit var sharedPref: PreferenceHelper
    private lateinit var viewModel: FormHireViewModel
    var selectedSpinner: Int = 0
    override fun layoutId(): Int {
        return R.layout.activity_form_hire
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, layoutId())
        sharedPref = PreferenceHelper(this)

        viewModel = ViewModelProvider(this).get(FormHireViewModel::class.java)

        val serviceList = APIClient.getApiClientToken(this)?.create(ProjectApiService::class.java)
        val servicePost = APIClient.getApiClientToken(this)?.create(HireApiService::class.java)

        if (servicePost != null) {
            viewModel.setHireApiService(servicePost)
        }

        if (serviceList != null) {
            viewModel.setListProjectService(serviceList)
        }

        subscribeLiveData()

        val uid = sharedPref.getString(Constants.KEY_ID).toString()
        Log.d("Checking", uid)
        viewModel.listSpinnerApi(uid)

        binding.btnSave.setOnClickListener {
            val id = intent.getStringExtra(ProfileWorkerActivity.ID_WORKER).toInt()
            viewModel.callHireApi(
                binding.etProjectJob.text.toString(),
                binding.etMessage.text.toString(),
                binding.etPrice.text.toString().toInt(),
                id,
                selectedSpinner
            )
        }

    }
    private fun subscribeLiveData() {
        viewModel.finishSessionHire.observe(this, Observer {
            if (it) {
                Toast.makeText(this, "Waiting for Agreement", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                val e = Throwable()
                Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show()
            }
        })
        viewModel.spinnerLiveData.observe(this, Observer {
            var spinner = binding.spinnerProject
            spinner.adapter = ArrayAdapter<String>(
                this@FormHireActivity,
                R.layout.support_simple_spinner_dropdown_item,
                it.map {
                    it.name
                })
            spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    selectedSpinner = it[position].idProject.toInt()
                    (parent!!.getChildAt(0) as TextView).setTextColor(Color.GRAY)
                    Log.d("check id", selectedSpinner.toString())
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                }
            }
        })
        viewModel.isSuccessSpinnerLiveData.observe(this, Observer {
            if (!it) {
              dialogToProject()
            }
        })
    }

    private fun dialogToProject(){
        val dialog = AlertDialog.Builder(this)
            .setTitle("Not Project Found")
            .setMessage("You must have a project")
            .setPositiveButton("Make Project"){dialog: DialogInterface?, which: Int ->
                IntentStart<FormProjectActivity>(this)
            }
            .setNegativeButton("Not Now"){dialog: DialogInterface?,which: Int ->
                onBackPressed()
            }
            .create()
        dialog.show()
    }
}