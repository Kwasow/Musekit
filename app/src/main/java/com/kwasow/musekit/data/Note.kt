package com.kwasow.musekit.data

import android.content.Context
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.RelativeSizeSpan
import android.text.style.SubscriptSpan
import android.text.style.SuperscriptSpan
import com.kwasow.musekit.R
import java.util.Objects
import kotlin.math.pow
import kotlin.math.roundToInt

class Note {

    // ====== Fields
    var pitch: Int
    var note: Notes
    var octave: Int

    companion object {
        fun fromCentDiff(centDiff: Double, pitch: Int = 440): Note {
            val semitones = (centDiff / 100).roundToInt()

            val note = Note(pitch)
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

    // ====== Interface methods
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Note) return false

        return this.pitch == other.pitch &&
            this.note == other.note &&
            this.octave == other.octave
    }

    override fun hashCode(): Int {
        return Objects.hash(pitch, note, octave)
    }

    override fun toString(): String {
        return note.name + octave
    }

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

        text.forEachIndexed { index, c ->
            if (c == '♯' || c == '♭') {
                spannableStringBuilder.setSpan(
                    SuperscriptSpan(),
                    index,
                    index + 1,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            } else if (c.isDigit()) {
                spannableStringBuilder.setSpan(
                    RelativeSizeSpan(0.5f),
                    index,
                    index + 1,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                spannableStringBuilder.setSpan(
                    SubscriptSpan(),
                    index,
                    index + 1,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }
        }

        return spannableStringBuilder
    }
}
