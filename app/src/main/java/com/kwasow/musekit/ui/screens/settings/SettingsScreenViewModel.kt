package com.kwasow.musekit.ui.screens.settings

import android.content.Context
import android.content.Intent
import androidx.annotation.RawRes
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kwasow.musekit.data.NotationStyle
import com.kwasow.musekit.managers.PreferencesManager
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SettingsScreenViewModel(
    private val applicationContext: Context,
    private val preferencesManager: PreferencesManager,
) : ViewModel() {
    // ====== Fields
    var notationStyle = preferencesManager.notationStyle.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(),
        null
    )
    var nightMode = preferencesManager.nightMode.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(),
        null
    )

    // ====== Public methods
    fun updateNotationStyle(style: NotationStyle) =
        viewModelScope.launch {
            preferencesManager.setNotationStyle(style)
        }

    fun updateNightMode(mode: Int) =
        viewModelScope.launch {
            preferencesManager.setNightMode(mode)
        }

    fun openGithub() = openUrl("https://github.com/Kwasow/Musekit")

    fun openMastodon() = openUrl("https://mstdn.social/@kwasow")

    fun openWebsite() = openUrl("https://kwasow.pl")

    fun openFile(
        @RawRes id: Int,
    ): String {
        val inputStream = applicationContext.resources.openRawResource(id)
        val byteArray = ByteArray(inputStream.available())
        inputStream.read(byteArray)

        return String(byteArray)
    }

    // ====== Private methods
    private fun openUrl(url: String) {
        val browserIntent =
            Intent(Intent.ACTION_VIEW, url.toUri()).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
        applicationContext.startActivity(browserIntent)
    }
}
