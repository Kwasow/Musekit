package com.kwasow.musekit.fragments

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.color.MaterialColors
import com.google.android.material.tabs.TabLayout
import com.kwasow.musekit.R
import com.kwasow.musekit.adapters.NoteForkAdapter
import com.kwasow.musekit.databinding.FragmentNoteForkBinding

class NoteForkFragment : Fragment() {
    private lateinit var binding: FragmentNoteForkBinding
    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager2

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNoteForkBinding.inflate(inflater)
        tabLayout = binding.noteForkTabs
        viewPager = binding.noteForkPager

        return binding.root
    }

    override fun onStart() {
        super.onStart()

        setupTabs()
    }

    private fun setupTabs() {
        // Setup viewPager
        val adapter = NoteForkAdapter(requireActivity())

        viewPager.adapter = adapter
        viewPager.isUserInputEnabled = false

        tabLayout.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener {

            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.let {
                    viewPager.currentItem = tab.position
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}

            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }
}