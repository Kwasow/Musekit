package com.kwasow.musekit.utils

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

object PermissionManager {
    // ====== Public methods
    fun requestMicrophonePermission(
        fragment: Fragment,
        callback: ActivityResultCallback<Boolean>,
    ) {
        if (checkMicrophonePermission(fragment.requireContext())) {
            callback.onActivityResult(true)
            return
        }

        val requestPermissionLauncher =
            fragment.registerForActivityResult(
                ActivityResultContracts.RequestPermission(),
                callback,
            )

        requestPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
    }

    fun openPermissionSettings(context: Context) {
        val intent =
            Intent(
                Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                Uri.parse("package:" + context.packageName),
            )
        intent.addCategory(Intent.CATEGORY_DEFAULT)

        context.startActivity(intent)
    }

    // ====== Private methods
    private fun checkMicrophonePermission(context: Context): Boolean {
        val permissionStatus =
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.RECORD_AUDIO,
            )

        return permissionStatus == PackageManager.PERMISSION_GRANTED
    }
}
