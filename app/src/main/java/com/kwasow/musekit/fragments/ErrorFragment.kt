package com.kwasow.musekit.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.kwasow.musekit.databinding.FragmentErrorBinding
import kotlin.system.exitProcess

class ErrorFragment : Fragment() {
    private lateinit var binding: FragmentErrorBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentErrorBinding.inflate(inflater)
        return binding.root
    }

    override fun onStart() {
        super.onStart()

        binding.buttonGoBack.setOnClickListener {
            exitProcess(0)
        }
    }
}
