package com.kwasow.musekit

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import com.kwasow.musekit.koin.databaseModule
import com.kwasow.musekit.koin.managersModule
import com.kwasow.musekit.koin.viewModelsModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import java.util.Locale

class MainApplication : Application() {
    // ====== Interface methods
    override fun onCreate() {
        super.onCreate()

        // 检测系统语言，若为中文则自动切换 App 语言为中文
        applyChineseLocaleIfNeeded()

        startKoin {
            androidContext(this@MainApplication)
            modules(databaseModule, managersModule, viewModelsModule)
        }
    }

    /**
     * 当系统语言为中文时，将应用语言设置为中文。
     * 使用 AppCompatDelegate.setApplicationLocales() 兼容 Android 13+ 及更低版本。
     */
    private fun applyChineseLocaleIfNeeded() {
        val systemLocale = Locale.getDefault()
        val language = systemLocale.language

        if (language.equals(Locale.CHINESE.language, ignoreCase = true) ||
            language.equals(Locale.SIMPLIFIED_CHINESE.language, ignoreCase = true) ||
            language.equals(Locale.TRADITIONAL_CHINESE.language, ignoreCase = true)
        ) {
            AppCompatDelegate.setApplicationLocales(
                LocaleListCompat.forLanguageTags("zh")
            )
        }
    }
}
