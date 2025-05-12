package com.kwasow.musekit.ui.screens.fork

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.kwasow.musekit.ui.components.PermissionMicrophoneView

// ====== Public composables
@Composable
fun NoteForkAutoScreen() {
    PermissionMicrophoneView { granted ->
        if (granted) {
            Text("Auto")
        } else {
            MicrophonePermissionNotGranted()
        }
    }
}

// ====== Private composables
@Composable
private fun MicrophonePermissionNotGranted() {
    Text("Microphone permission not granted")
}
