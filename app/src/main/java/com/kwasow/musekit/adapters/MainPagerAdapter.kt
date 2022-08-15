package com.kwasow.musekit.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.kwasow.musekit.fragments.ErrorFragment
import com.kwasow.musekit.fragments.NoteForkFragment
import com.kwasow.musekit.fragments.SettingsFragment
import com.kwasow.musekit.fragments.MetronomeFragment

class MainPagerAdapter(fragmentActivity: FragmentActivity) :
  FragmentStateAdapter(fragmentActivity) {
  private val pageCount = 3

  override fun getItemCount(): Int = pageCount

  override fun createFragment(position: Int): Fragment {
    return when (position) {
      0 -> NoteForkFragment()
      1 -> MetronomeFragment()
      2 -> SettingsFragment()
      else -> ErrorFragment()
    }
  }
}