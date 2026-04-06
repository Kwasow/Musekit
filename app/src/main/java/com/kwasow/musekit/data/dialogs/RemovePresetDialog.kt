package com.kwasow.musekit.data.dialogs

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.kwasow.musekit.room.Preset

class RemovePresetDialog(
    state: State = State.CLOSED,
    removePreset: Preset? = null,
) {
    // ====== Fields
    var state by mutableStateOf(state)
    var removePreset by mutableStateOf(removePreset)

    enum class State {
        CLOSED,
        SAVE_PRESET,
        REMOVE_PRESET,
    }

    // ====== Public methods
    fun openSave() {
        state = State.SAVE_PRESET
    }

    fun openRemove(preset: Preset) {
        removePreset = preset
        state = State.REMOVE_PRESET
    }

    fun close() {
        state = State.CLOSED
    }
}
