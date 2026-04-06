package com.kwasow.musekit

import android.app.Application
import com.kwasow.musekit.koin.databaseModule
import com.kwasow.musekit.koin.managersModule
import com.kwasow.musekit.koin.viewModelsModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MainApplication : Application() {
    // ====== Interface methods
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@MainApplication)
            modules(databaseModule, managersModule, viewModelsModule)
        }
    }
}
