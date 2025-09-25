package com.kwasow.musekit

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.kwasow.musekit.managers.PreferencesManager
import com.kwasow.musekit.ui.App
import com.kwasow.musekit.ui.theme.MusekitTheme
import org.koin.android.ext.android.inject

class MainActivity : ComponentActivity() {
    // ====== Fields
    private val preferencesManager by inject<PreferencesManager>()

    // ====== Interface methods
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val nightMode by
                preferencesManager.nightMode.collectAsState(null)

            MusekitTheme(nightMode = nightMode) {
                App()
            }
        }
    }
}
