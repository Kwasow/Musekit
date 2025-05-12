package com.kwasow.musekit.data

import android.content.Context
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.style.BaselineShift
import com.kwasow.musekit.R
import java.util.Objects
import kotlin.math.max
import kotlin.math.pow
import kotlin.math.roundToInt

class Note(
    pitch: Int,
    var note: Notes,
    octave: Int,
) {
    // ====== Fields
    var pitch: Int = pitch
        set(value) {
            field = max(value, 1)
        }
    var octave: Int = octave
        set(value) {
            field = max(value, 1)
        }

    companion object {
        fun fromCentDiff(
            centDiff: Double,
            pitch: Int = 440,
        ): Note {
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

    fun getSuperscripted(
        context: Context,
        style: NotationStyle,
    ): AnnotatedString {
        val text =
            context.getString(
                R.string.note_placeholder,
                this.getNoteName(style),
                this.octave,
            )
        val annotations = mutableListOf<AnnotatedString.Range<AnnotatedString.Annotation>>()

        text.forEachIndexed { index, c ->
            if (c == '♯' || c == '♭') {
                annotations.add(
                    AnnotatedString.Range(
                        SpanStyle(baselineShift = BaselineShift.Superscript),
                        index,
                        index,
                    ),
                )
            } else if (c.isDigit()) {
                annotations.add(
                    AnnotatedString.Range(
                        SpanStyle(baselineShift = BaselineShift.Subscript),
                        index,
                        index,
                    ),
                )
            }
        }

        return AnnotatedString(
            text = text,
            annotations = annotations,
        )
    }

    // ======= Private methods
    private fun getNoteName(style: NotationStyle): String = note.getName(style)
}
