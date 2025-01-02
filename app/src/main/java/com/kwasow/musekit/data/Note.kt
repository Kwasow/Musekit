package com.kwasow.musekit.data

import android.content.Context
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.SuperscriptSpan
import com.kwasow.musekit.R
import kotlin.math.pow
import kotlin.math.roundToInt

class Note {
    // ====== Fields
    var pitch: Int
    var note: Notes
    var octave: Int

    companion object {
        fun fromCentDiff(centDiff: Double): Note {
            val semitones = (centDiff / 100).roundToInt()

            val note = Note()
            if (semitones < 0) {
                repeat(-semitones) { note.down() }
            } else {
                repeat(semitones) { note.up() }
            }

            return note
        }
    }

    // ====== Constructors
    constructor() : this(440, Notes.A, 4)

    constructor(pitch: Int) : this(pitch, Notes.A, 4)

    constructor(note: Notes) : this(440, note, 4)

    constructor(note: Notes, octave: Int) : this(440, note, octave)

    constructor(pitch: Int, note: Notes, octave: Int) {
        this.pitch = pitch
        this.note = note
        this.octave = octave
    }

    // Copy constructor
    constructor(note: Note) : this(note.pitch, note.note, note.octave)

    // ====== Public methods
    fun getFrequency(): Double {
        val semitones = note.semitones
        val octavePower =
            if (note.semitones >= 3) {
                octave - 5
            } else {
                octave - 4
            }

        return pitch *
            2.0.pow(octavePower) *
            2.0.pow(semitones / 12.0)
    }

    fun getNoteName(): String = note.noteName

    fun up() {
        when (note) {
            Notes.GisAs -> note = Notes.A
            Notes.H -> {
                note = Notes.C
                octave += 1
            }
            else -> note = Notes.entries.first { it.semitones == note.semitones + 1 }
        }
    }

    fun down() {
        when (note) {
            Notes.A -> note = Notes.GisAs
            Notes.C -> {
                if (octave != 1) {
                    note = Notes.H
                    octave -= 1
                }
            }
            else -> note = Notes.entries.first { it.semitones == note.semitones - 1 }
        }
    }

    fun getSuperscripted(context: Context): SpannableStringBuilder {
        val text = context.getString(R.string.note_placeholder, this.getNoteName(), this.octave)
        val spannableStringBuilder = SpannableStringBuilder(text)

        if (text.length == 6) {
            spannableStringBuilder.setSpan(
                SuperscriptSpan(),
                1,
                2,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            spannableStringBuilder.setSpan(
                SuperscriptSpan(),
                4,
                5,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }

        return spannableStringBuilder
    }

    override fun toString(): String {
        return note.name + octave
    }
}
