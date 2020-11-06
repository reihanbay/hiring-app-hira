package com.reihan.hira.project

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.reihan.hira.BaseActivity
import com.reihan.hira.R
import com.reihan.hira.databinding.ActivityFormProjectBinding
import com.reihan.hira.utils.api.service.ProjectApiService
import com.reihan.hira.utils.api.APIClient
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.reihan.hira.utils.api.model.ProjectModel
import com.reihan.hira.utils.sharedpreferences.Constants
import com.reihan.hira.utils.sharedpreferences.PreferenceHelper
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class FormProjectActivity : BaseActivity() {

    private lateinit var binding: ActivityFormProjectBinding
    private lateinit var viewModel: FormProjectViewModel
    private lateinit var sharedPref: PreferenceHelper
    override fun layoutId(): Int {
        return R.layout.activity_form_project
    }

    companion object {
        const val IMAGE_PICK_CODE = 1000
        const val CODE_RESULT = 2002
        const val PERMISSION_CODE = 1001
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, layoutId())
        viewModel = ViewModelProvider(this).get(FormProjectViewModel::class.java)
        val service = APIClient.getApiClientToken(this)?.create(ProjectApiService::class.java)
        if (service != null) {
            viewModel.setProjectService(service)
        }
        val data = intent.getParcelableExtra<ProjectModel>("DataParcelize")
        val session = intent.getStringExtra("SESSION")
        if (session != null && session == "EDIT") {
            Glide.with(this)
                .load("http://34.229.16.81:8008/uploads/${data.image}")
                .placeholder(R.drawable.ava)
                .into(binding.imgProject)
            binding.btnEdit.visibility = View.VISIBLE
            binding.btnSave.visibility = View.GONE
            binding.etNameProject.setText(data.name)
            binding.etDeadline.setText(data.deadline)
            binding.etDescription.setText(data.description)
        }
        binding.imgProject.clipToOutline = true
        binding.btnUpload.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                    val permission = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
                    requestPermissions(permission, PERMISSION_CODE)
                } else {
                    pickImageFromGallery()
                }
            } else {
                pickImageFromGallery()
            }
        }
    }

    private fun pickImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_PICK_CODE)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            PERMISSION_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    pickImageFromGallery()
                } else {
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        sharedPref = PreferenceHelper(this)
        if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_CODE) {
            binding.imgEditProject.visibility = View.VISIBLE
            binding.imgProject.visibility = View.GONE
            binding.imgEditProject.setImageURI(data?.data)
            binding.imgEditProject.clipToOutline = true
            val filePath = getPath(this, data?.data)
            val file = File(filePath)

            var img: MultipartBody.Part? = null
            val mediaTypeImg = "image/png".toMediaType()
            val inputStream = contentResolver.openInputStream(data?.data!!)
            val reqFile: RequestBody? = inputStream?.readBytes()?.toRequestBody(mediaTypeImg)

            img = reqFile?.let { it ->
                MultipartBody.Part.createFormData(
                    "image", file.name,
                    it
                )
            }
            subscribeLiveData()
            binding.btnSave.setOnClickListener {
                val name = createPartFromString(binding.etNameProject.text.toString())
                val description = createPartFromString(binding.etDescription.text.toString())
                val deadline = createPartFromString(binding.etDeadline.text.toString())
                val idRecruiter =
                    createPartFromString(sharedPref.getString(Constants.KEY_ID_RECRUITER).toString())
                if (img != null) {
                    viewModel.postProjectApi(name, description, deadline, idRecruiter, img)
                }
            }

            binding.btnEdit.setOnClickListener {
                val data = intent.getParcelableExtra<ProjectModel>("DataParcelize")
                val name = createPartFromString(binding.etNameProject.text.toString())
                val description = createPartFromString(binding.etDescription.text.toString())
                val deadline = createPartFromString(binding.etDeadline.text.toString())
                val idRecruiter =
                    createPartFromString(sharedPref.getString(Constants.KEY_ID_RECRUITER).toString())
                if (img != null) {
                    viewModel.putProjectApi(data.idProject.toInt(), name, description, deadline, idRecruiter, img)
                }
            }
        }
    }

    fun getPath(context: Context, uri: Uri?): String {
        var result: String? = null
        val proj = arrayOf(MediaStore.Images.Media.DATA)
        val cursor: Cursor? =
            uri?.let { context.contentResolver.query(it, proj, null, null, null) }
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                val column_index = cursor.getColumnIndexOrThrow(proj[0])
                result = cursor.getString(column_index)
            }
            cursor.close()
        }
        if (result == null) {
            result = "Not found"
        }
        return result
    }

    @NonNull
    private fun createPartFromString(json: String): RequestBody {
        val mediaType = "multipart/form-data".toMediaType()
        return json
            .toRequestBody(mediaType)
    }

    private fun subscribeLiveData() {
        viewModel.finishedFormLiveData.observe(this@FormProjectActivity, Observer {
            if (it) {
                Toast.makeText(this@FormProjectActivity, "Success", Toast.LENGTH_SHORT).show()
                setResult(Activity.RESULT_OK)
                finish()
            } else {
                Toast.makeText(this@FormProjectActivity, "Failed", Toast.LENGTH_SHORT).show()
            }
        })
    }
}