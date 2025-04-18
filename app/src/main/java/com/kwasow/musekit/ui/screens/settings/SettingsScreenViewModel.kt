package com.kwasow.musekit.ui.screens.settings

import android.content.Context
import android.content.Intent
import androidx.annotation.RawRes
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import com.kwasow.musekit.data.NotationStyle
import com.kwasow.musekit.managers.PreferencesManager
import com.kwasow.musekit.managers.ThemeManager

class SettingsScreenViewModel(
    private val applicationContext: Context,
    private val preferencesManager: PreferencesManager,
    private val themeManager: ThemeManager,
) : ViewModel() {
    // ====== Fields
    var showLicensesDialog by mutableStateOf(false)
    var currentLicenseName by mutableStateOf<String?>(null)
    var currentLicenseText by mutableStateOf<String?>(null)

    var notationStyle by mutableStateOf(preferencesManager.getNotationStyle())
    var themeMode by mutableIntStateOf(themeManager.getNightMode())

    // ====== Public methods
    fun updateNotationStyle(style: NotationStyle) {
        preferencesManager.setNotationStyle(style)
        notationStyle = preferencesManager.getNotationStyle()
    }

    fun updateThemeMode(mode: Int) {
        themeManager.setNightMode(mode)
        themeMode = themeManager.getNightMode()
    }

    fun openGithub() = openUrl("https://github.com/Kwasow/Musekit")

    fun openMastodon() = openUrl("https://mstdn.social/@kwasow")

    fun openWebsite() = openUrl("https://kwasow.pl")

    fun openLicenseDialog(
        name: String,
        file: Int,
    ) {
        showLicensesDialog = false
        currentLicenseName = name
        currentLicenseText = readRawFileAsString(file)
    }

    fun closeDialogs() {
        showLicensesDialog = false
        currentLicenseName = null
        currentLicenseName = null
    }

    // ====== Private methods
    private fun openUrl(url: String) {
        val browserIntent =
            Intent(Intent.ACTION_VIEW, url.toUri()).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
        applicationContext.startActivity(browserIntent)
    }

    private fun readRawFileAsString(
        @RawRes id: Int,
    ): String {
        val inputStream = applicationContext.resources.openRawResource(id)
        val byteArray = ByteArray(inputStream.available())
        inputStream.read(byteArray)

        return String(byteArray)
    }
}
