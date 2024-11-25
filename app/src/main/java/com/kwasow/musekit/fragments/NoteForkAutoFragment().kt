package com.kwasow.musekit.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.kwasow.musekit.databinding.FragmentNoteForkAutoBinding

class NoteForkAutoFragment : Fragment() {
    private lateinit var binding: FragmentNoteForkAutoBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNoteForkAutoBinding.inflate(inflater)
        return binding.root
    }
}