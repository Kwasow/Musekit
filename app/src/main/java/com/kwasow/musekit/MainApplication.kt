package com.kwasow.musekit

import android.app.Application
import com.kwasow.musekit.utils.ThemeUtils

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        ThemeUtils.init(this)
    }
}
