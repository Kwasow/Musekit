package com.kwasow.musekit.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.kwasow.musekit.R

class SoundsAdapter(context: Context, names: List<String>) :
  ArrayAdapter<String>(context, 0, names) {

  override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
    val soundName = getItem(position)
    val root =
      convertView ?: LayoutInflater.from(context).inflate(R.layout.sound_list_item, parent, false)

    val soundTextView = root.findViewById<TextView>(R.id.textSoundName)
    soundTextView.text = soundName

    return root
  }
}