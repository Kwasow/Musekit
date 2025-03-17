package com.kwasow.musekit.dialogs

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.text.isDigitsOnly
import androidx.fragment.app.DialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.kwasow.musekit.R
import com.kwasow.musekit.databinding.DialogSetBeatBinding
import com.kwasow.musekit.managers.PreferencesManager

class SetBeatDialog(val onSave: (Int) -> Unit) : DialogFragment() {
    // ====== Fields
    companion object {
        const val TAG = "SetBeatDialog"
    }

    private lateinit var dialog: AlertDialog
    private lateinit var beatText: TextView

    // ====== Interface methods
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val binding = DialogSetBeatBinding.inflate(layoutInflater)
        beatText = binding.beatValue

        dialog =
            MaterialAlertDialogBuilder(requireContext())
                .setTitle(R.string.set_beat)
                .setView(binding.root)
                .setNeutralButton(R.string.cancel) { _, _ -> }
                .setPositiveButton(R.string.save) { _, _ ->
                    onSave(beatText.text.toString().toInt())
                }
                .create()

        // Set starting value
        beatText.text = PreferencesManager.metronomeBPM.toString()

        // Disable "Save" button if text field is empty
        beatText.addTextChangedListener(
            object : TextWatcher {
                override fun beforeTextChanged(
                    p0: CharSequence?,
                    p1: Int,
                    p2: Int,
                    p3: Int,
                ) {}

                override fun onTextChanged(
                    p0: CharSequence?,
                    p1: Int,
                    p2: Int,
                    p3: Int,
                ) {}

                override fun afterTextChanged(p0: Editable?) {
                    dialog.getButton(DialogInterface.BUTTON_POSITIVE)?.isEnabled =
                        !p0.isNullOrBlank() && p0.isDigitsOnly()
                }
            },
        )

        return dialog
    }
}
