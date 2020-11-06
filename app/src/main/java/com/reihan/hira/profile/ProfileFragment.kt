package com.reihan.hira.profile

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.reihan.hira.R
import com.reihan.hira.databinding.ActivityProfileScreenBinding
import com.reihan.hira.databinding.ContainerDialogLogoutBinding
import com.reihan.hira.login.LoginActivity
import com.reihan.hira.utils.api.APIClient
import com.reihan.hira.utils.api.model.ProfileModel
import com.reihan.hira.utils.api.service.ProfileApiService
import com.reihan.hira.utils.sharedpreferences.Constants
import com.reihan.hira.utils.sharedpreferences.PreferenceHelper
import kotlinx.coroutines.*

class ProfileFragment : Fragment() {

    private lateinit var binding: ActivityProfileScreenBinding
    lateinit var drawer: DrawerLayout
    private lateinit var viewModel: ProfileViewModel
    lateinit var sharedPref: PreferenceHelper
    private lateinit var ProfileModel: ProfileModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.activity_profile_screen,
            container,
            false
        )
        sharedPref = PreferenceHelper(activity as AppCompatActivity)

        val toolbar = binding.toolbarMain
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        val supportActionBar = (activity as AppCompatActivity).supportActionBar
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setHomeButtonEnabled(true)

        viewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)
        val service = APIClient.getApiClientToken(activity as AppCompatActivity)
            ?.create(ProfileApiService::class.java)
        if (service != null) {
            viewModel.setProfileService(service)
        }
        val id = sharedPref.getString(Constants.KEY_ID)
        if (id != null) {
            viewModel.getProfileApi(id.toInt())
        }
        subscribeLiveData()

        drawer = binding.drawerLayout
        binding.drawerView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.menu_edit -> {
                    val intent = Intent(activity, FormProfileActivity::class.java)
                    intent.putExtra("dataProfile", ProfileModel)
                    intent.putExtra("CODE_EDIT", "EDIT")
                    startActivityForResult(intent, FormProfileActivity.CODE_RESULT)
                }
                R.id.menu_settings ->
                    Toast.makeText(
                        (activity as AppCompatActivity).applicationContext,
                        "Toast Setting",
                        Toast.LENGTH_LONG
                    ).show()
                R.id.menu_info -> Toast.makeText(
                    (activity as AppCompatActivity).applicationContext,
                    "Toast Info",
                    Toast.LENGTH_LONG
                ).show()
                R.id.menu_logout -> logoutDialog()
            }
            true
        }
        setHasOptionsMenu(true);


        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (this@ProfileFragment.drawer.isDrawerOpen(Gravity.RIGHT)) {
                    this@ProfileFragment.drawer.closeDrawer(Gravity.RIGHT)
                } else {
                    handleOnBackPressed()
                }
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.item_ic_burger, menu)
        super.onCreateOptionsMenu(menu, menuInflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (drawer.isDrawerOpen(Gravity.RIGHT)) {
            drawer.closeDrawer(Gravity.RIGHT);
        } else {
            drawer.openDrawer(Gravity.RIGHT);
        }
        return true;
    }

    fun logoutDialog() {
        sharedPref = PreferenceHelper((activity as AppCompatActivity))
        val rootview = DataBindingUtil.inflate<ContainerDialogLogoutBinding>(
            layoutInflater,
            R.layout.container_dialog_logout,
            null,
            false
        )
        val dialog = AlertDialog.Builder((activity as AppCompatActivity))
            .setView(rootview.root)
            .setCancelable(true)
            .create()
        dialog.show()
        rootview.btnLogoutOk.setOnClickListener {
            sharedPref.clear()
            Toast.makeText(
                (activity as AppCompatActivity).applicationContext,
                "Wow Keluar",
                Toast.LENGTH_SHORT
            ).show()
            val intent =
                Intent((activity as AppCompatActivity).application, LoginActivity::class.java)
            startActivity(intent)
        }
        rootview.btnLogoutNo.setOnClickListener { dialog.dismiss() }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == FormProfileActivity.CODE_RESULT){
            val id = sharedPref.getString(Constants.KEY_ID)
            if (id != null) {
                subscribeLiveData()
                viewModel.getProfileApi(id.toInt())
            }
        }
    }

    private fun subscribeLiveData() {
        viewModel.profileLiveData.observe(activity as AppCompatActivity, Observer {
            if (it.data.idRecruiter != null) {
                if (it.data.idAccount == sharedPref.getString(Constants.KEY_ID)) {
                    sharedPref.put(Constants.KEY_ID_RECRUITER, it.data.idRecruiter.toString())
                    Glide.with(this@ProfileFragment)
                        .load("http://34.229.16.81:8008/uploads/${it.data.image.toString()}")
                        .placeholder(R.drawable.ava)
                        .into(binding.profilePict)
                    binding.tvCompanyName.text = it.data.company.toString()
                    binding.tvCompanySector.text = it.data.sector.toString()
                    binding.tvCompanyLocate.text = it.data.city.toString()
                    binding.tvContentSummaryCompany.text = it.data.description.toString()
                    binding.tvSocialInstagram.text = it.data.instagram.toString()
                    binding.tvSocialLinkedin.text = it.data.linkedin.toString()
                    binding.tvSocialWebsite.text = it.data.web.toString()
                    ProfileModel = ProfileModel(
                        it.data.idRecruiter,
                        it.data.name.toString(),
                        it.data.company.toString(),
                        it.data.position.toString(),
                        it.data.sector.toString(),
                        it.data.city.toString(),
                        it.data.description.toString(),
                        it.data.image.toString(),
                        it.data.instagram.toString(),
                        it.data.linkedin.toString(),
                        it.data.web.toString(),
                        it.data.idAccount.toString()
                    )
                }
            } else {
                val intent = Intent(activity as AppCompatActivity, FormProfileActivity::class.java)
                startActivity(intent)
            }
        })
        viewModel.toastLiveData.observe(activity as AppCompatActivity, Observer {
            if (it) {
                Toast.makeText(activity, "Get Data Success", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(activity, "Get Data Failed", Toast.LENGTH_SHORT).show()
            }
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

