package com.kwasow.musekit

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.runtime.collectAsState
import com.kwasow.musekit.managers.PreferencesManager
import com.kwasow.musekit.ui.App
import com.kwasow.musekit.ui.theme.MusekitTheme
import org.koin.android.ext.android.inject
import org.koin.compose.KoinContext

class MainActivity : ComponentActivity() {
    // ====== Fields
    private val preferencesManager by inject<PreferencesManager>()

    // ====== Interface methods
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val nightMode =
                preferencesManager.nightMode.collectAsState(
                    AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM,
                )

            MusekitTheme(
                nightMode = nightMode.value,
            ) {
                KoinContext {
                    App()
                }
            }
        }
    }
}
