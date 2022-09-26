package com.kwasow.musekit.data

import kotlin.math.pow
import kotlin.properties.Delegates

class Note {
    var pitch by Delegates.notNull<Int>()
    var note: Notes
    var octave: Int

    constructor() : this(440, Notes.A, 4)

    constructor(pitch: Int) : this(pitch, Notes.A, 4)

    constructor(note: Notes, octave: Int) : this(440, note, octave)

    constructor(pitch: Int, note: Notes, octave: Int) {
        this.pitch = pitch
        this.note = note
        this.octave = octave
    }

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
            else -> note = Notes.values().first { it.semitones == note.semitones + 1 }
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
            else -> note = Notes.values().first { it.semitones == note.semitones - 1 }
        }
    }
}
