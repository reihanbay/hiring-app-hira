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
import android.util.Log
import android.widget.Toast
import androidx.annotation.NonNull
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
        //Image Pick Code
        const val IMAGE_PICK_CODE = 1000

        //Permission Code
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

        binding.btnUpload.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                    //permission denied
                    val permission = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
                    // show popup to req runtime permission
                    requestPermissions(permission, PERMISSION_CODE)
                } else {
                    //permission already granted
                    pickImageFromGallery()
                }
            } else {
                //system OS is < Marshmallow
                pickImageFromGallery()
            }
        }
    }

    private fun pickImageFromGallery() {
        //Intent to pick image
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
            binding.imgProject.setImageURI(data?.data)
            binding.imgProject.clipToOutline = true
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
            Toast.makeText(this, img.toString(), Toast.LENGTH_SHORT).show()

            binding.btnSave.setOnClickListener {
                Log.d("checkBind", binding.etNameProject.text.toString())
                val name = createPartFromString(binding.etNameProject.text.toString())
                val description = createPartFromString(binding.etDescription.text.toString())
                val deadline = createPartFromString(binding.etDeadline.text.toString())
                val idRecruiter =
                    createPartFromString(sharedPref.getString(Constants.KEY_ID).toString())
                if (img != null) {
                    viewModel.postProjectApi(name, description, deadline, idRecruiter, img)
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
}