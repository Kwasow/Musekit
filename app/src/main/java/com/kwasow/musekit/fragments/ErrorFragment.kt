package com.kwasow.musekit.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.button.MaterialButton
import com.kwasow.musekit.databinding.FragmentErrorBinding
import kotlin.system.exitProcess

class ErrorFragment : Fragment() {
    // ====== Fields
    private lateinit var buttonCloseApp: MaterialButton

    // ====== Interface methods
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentErrorBinding.inflate(inflater)

        buttonCloseApp = binding.buttonCloseApp

        return binding.root
    }

    override fun onStart() {
        super.onStart()

        buttonCloseApp.setOnClickListener {
            exitProcess(0)
        }
    }
}
