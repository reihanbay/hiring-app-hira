package com.reihan.hira.detailWorker

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.reihan.hira.R
import com.reihan.hira.databinding.ItemSkillBinding
import com.reihan.hira.utils.api.model.SkillModel

class SkillAdapter( val items: ArrayList<SkillModel>, val listener: OnClickViewListener): RecyclerView.Adapter<SkillAdapter.SkillViewHolder>() {


    fun addList(list: List<SkillModel>) {
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }

    class SkillViewHolder(val binding: ItemSkillBinding) :
        RecyclerView.ViewHolder(binding.root)

    interface OnClickViewListener {
        fun OnClick(id: Int?)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SkillViewHolder {
        return SkillViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_skill, parent, false))

    }

    override fun onBindViewHolder(holder: SkillViewHolder, position: Int) {
        val item = items[position]

        if (items.size == 0) {
            holder.binding.tvSkill.text = "Not Any Skill"
        }
        holder.binding.tvSkill.text = item.skill
    }

    override fun getItemCount(): Int = items.size
}