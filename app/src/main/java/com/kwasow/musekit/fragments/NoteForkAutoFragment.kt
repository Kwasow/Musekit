package com.kwasow.musekit.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.google.android.material.button.MaterialButton
import com.kwasow.musekit.data.Note
import com.kwasow.musekit.databinding.FragmentNoteForkAutoBinding
import com.kwasow.musekit.utils.MusekitPitchDetector
import com.kwasow.musekit.utils.PermissionManager
import com.kwasow.musekit.views.TunerView

class NoteForkAutoFragment : Fragment() {
    // ====== Fields
    private lateinit var binding: FragmentNoteForkAutoBinding
    private lateinit var tunerView: TunerView
    private lateinit var noPermissionView: LinearLayout
    private lateinit var openSettingsButton: MaterialButton

    private var pitchDetector: MusekitPitchDetector? = null
    private val pitchObserver: Observer<Pair<Note, Double>?> = Observer {
        it?.let {
            tunerView.updateState(it.first, it.second)
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
        noPermissionView = binding.noAudioPermissionView
        openSettingsButton = binding.openPermissionSettingButton

        openSettingsButton.setOnClickListener {
            PermissionManager.openPermissionSettings(requireContext())
        }

        PermissionManager.requestMicrophonePermission(this) { granted ->
            if (granted) {
                permissionGranted()
            } else {
                permissionNotGranted()
            }
        }

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()

        pitchDetector?.stopListening()
    }

    // ====== Private methods
    private fun permissionGranted() {
        tunerView.visibility = View.VISIBLE

        val dispatcher = MusekitPitchDetector.buildDefaultDispatcher()
        pitchDetector = MusekitPitchDetector(dispatcher)
        pitchDetector?.currentPitch?.observe(viewLifecycleOwner, pitchObserver)

        pitchDetector?.startListening()
    }

    private fun permissionNotGranted() {
        noPermissionView.visibility = View.VISIBLE
    }
}
