package com.kwasow.musekit.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView
import com.kwasow.musekit.R
import com.kwasow.musekit.data.Note
import com.kwasow.musekit.databinding.FragmentNoteForkAutoBinding
import com.kwasow.musekit.utils.MusekitPitchDetector
import com.kwasow.musekit.utils.MusekitPreferences
import com.kwasow.musekit.utils.PermissionManager
import com.kwasow.musekit.views.TunerView

class NoteForkAutoFragment : Fragment() {
    // ====== Fields
    private lateinit var tunerView: TunerView
    private lateinit var noPermissionView: LinearLayout
    private lateinit var openSettingsButton: MaterialButton
    private lateinit var pitchDownButton: MaterialButton
    private lateinit var pitchUpButton: MaterialButton
    private lateinit var pitchText: AppCompatTextView
    private lateinit var pitchSelectorView: MaterialCardView

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
        val binding = FragmentNoteForkAutoBinding.inflate(inflater)

        tunerView = binding.tunerView
        noPermissionView = binding.noAudioPermissionView
        openSettingsButton = binding.openPermissionSettingButton
        pitchDownButton = binding.buttonPitchDown
        pitchUpButton = binding.buttonPitchUp
        pitchText = binding.textPitch
        pitchSelectorView = binding.pitchSelectorView

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

        setupButtons()
        refreshPitch()

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()

        pitchDetector?.stopListening()
    }

    // ====== Private methods
    private fun permissionGranted() {
        // Update visibilities
        noPermissionView.visibility = View.GONE

        tunerView.visibility = View.VISIBLE
        pitchSelectorView.visibility = View.VISIBLE

        // Start pitch detection
        val dispatcher = MusekitPitchDetector.buildDefaultDispatcher()
        pitchDetector = MusekitPitchDetector(dispatcher)
        pitchDetector?.currentPitch?.observe(viewLifecycleOwner, pitchObserver)

        pitchDetector?.startListening()
    }

    private fun permissionNotGranted() {
        tunerView.visibility = View.GONE
        pitchSelectorView.visibility = View.GONE

        noPermissionView.visibility = View.VISIBLE
    }

    private fun refreshPitch() {
        pitchText.text = getString(
            R.string.pitch_placeholder,
            MusekitPreferences.automaticTunerPitch
        )
    }

    private fun setupButtons() {
        pitchDownButton.setOnClickListener {
            MusekitPreferences.automaticTunerPitch -= 1
            refreshPitch()
        }

        pitchUpButton.setOnClickListener {
            MusekitPreferences.automaticTunerPitch += 1
            refreshPitch()
        }
    }
}
