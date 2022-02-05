package com.kwasow.musekit

import kotlin.math.pow
import kotlin.properties.Delegates

class Note {
    // Enum classes named using a polish convention where
    //  - the "is" suffix is added to a note with a sharp
    //  - the "as" suffix is added to a note with a flat
    //  - the note one half step below C is known as H; H flat is B
    enum class Notes(val semitones: Int, val noteName: String) {
        A(0, "A"),
        AisB(1, "A♯/B♭"),
        H(2, "B"),
        C(3, "C"),
        CisDes(4, "C♯/D♭"),
        D(5, "D"),
        DisEs(6, "D♯/E♭"),
        E(7, "E"),
        F(8, "F"),
        FisGes(9, "F♯/G♭"),
        G(10, "G"),
        GisAs(11, "G♯/A♭")
    }

    var pitch by Delegates.notNull<Int>()
    private var note: Notes
    var octave: Int

    constructor() : this(440, Notes.A, 4)

    constructor(pitch: Int) : this(pitch, Notes.A, 4)

    constructor(note: Notes) : this(440, note, 4)

    constructor(pitch: Int, note: Notes, octave: Int) {
        this.pitch = pitch
        this.note = note
        this.octave = octave
    }

    fun getFrequency(): Double {
        val semitones = note.semitones
        val octavePower =
            if (note.semitones >= 3) { octave - 5 }
            else { octave - 4 }

        return pitch *
                2.0.pow(octavePower) *
                2.0.pow(semitones / 12.0)
    }

    fun getFrequencyString(): String = String.format("%.2f", getFrequency())

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