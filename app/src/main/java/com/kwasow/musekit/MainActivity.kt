package com.kwasow.musekit

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.lifecycleScope
import com.kwasow.musekit.managers.PreferencesManager
import com.kwasow.musekit.managers.ReviewManager
import com.kwasow.musekit.managers.UpdateManager
import com.kwasow.musekit.ui.App
import com.kwasow.musekit.ui.theme.MusekitTheme
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.koin.android.ext.android.inject
import kotlin.getValue

class MainActivity : ComponentActivity() {
    // ====== Fields
    private val preferencesManager by inject<PreferencesManager>()
    private val reviewManager by inject<ReviewManager>()
    private val updateManager by inject<UpdateManager>()

    // ====== Interface methods
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        syncInit()
        asyncInit()

        setContent {
            val nightMode by
                preferencesManager.nightMode.collectAsState(null)

            MusekitTheme(nightMode = nightMode) {
                App()
            }
        }
    }

    // ====== Private methods
    private fun syncInit() {
        runBlocking {
            updateManager.init()
        }
    }

    private fun asyncInit() {
        lifecycleScope.launch {
            reviewManager.init()
        }
    }
}
