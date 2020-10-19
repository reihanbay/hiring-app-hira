package com.reihan.hira.profile

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
import com.reihan.hira.utils.api.service.ProfileApiService
import com.reihan.hira.utils.sharedpreferences.Constants
import com.reihan.hira.utils.sharedpreferences.PreferenceHelper
import kotlinx.coroutines.*

class ProfileFragment : Fragment() {

    private lateinit var binding: ActivityProfileScreenBinding
    lateinit var drawer: DrawerLayout
    private lateinit var viewModel: ProfileViewModel
    lateinit var sharedPref: PreferenceHelper

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

        drawer = binding.drawerLayout
        binding.drawerView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.menu_help -> {
                    val intent = Intent(activity, FormProfileActivity::class.java)
                    startActivity(intent)
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


    private fun subscribeLiveData() {
        viewModel.profileLiveData.observe(activity as AppCompatActivity, Observer {
            if (sharedPref.getString(Constants.KEY_ID_RECRUITER) != null) {
                if (it.data.idAccount == sharedPref.getString(Constants.KEY_ID)) {
                    sharedPref.put(Constants.KEY_ID_RECRUITER, it.data.idRecruiter.toString())
                    Glide.with(this@ProfileFragment)
                        .load("http://34.229.16.81:8080/uploads/${it.data.image.toString()}")
                        .placeholder(R.drawable.ava)
                        .into(binding.profilePict)
                    binding.tvCompanyName.text = it.data.company.toString()
                    binding.tvCompanySector.text = it.data.sector.toString()
                    binding.tvCompanyLocate.text = it.data.city.toString()
                    binding.tvContentSummaryCompany.text = it.data.description.toString()
                    binding.tvSocialInstagram.text = it.data.instagram.toString()
                    binding.tvSocialLinkedin.text = it.data.linkedin.toString()
                    binding.tvSocialWebsite.text = it.data.web.toString()
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

