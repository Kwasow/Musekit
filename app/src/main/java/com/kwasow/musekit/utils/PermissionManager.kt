package com.kwasow.musekit.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

object PermissionManager {
    // ====== Public methods
    fun checkMicrophonePermission(context: Context): Boolean {
        val permissionStatus = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.RECORD_AUDIO
        )

        return permissionStatus == PackageManager.PERMISSION_GRANTED
    }

    fun requestMicrophonePermission(fragment: Fragment) {
        if (checkMicrophonePermission(fragment.requireContext())) {
            return
        }

        val requestPermissionLauncher = fragment.registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) {}

        requestPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
    }
}
