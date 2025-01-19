package com.kwasow.musekit

import android.app.Application
import com.kwasow.musekit.utils.MusekitPreferences
import com.kwasow.musekit.utils.ThemeUtils

class MainApplication : Application() {

    // ====== Interface methods
    override fun onCreate() {
        super.onCreate()

        MusekitPreferences.init(this)
        ThemeUtils.init(this)
    }
}
