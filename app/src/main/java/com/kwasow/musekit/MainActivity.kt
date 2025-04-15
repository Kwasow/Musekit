package com.kwasow.musekit

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import com.kwasow.musekit.managers.ThemeManager
import com.kwasow.musekit.ui.App
import com.kwasow.musekit.ui.theme.MusekitTheme
import org.koin.android.ext.android.inject
import org.koin.compose.KoinContext

class MainActivity : AppCompatActivity() {
    // ====== Fields
    private val themeManager by inject<ThemeManager>()

    // ====== Interface methods
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MusekitTheme(
                nightMode = themeManager.getNightMode(),
            ) {
                KoinContext {
                    App()
                }
            }
        }
    }
}
