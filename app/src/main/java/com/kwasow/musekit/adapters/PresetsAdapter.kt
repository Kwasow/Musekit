package com.kwasow.musekit.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.google.android.material.button.MaterialButton
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.kwasow.musekit.R
import com.kwasow.musekit.utils.PresetsManager

class PresetsAdapter(context: Context, names: List<String>) :
    ArrayAdapter<String>(context, 0, names) {

    // ====== Interface methods
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val presetName = getItem(position)
        val root =
            convertView ?: LayoutInflater.from(context).inflate(R.layout.preset_list_item, parent, false)

        val button = root.findViewById<MaterialButton>(R.id.buttonDeletePreset)
        button.tag = position
        if (position == 0) {
            button.isEnabled = false
            button.visibility = View.GONE
        } else {
            button.setOnClickListener {
                if (presetName != null) {
                    showDeleteDialog(presetName)
                }
            }
        }

        val presetTextView = root.findViewById<TextView>(R.id.textPresetName)
        presetTextView.text = presetName

        return root
    }

    // ====== Private methods
    private fun showDeleteDialog(presetName: String) {
        MaterialAlertDialogBuilder(context)
            .setTitle(R.string.delete_preset)
            .setMessage(context.getString(R.string.warning_preset_deletion, presetName))
            .setIcon(R.drawable.ic_delete)
            .setNeutralButton(R.string.cancel) { _, _ -> }
            .setPositiveButton(R.string.delete) { _, _ ->
                PresetsManager.removePreset(presetName, context)
                this.remove(presetName)
            }
            .show()
    }
}
