package com.reihan.hira.utils.recyclerview

import android.view.LayoutInflater
import android.view.View
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
        Glide.with(holder.itemView).load("http://34.229.16.81:8008/uploads/${item.image}")
            .placeholder(R.drawable.ava).into(holder.binding.imageWorker)
        holder.binding.tvNameWorker.text = item.name
        if (item.skill != "Not Any Skill") {
            var skill = item.skill.split(",")
            holder.binding.tvSkill1.text = skill[0]
            if (skill.size > 1) {
                holder.binding.tvSkill2.text = skill[1]
                holder.binding.tvSkill3.text = skill[2]
                holder.binding.tvSkillPlus.text = "${skill.size - 2}+"
            } else {
                holder.binding.tvSkillPlus.visibility = View.GONE
                holder.binding.tvSkill2.visibility = View.GONE
            }
        } else {
            holder.binding.tvSkillPlus.text = item.skill
            holder.binding.tvSkill1.visibility = View.GONE
            holder.binding.tvSkill2.visibility = View.GONE
            holder.binding.tvSkill3.visibility = View.GONE
        }
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
        fun OnClick(id: Int?)
    }
}