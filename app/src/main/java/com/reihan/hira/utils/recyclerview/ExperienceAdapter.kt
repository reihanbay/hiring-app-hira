package com.reihan.hira.utils.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.reihan.hira.R
import com.reihan.hira.databinding.ItemExperienceBinding
import com.reihan.hira.utils.api.model.ExperienceModel


class ExperienceAdapter(val items: ArrayList<ExperienceModel>) :
    RecyclerView.Adapter<ExperienceAdapter.experienceViewHolder>() {

    fun addList(list: List<ExperienceModel>) {
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }

    class experienceViewHolder(val binding: ItemExperienceBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): experienceViewHolder {
        return experienceViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_experience,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: experienceViewHolder, position: Int) {
        val item = items[position]

        val words = item.workPosition.split(" ")
        var work = ""
        words.forEach {
            work += it.capitalize() + " "
        }
        work.trimEnd()
        holder.binding.tvTitleJobStack.text = work
        holder.binding.tvWorkPlace.text = item.companyName
        holder.binding.tvPeriodeStart.text = "Start : ${item.start}"
        holder.binding.tvPeriodeEnd.text = "End : ${item.end}"
        holder.binding.tvDescription.text = item.description
    }

    override fun getItemCount(): Int = items.size

}