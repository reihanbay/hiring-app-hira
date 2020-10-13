package com.reihan.hira.utils.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.reihan.hira.R
import com.reihan.hira.databinding.ItemPortfolioBinding
import com.reihan.hira.utils.api.model.PortfolioModel

class PortfolioAdapter(val items: ArrayList<PortfolioModel>, val listener: onClickViewListener) :
    RecyclerView.Adapter<PortfolioAdapter.portfolioViewHolder>() {

    fun addList(list: List<PortfolioModel>) {
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }

    interface onClickViewListener {
        fun onClick(id: Int)
    }

    class portfolioViewHolder(val binding: ItemPortfolioBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): portfolioViewHolder {
        return portfolioViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_portfolio,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: portfolioViewHolder, position: Int) {
        val item = items[position]
        Glide.with(holder.itemView).load("http://34.229.16.81:8080/uploads/${item.image}")
            .placeholder(R.drawable.ava).into(holder.binding.imagePortfolio)
        holder.binding.rlPortfolio.setOnClickListener {
            listener.onClick(item.idPortfolio)
        }
    }

    override fun getItemCount(): Int = items.size

}