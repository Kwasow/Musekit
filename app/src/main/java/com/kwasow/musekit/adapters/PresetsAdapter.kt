package com.kwasow.musekit.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.google.android.material.button.MaterialButton
import com.kwasow.musekit.R

class PresetsAdapter(
    context: Context,
    names: List<String>,
    val onDelete: (String) -> Unit
) :
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
                    onDelete(presetName)
                }
            }
        }

        val presetTextView = root.findViewById<TextView>(R.id.textPresetName)
        presetTextView.text = presetName

        return root
    }
}
