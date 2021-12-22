package com.kwasow.musekit

import kotlin.math.pow
import kotlin.properties.Delegates

class Note {
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
    private var octave: Int

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

        return pitch *
                2.0.pow(octave - 4.0) *
                2.0.pow(semitones / 12.0)
    }

    fun getFrequencyString(): String = String.format("%.2f", getFrequency())

    fun getNoteName(): String = note.noteName

    fun up() {
        if (note.semitones == 11) {
            note = Notes.A
            octave += 1
        } else {
            note = Notes.values().first { it.semitones == note.semitones + 1 }
        }
    }

    fun down() {
        if (note.semitones == 0) {
            if (octave != 1) {
                note = Notes.GisAs
                octave -= 1
            }
        } else {
            note = Notes.values().first { it.semitones == note.semitones - 1 }
        }
    }
}