package com.reihan.hira.detailWorker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.reihan.hira.R
import com.reihan.hira.databinding.FragmentPortfolioBinding
import com.reihan.hira.databinding.ItemDialogPortfolioBinding
import com.reihan.hira.home.HomeFragment
import com.reihan.hira.utils.recyclerview.PortfolioAdapter
import com.reihan.hira.utils.api.APIClient
import com.reihan.hira.utils.api.model.PortfolioModel
import com.reihan.hira.utils.api.service.PortfolioApiService
import com.reihan.hira.utils.sharedpreferences.PreferenceHelper

@Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class PortfolioFragment : Fragment() {
    private lateinit var binding : FragmentPortfolioBinding
    private lateinit var viewModel: PortfolioViewModel
    private lateinit var recyclerView : PortfolioAdapter
    private lateinit var sharedPref : PreferenceHelper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_portfolio,container, false)
        sharedPref = PreferenceHelper(activity as AppCompatActivity)

        viewModel = ViewModelProvider(this).get(PortfolioViewModel::class.java)
        val service = APIClient.getApiClientToken(activity as AppCompatActivity)?.create(PortfolioApiService::class.java)
        if (service != null) {
            viewModel.setService(service)
        }
        val id = (activity as AppCompatActivity).intent.getStringExtra(HomeFragment.ID_WORKER)
        sharedPref = PreferenceHelper(activity as AppCompatActivity)

        if(id != null) {
            viewModel.getPortfolio(id.toInt())
        }
        subscribeLiveData()
        setListView()
        return binding.root
    }

    private fun subscribeLiveData(){
        viewModel.portfolioLiveData.observe(viewLifecycleOwner, Observer {
            (binding.rvPortfolio.adapter as PortfolioAdapter).addList(it)
        })
        viewModel.isEmptyLiveData.observe(viewLifecycleOwner, Observer {
            if(it){
                binding.llEmpty.visibility = View.VISIBLE
                binding.rvPortfolio.visibility = View.GONE
            }else {
                binding.llEmpty.visibility = View.GONE
                binding.rvPortfolio.visibility = View.VISIBLE
            }
        })
    }

    private fun setListView(){
        recyclerView = PortfolioAdapter(arrayListOf(), object : PortfolioAdapter.onClickViewListener {
            override fun onClick(data: PortfolioModel?) {
                if (data != null) {
                    dialog(data)
                }
            }
        })
        binding.rvPortfolio.adapter = recyclerView
        binding.rvPortfolio.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)

    }


    private fun dialog(data: PortfolioModel?){
        val view = DataBindingUtil.inflate<ItemDialogPortfolioBinding>(layoutInflater, R.layout.item_dialog_portfolio, null, false)
        val dialog = AlertDialog.Builder(activity as AppCompatActivity)
            .setView(view.root)
            .setCancelable(true)
            .create()
        dialog.show()
        view.tvNamePortfolio.text = data?.namePortfolio
        view.tvValueType.text = data?.typePortfolio
        view.tvValueLink.text = data?.linkRepo
        Glide.with(this).load("http://34.229.16.81:8008/uploads/${data?.image}")
            .placeholder(R.drawable.ic_ava).into(view.ivPortfolio)
    }
}