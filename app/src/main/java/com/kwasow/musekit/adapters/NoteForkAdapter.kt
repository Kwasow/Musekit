package com.kwasow.musekit.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.kwasow.musekit.fragments.ErrorFragment
import com.kwasow.musekit.fragments.NoteForkAutoFragment
import com.kwasow.musekit.fragments.NoteForkManualFragment

class NoteForkAdapter(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {
    private val pageCount = 2

    override fun getItemCount(): Int = pageCount

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> NoteForkAutoFragment()
            1 -> NoteForkManualFragment()
            else -> ErrorFragment()
        }
    }
}
