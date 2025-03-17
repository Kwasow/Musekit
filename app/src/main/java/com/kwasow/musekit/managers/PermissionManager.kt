package com.kwasow.musekit.managers

import androidx.activity.result.ActivityResultCallback
import androidx.fragment.app.Fragment

interface PermissionManager {
    // ====== Methods
    fun requestMicrophonePermission(
        fragment: Fragment,
        callback: ActivityResultCallback<Boolean>,
    )

    fun openPermissionSettings()
}
