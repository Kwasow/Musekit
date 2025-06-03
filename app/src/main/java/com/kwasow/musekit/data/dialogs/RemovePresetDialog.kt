package com.kwasow.musekit.data.dialogs

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class RemovePresetDialog(
    state: State = State.CLOSED,
    removePresetName: String = "",
) {
    // ====== Fields
    var state by mutableStateOf(state)
    var removePresetName by mutableStateOf(removePresetName)

    enum class State {
        CLOSED,
        SAVE_PRESET,
        REMOVE_PRESET,
    }

    // ====== Public methods
    fun openSave() {
        state = State.SAVE_PRESET
    }

    fun openRemove(presetName: String) {
        removePresetName = presetName
        state = State.REMOVE_PRESET
    }

    fun close() {
        state = State.CLOSED
    }
}
