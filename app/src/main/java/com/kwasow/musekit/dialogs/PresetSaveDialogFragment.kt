package com.kwasow.musekit.dialogs

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.kwasow.musekit.R
import com.kwasow.musekit.data.Note
import com.kwasow.musekit.data.Preset
import com.kwasow.musekit.databinding.DialogSavePresetBinding
import com.kwasow.musekit.utils.PresetsManager

class PresetSaveDialogFragment(
    val note: Note,
    val onSave: () -> Unit = {}
) : DialogFragment() {

    // ====== Fields
    companion object {
        const val TAG = "PresetSaveDialogFragment"
    }

    private lateinit var dialog: AlertDialog
    private lateinit var presetNameText: TextView

    // ====== Interface methods
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val binding = DialogSavePresetBinding.inflate(layoutInflater)
        presetNameText = binding.presetName

        dialog = MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.save_preset)
            .setView(binding.root)
            .setNeutralButton(R.string.cancel) { _, _ -> }
            .setPositiveButton(R.string.save) { _, _ ->
                // Get preset details
                val preset = Preset(
                    name = binding.presetName.text.toString(),
                    semitones = note.note.semitones,
                    octave = note.octave,
                    pitch = note.pitch
                )

                // Save preset
                PresetsManager.savePreset(preset, requireContext())
                onSave()
            }
            .create()

        // Disable "Save" button if text field is empty
        presetNameText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(p0: Editable?) {
                dialog.getButton(DialogInterface.BUTTON_POSITIVE)?.isEnabled =
                    !p0.isNullOrBlank()
            }
        })

        return dialog
    }

    override fun onStart() {
        super.onStart()

        dialog.getButton(DialogInterface.BUTTON_POSITIVE)?.isEnabled =
            presetNameText.text.isNotBlank()
    }
}
