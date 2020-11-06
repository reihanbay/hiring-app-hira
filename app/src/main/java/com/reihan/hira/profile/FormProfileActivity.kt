package com.reihan.hira.profile

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.reihan.hira.BaseActivity
import com.reihan.hira.R
import com.reihan.hira.databinding.ActivityFormProfileBinding
import com.reihan.hira.utils.api.APIClient
import com.reihan.hira.utils.api.model.ProfileModel
import com.reihan.hira.utils.api.service.ProfileApiService
import com.reihan.hira.utils.sharedpreferences.Constants
import com.reihan.hira.utils.sharedpreferences.PreferenceHelper
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class FormProfileActivity : BaseActivity() {
    private lateinit var binding: ActivityFormProfileBinding
    private lateinit var sharedPref: PreferenceHelper
    private lateinit var viewModel: FormProfileViewModel
    override fun layoutId(): Int {
        return R.layout.activity_form_profile
    }

    companion object {
        const val CODE_RESULT = 2002
        const val IMAGE_PICK_CODE = 1002
        const val PERMISSION_CODE = 1003
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, layoutId())
        viewModel = ViewModelProvider(this).get(FormProfileViewModel::class.java)
        val service = APIClient.getApiClientToken(this)?.create(ProfileApiService::class.java)
        binding.imgProfile.clipToOutline = true
        if (service != null) {
            viewModel.setProfileService(service)
        }
        val code = intent.getStringExtra("CODE_EDIT")
        val data = intent.getParcelableExtra<ProfileModel>("dataProfile")

        if (data != null && code == "EDIT"){
            Glide.with(this)
                .load("http://34.229.16.81:8008/uploads/${data.image}")
                .placeholder(R.drawable.ava)
                .into(binding.imgProfile)
            binding.etFullname.setText(data.name)
            binding.etPosition.setText(data.position)
            binding.etCompany.setText(data.company)
            binding.etSector.setText(data.sector)
            binding.etCity.setText(data.city)
            binding.etSummary.setText(data.description)
            binding.etInstagram.setText(data.instagram)
            binding.etLinkedin.setText(data.linkedin)
            binding.etWebsite.setText(data.web)

            binding.btnSave.visibility = View.GONE
            binding.btnSubmit.visibility = View.VISIBLE
        }

        binding.imgProfile.clipToOutline = true
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
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        sharedPref = PreferenceHelper(this)
        if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_CODE) {
            binding.imgEditProfile.visibility = View.VISIBLE
            binding.imgProfile.visibility = View.GONE
            binding.imgEditProfile.setImageURI(data?.data)
            binding.imgEditProfile.clipToOutline = true
            val filePath = getPath(this, data?.data)
            val file = File(filePath)

            var img: MultipartBody.Part? = null
            val mediaTypeImg = "image/png".toMediaType()
            val inputStream = contentResolver.openInputStream(data?.data!!)
            val reqFile: RequestBody? = inputStream?.readBytes()?.toRequestBody(mediaTypeImg)

            img = reqFile?.let { it ->
                MultipartBody.Part.createFormData("image", file.name, it)
            }
            subscribeLiveData()
            binding.btnSubmit.setOnClickListener {
                val name = createPartFromString(binding.etFullname.text.toString())
                val company = createPartFromString(binding.etCompany.text.toString())
                val position = createPartFromString(binding.etPosition.text.toString())
                val sector = createPartFromString(binding.etSector.text.toString())
                val city = createPartFromString(binding.etCity.text.toString())
                val description = createPartFromString(binding.etSummary.text.toString())
                val instagram = createPartFromString(binding.etInstagram.text.toString())
                val linkedin = createPartFromString(binding.etLinkedin.text.toString())
                val web = createPartFromString(binding.etWebsite.text.toString())
                val idAccount =
                    createPartFromString(sharedPref.getString(Constants.KEY_ID).toString())
                if (img != null) {
                    viewModel.postProfileApi(
                        name,
                        company,
                        position,
                        sector,
                        city,
                        description,
                        img,
                        instagram,
                        linkedin,
                        web,
                        idAccount
                    )
                }

            }
            binding.btnSubmit.setOnClickListener {
                val name = createPartFromString(binding.etFullname.text.toString())
                val company = createPartFromString(binding.etCompany.text.toString())
                val position = createPartFromString(binding.etPosition.text.toString())
                val sector = createPartFromString(binding.etSector.text.toString())
                val city = createPartFromString(binding.etCity.text.toString())
                val description = createPartFromString(binding.etSummary.text.toString())
                val instagram = createPartFromString(binding.etInstagram.text.toString())
                val linkedin = createPartFromString(binding.etLinkedin.text.toString())
                val web = createPartFromString(binding.etWebsite.text.toString())
                val idAccount =
                    createPartFromString(sharedPref.getString(Constants.KEY_ID).toString())
                val data = intent.getParcelableExtra<ProfileModel>("dataProfile")

                if (img != null) {
                    viewModel.putProfileApi(
                        data.idRecruiter.toInt(),
                        name,
                        company,
                        position,
                        sector,
                        city,
                        description,
                        img,
                        instagram,
                        linkedin,
                        web,
                        idAccount
                    )
                }

            }
        }
    }

    fun getPath(context: Context, uri: Uri?): String {
        var result: String? = null
        val proj = arrayOf(MediaStore.Images.Media.DATA)
        val cursor: Cursor? = uri?.let { context.contentResolver.query(it, proj, null, null, null) }
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                val column_index = cursor.getColumnIndexOrThrow(proj[0])
                result = cursor.getString(column_index)
            }
            cursor.close()
        }
        if (result == null) {
            result = "Not Found"
        }
        return result
    }

    @NonNull
    private fun createPartFromString(json: String): RequestBody {
        val mediaType = "multipart/form-data".toMediaType()
        return json.toRequestBody(mediaType)
    }

    private fun subscribeLiveData() {
        viewModel.finishedFormLiveData.observe(this@FormProfileActivity, Observer {
            if (it) {
                Toast.makeText(this@FormProfileActivity, "Success Save Profile", Toast.LENGTH_SHORT)
                    .show()
                setResult(Activity.RESULT_OK)
                finish()
            } else {
                Toast.makeText(this@FormProfileActivity, "Failed Save Profile", Toast.LENGTH_SHORT)
                    .show()
            }
        })
        viewModel.inputIdLiveData.observe(this@FormProfileActivity, Observer {
            sharedPref.put(Constants.KEY_ID_RECRUITER, it.data.idRecruiter.toString())
        })
    }

}

