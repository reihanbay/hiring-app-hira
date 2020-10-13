package com.reihan.hira.detailWorker

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

class ProfileWorkerAdapter(fragment: FragmentManager) : FragmentStatePagerAdapter(fragment, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    val view = arrayOf(PortfolioFragment(), ExperienceFragment())

    override fun getCount(): Int = view.size
    override fun getItem(position: Int): Fragment = view[position]

    override fun getPageTitle(position: Int): CharSequence? {
        return when(position) {
            0 -> "Portfolio"
            1 -> "Experience"
            else -> ""
        }
    }
}