package com.kwasow.musekit.data

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