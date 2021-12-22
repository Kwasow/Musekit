package com.kwasow.musekit.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.kwasow.musekit.Note
import com.kwasow.musekit.databinding.FragmentNoteForkBinding

class NoteForkFragment : Fragment() {
    private lateinit var binding: FragmentNoteForkBinding
    private lateinit var note: Note

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNoteForkBinding.inflate(inflater)
        return binding.root
    }

    override fun onStart() {
        super.onStart()

        note = Note()

        binding.pitch.text = note.pitch.toString()
        binding.frequency.text = note.getFrequencyString()
        binding.note.text = note.getNoteName()

        binding.buttonUp.setOnClickListener {
            note.up()
            binding.frequency.text = note.getFrequencyString()
            binding.note.text = note.getNoteName()
        }

        binding.buttonDown.setOnClickListener {
            note.down()
            binding.frequency.text = note.getFrequencyString()
            binding.note.text = note.getNoteName()
        }
    }
}