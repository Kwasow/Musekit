package com.kwasow.musekit.data

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class LicenseDialogInfo(
    state: State = State.CLOSED,
    name: String = "",
    text: String = "",
) {
    var state by mutableStateOf(state)
    var name by mutableStateOf(name)
    var text by mutableStateOf(text)

    enum class State {
        CLOSED,
        DIALOG_OPEN,
        SUBDIALOG_OPEN,
        LOADING,
    }
}
