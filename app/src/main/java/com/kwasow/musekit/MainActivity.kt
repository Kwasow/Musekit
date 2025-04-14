package com.kwasow.musekit

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import com.kwasow.musekit.ui.App
import com.kwasow.musekit.ui.theme.MusekitTheme
import org.koin.compose.KoinContext

class MainActivity : AppCompatActivity() {
    // ====== Interface methods
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            KoinContext {
                MusekitTheme {
                    App()
                }
            }
        }
    }
}
