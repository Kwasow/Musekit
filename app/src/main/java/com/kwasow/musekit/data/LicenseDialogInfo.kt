package com.kwasow.musekit.data

import android.content.Context
import androidx.annotation.RawRes
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class LicenseDialogInfo(
    state: State = State.CLOSED,
    name: String = "",
    text: String = ""
) {
    var state by mutableStateOf(state)
    var name by mutableStateOf(name)
    var text by mutableStateOf(text)

    enum class State {
        CLOSED,
        DIALOG_OPEN,
        SUBDIALOG_OPEN,
    }

    fun open(context: Context, name: String, @RawRes file: Int) {
        val inputStream = context.resources.openRawResource(file)
        val byteArray = ByteArray(inputStream.available())
        inputStream.read(byteArray)

        this.name = name
        this.text = String(byteArray)
        this.state = State.SUBDIALOG_OPEN
    }
}
