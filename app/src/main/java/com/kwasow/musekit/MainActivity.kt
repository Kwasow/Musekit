package com.kwasow.musekit

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.kwasow.musekit.managers.PreferencesManager
import com.kwasow.musekit.managers.UpdateManager
import com.kwasow.musekit.ui.App
import com.kwasow.musekit.ui.theme.MusekitTheme
import kotlinx.coroutines.runBlocking
import org.koin.android.ext.android.inject
import org.koin.compose.KoinContext
import kotlin.getValue

class MainActivity : ComponentActivity() {
    // ====== Fields
    private val preferencesManager by inject<PreferencesManager>()
    private val updateManager by inject<UpdateManager>()

    // ====== Interface methods
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        runBlocking {
            updateManager.init()
        }

        setContent {
            val nightMode by
                preferencesManager.nightMode.collectAsState(null)

            MusekitTheme(nightMode = nightMode) {
                KoinContext {
                    App()
                }
            }
        }
    }
}
