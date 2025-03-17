package com.kwasow.musekit.dialogs

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.kwasow.musekit.R
import com.kwasow.musekit.utils.PresetsManager

class PresetDeleteDialog(
    val presetName: String,
    val onDelete: () -> Unit = {},
) : DialogFragment() {
    // ====== Fields
    companion object {
        const val TAG = "PresetDeleteDialog"
    }

    // ====== Interface methods
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val message = requireContext().getString(R.string.warning_preset_deletion, presetName)

        val dialog =
            MaterialAlertDialogBuilder(requireContext())
                .setTitle(R.string.delete_preset)
                .setMessage(message)
                .setIcon(R.drawable.ic_delete)
                .setNeutralButton(R.string.cancel) { _, _ -> }
                .setPositiveButton(R.string.delete) { _, _ ->
                    PresetsManager.removePreset(presetName, requireContext())
                    onDelete()
                }
                .create()

        return dialog
    }
}
