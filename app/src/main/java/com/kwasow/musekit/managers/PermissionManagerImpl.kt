package com.kwasow.musekit.managers

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

class PermissionManagerImpl(
    private val context: Context,
) : PermissionManager {
    // ====== Public methods
    override fun requestMicrophonePermission(
        fragment: Fragment,
        callback: ActivityResultCallback<Boolean>,
    ) {
        if (checkMicrophonePermission()) {
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

    override fun openPermissionSettings() {
        val intent =
            Intent(
                Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                Uri.parse("package:" + context.packageName),
            )
        intent.addCategory(Intent.CATEGORY_DEFAULT)

        context.startActivity(intent)
    }

    // ====== Private methods
    private fun checkMicrophonePermission(): Boolean {
        val permissionStatus =
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.RECORD_AUDIO,
            )

        return permissionStatus == PackageManager.PERMISSION_GRANTED
    }
}
