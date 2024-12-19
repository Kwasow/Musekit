package com.kwasow.musekit.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.kwasow.musekit.R
import com.kwasow.musekit.databinding.FragmentNoteForkAutoBinding
import com.kwasow.musekit.utils.MusekitPitchDetector
import com.kwasow.musekit.utils.PermissionManager
import com.kwasow.musekit.views.TunerView

class NoteForkAutoFragment : Fragment() {
    // ====== Fields
    private lateinit var binding: FragmentNoteForkAutoBinding
    private lateinit var tunerView: TunerView

    private val pitchDetector = MusekitPitchDetector()
    private val pitchObserver: Observer<Double> = Observer { pitch ->
        println(pitch)
        val res = MusekitPitchDetector.findNoteDetails(pitch)

        res?.let {
            tunerView.updateState(it.first, it.second)
            println(it)
        }
    }

    // ====== Interface methods
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNoteForkAutoBinding.inflate(inflater)
        tunerView = binding.tunerView

        PermissionManager.requestMicrophonePermission(this)

        pitchDetector.currentPitch.observe(viewLifecycleOwner, pitchObserver)

        return binding.root
    }

    override fun onResume() {
        super.onResume()

        if (PermissionManager.checkMicrophonePermission(requireContext())) {
            pitchDetector.startListening()
        } else {
            permissionNotGranted()
        }
    }

    override fun onPause() {
        super.onPause()
        pitchDetector.stopListening()
    }

    // ====== Private methods
    private fun permissionNotGranted() {
        Toast.makeText(
            requireContext(),
            R.string.record_permission_not_granted,
            Toast.LENGTH_SHORT
        ).show()
    }
}
