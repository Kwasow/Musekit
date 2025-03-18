package com.kwasow.musekit.dialogs

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.kwasow.musekit.R
import com.kwasow.musekit.models.PresetDialogViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class PresetDeleteDialog(
    val presetName: String,
    val onDelete: () -> Unit = {},
) : DialogFragment() {
    // ====== Fields
    companion object {
        const val TAG = "PresetDeleteDialog"
    }

    private val viewModel by viewModel<PresetDialogViewModel>()

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
                    viewModel.removePreset(presetName)
                    onDelete()
                }
                .create()

        return dialog
    }
}
