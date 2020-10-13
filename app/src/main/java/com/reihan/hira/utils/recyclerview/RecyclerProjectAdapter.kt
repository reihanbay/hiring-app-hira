package com.reihan.hira.utils.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.reihan.hira.R
import com.reihan.hira.databinding.ItemRecyclerViewProjectBinding
import com.reihan.hira.utils.api.model.ProjectModel


class RecyclerProjectAdapter(
    val items: ArrayList<ProjectModel>,
    val listener: onClickViewListener
) :
    RecyclerView.Adapter<RecyclerProjectAdapter.projectViewHolder>() {

    fun addList(list: List<ProjectModel>) {
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): projectViewHolder {
        return projectViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_recycler_view_project,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: projectViewHolder, position: Int) {
        val item = items[position]
        holder.binding.titleRecyclerViewProject.text = item.name
        holder.binding.deadlineRecyclerViewProject.text = item.deadline
        holder.binding.imageRecyclerViewProject.clipToOutline = true
        holder.binding.containerItemRecyclerProject.setOnClickListener {
            listener.OnClick(item.idProject)
        }
        Glide.with(holder.itemView).load("http://34.229.16.81:8080/uploads/${item.image}")
            .placeholder(R.drawable.ava).into(holder.binding.imageRecyclerViewProject)

    }

    override fun getItemCount(): Int = items.size

    class projectViewHolder(val binding: ItemRecyclerViewProjectBinding) :
        RecyclerView.ViewHolder(binding.root)

    interface onClickViewListener {
        fun OnClick(id: String)
    }
}