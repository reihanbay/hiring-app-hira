package com.reihan.hira.detailWorker


import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.bumptech.glide.Glide
import com.reihan.hira.BaseActivity
import com.reihan.hira.R
import com.reihan.hira.databinding.ActivityProfileWorkerBinding
import com.reihan.hira.home.HomeFragment
import com.reihan.hira.hire.FormHireActivity
import com.reihan.hira.utils.api.APIClient
import com.reihan.hira.utils.api.service.SkillService
import com.reihan.hira.utils.api.service.WorkerApiService
import com.reihan.hira.utils.sharedpreferences.PreferenceHelper

class ProfileWorkerActivity : BaseActivity() {
    private lateinit var binding: ActivityProfileWorkerBinding
    lateinit var sharedPref: PreferenceHelper
    private lateinit var pagerAdapter: ProfileWorkerAdapter
    private lateinit var viewModel: ProfileWorkerViewModel
    private lateinit var recyclerView : SkillAdapter

    override fun layoutId(): Int {
        return R.layout.activity_profile_worker
    }

    companion object {
        const val ID_WORKER = "ID_WORKER"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, layoutId())
        val intent = intent.getStringExtra(HomeFragment.ID_WORKER)

        val toolbar = binding.toolbarMain
        this.setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setHomeButtonEnabled(true)
        toolbar.setNavigationOnClickListener { onBackPressed() }

        viewModel = ViewModelProvider(this).get(ProfileWorkerViewModel::class.java)
        val service = APIClient.getApiClientToken(this)?.create(WorkerApiService::class.java)
        val serviceSkill = APIClient.getApiClientToken(this)?.create(SkillService::class.java)
        if (service != null) {
            viewModel.setWorkerService(service)
        }
        if (serviceSkill != null) {
            viewModel.setSkillService(serviceSkill)
        }
        subscribeLiveData()
        val id = intent?.toInt()
        if (id != null) {
            viewModel.workerProfileApi(id)
            viewModel.getSkill(id.toInt())
        }
        setRecyclerSkill()
        binding.btnHire.setOnClickListener {
            IntentStart<FormHireActivity>(this)
            put(ID_WORKER, id.toString())
        }
        pagerAdapter = ProfileWorkerAdapter(supportFragmentManager)
        binding.viewPager.adapter = pagerAdapter
        binding.tabLayout.setupWithViewPager(binding.viewPager)
    }
    private fun setRecyclerSkill(){
        recyclerView = SkillAdapter( arrayListOf(), object : SkillAdapter.OnClickViewListener {
            override fun OnClick(id: Int?) {
            }
        })

        binding.rvSkill.adapter = recyclerView
        binding.rvSkill.layoutManager = StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.HORIZONTAL)
    }
    private fun subscribeLiveData() {
        viewModel.isProgessLiveData.observe(this, Observer {
            if (it) {
                binding.progressBar.visibility = View.VISIBLE
            } else {
                binding.progressBar.visibility = View.GONE
            }
        })
        viewModel.listSkillLiveData.observe(this, Observer {
            (binding.rvSkill.adapter as SkillAdapter).addList(it)
        })
        viewModel.workerLiveData.observe(this, Observer {
            Glide.with(this@ProfileWorkerActivity)
                .load("http://34.229.16.81:8008/uploads/${it.data.image.toString()}")
                .placeholder(R.drawable.ava)
                .into(binding.profilePict)
            binding.tvUsernameProfile.text = it.data.name.toString()
            binding.tvTitleProfile.text = it.data.title.toString()
            binding.tvLocateProfile.text = it.data.city.toString()
            binding.tvTalentProfile.text = it.data.statusJob.toString()
            binding.tvContentSummaryProfile.text = it.data.description.toString()
        })
        viewModel.isSuccessSkillLiveData.observe(this, Observer {
            if (it) {
                binding.tvSkill.visibility = View.GONE
                binding.rvSkill.visibility = View.VISIBLE
            } else {
                binding.rvSkill.visibility = View.GONE
                binding.tvSkill.visibility = View.VISIBLE
            }
        })
    }

}




