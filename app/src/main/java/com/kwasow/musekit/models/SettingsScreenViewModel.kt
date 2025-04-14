package com.kwasow.musekit.models

import android.content.Context
import android.content.Intent
import androidx.annotation.RawRes
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import com.kwasow.musekit.data.NotationStyle
import com.kwasow.musekit.managers.PreferencesManager

class SettingsScreenViewModel(
    private val applicationContext: Context,
    private val preferencesManager: PreferencesManager,
) : ViewModel() {
    // ====== Fields
    var showLicensesDialog by mutableStateOf(false)
    var currentLicenseName by mutableStateOf<String?>(null)
    var currentLicenseText by mutableStateOf<String?>(null)

    // ====== Public methods
    fun setNotationStyle(style: NotationStyle) = preferencesManager.setNotationStyle(style)

    fun getNotationStyle(): NotationStyle = preferencesManager.getNotationStyle()

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
