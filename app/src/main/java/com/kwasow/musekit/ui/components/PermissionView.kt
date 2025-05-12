package com.kwasow.musekit.ui.components

import android.Manifest
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.google.accompanist.permissions.rememberMultiplePermissionsState

// ====== Public composables
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun PermissionMicrophoneView(content: @Composable (granted: Boolean) -> Unit) {
    val permissionState =
        rememberMultiplePermissionsState(
            listOf(Manifest.permission.RECORD_AUDIO),
        )

    PermissionView(
        permissionState = permissionState,
        content = content,
    )
}

// ====== Private composables
@OptIn(ExperimentalPermissionsApi::class)
@Composable
private fun PermissionView(
    permissionState: MultiplePermissionsState,
    content: @Composable (granted: Boolean) -> Unit,
) {
    LaunchedEffect(true) {
        permissionState.launchMultiplePermissionRequest()
    }

    content(permissionState.allPermissionsGranted)
}
