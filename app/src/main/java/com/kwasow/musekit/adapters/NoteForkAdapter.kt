package com.kwasow.musekit.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.kwasow.musekit.fragments.ErrorFragment
import com.kwasow.musekit.fragments.NoteForkAutoFragment
import com.kwasow.musekit.fragments.NoteForkManualFragment

class NoteForkAdapter(fragmentActivity: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentActivity, lifecycle) {
    // ====== Fields
    private val pageCount = 2

    // ====== Interface methods
    override fun getItemCount(): Int = pageCount

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> NoteForkAutoFragment()
            1 -> NoteForkManualFragment()
            else -> ErrorFragment()
        }
    }
}
