package com.kwasow.musekit.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.kwasow.musekit.adapters.NoteForkAdapter
import com.kwasow.musekit.databinding.FragmentNoteForkBinding
import com.kwasow.musekit.utils.MusekitPreferences

class NoteForkFragment : Fragment() {
    // ====== Fields
    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager2

    // ====== Interface methods
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val binding = FragmentNoteForkBinding.inflate(inflater)
        tabLayout = binding.noteForkTabs
        viewPager = binding.noteForkPager

        // Prevent sleep while tuning
        binding.root.keepScreenOn = true
        setupTabs()

        return binding.root
    }

    // ====== Private methods
    private fun setupTabs() {
        // Setup viewPager
        val adapter = NoteForkAdapter(childFragmentManager, viewLifecycleOwner.lifecycle)

        viewPager.adapter = adapter
        viewPager.isUserInputEnabled = false

        tabLayout.addOnTabSelectedListener(
            object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    tab?.let {
                        viewPager.currentItem = tab.position
                        MusekitPreferences.noteForkMode = tab.position
                    }
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {}

                override fun onTabReselected(tab: TabLayout.Tab?) {}
            },
        )

        // Read default view from preferences
        val defaultTab = tabLayout.getTabAt(MusekitPreferences.noteForkMode)
        tabLayout.selectTab(defaultTab)
    }
}
