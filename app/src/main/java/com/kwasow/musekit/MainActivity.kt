package com.kwasow.musekit

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.navigation.NavigationBarView
import com.kwasow.musekit.adapters.MainPagerAdapter
import com.kwasow.musekit.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    // ====== Fields
    private lateinit var binding: ActivityMainBinding

    // ====== Interface methods
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        setupViewPager()

        setContentView(binding.root)
    }

    // ====== Private methods
    private fun setupViewPager() {
        val pager = binding.mainPager
        val pagerAdapter = MainPagerAdapter(this)
        pager.adapter = pagerAdapter
        pager.isUserInputEnabled = false

        binding.bottomNavigation.setOnItemSelectedListener(
            NavigationBarView.OnItemSelectedListener { item ->
                when (item.itemId) {
                    R.id.action_fork -> pager.currentItem = 0
                    R.id.action_metronome -> pager.currentItem = 1
                    R.id.action_about -> pager.currentItem = 2
                }

                return@OnItemSelectedListener true
            },
        )
    }
}
