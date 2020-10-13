package com.reihan.hira.utils.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.reihan.hira.R
import com.reihan.hira.databinding.ItemWorkerBinding
import com.reihan.hira.utils.api.model.WorkerModel

class RecycleWorkerAdapter(val items: ArrayList<WorkerModel>, val listener: OnClickViewListener) :
    RecyclerView.Adapter<RecycleWorkerAdapter.WorkerViewHolder>() {

    class WorkerViewHolder(val binding: ItemWorkerBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkerViewHolder {
        return WorkerViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_worker,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: WorkerViewHolder, position: Int) {
        val item = items[position]
        Glide.with(holder.itemView).load("http://34.229.16.81:8080/uploads/${item.image}")
            .placeholder(R.drawable.ava).into(holder.binding.imageWorker)
        holder.binding.tvNameWorker.text = item.name
        holder.binding.tvListSkill.text = item.skill
        holder.binding.tvJobTitle.text = item.title
        holder.binding.imageWorker.clipToOutline = true
        holder.binding.containerItemRecyclerProject.setOnClickListener {
            listener.OnClick(item.idWorker)
        }
    }

    override fun getItemCount(): Int = items.size

    fun addList(list: List<WorkerModel>) {
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }

    interface OnClickViewListener {
        fun OnClick(id: String)
    }
}