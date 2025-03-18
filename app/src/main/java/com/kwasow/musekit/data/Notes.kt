package com.kwasow.musekit.data

// Enum classes named using the German notation where
//  - the "is" suffix is added to a note with a sharp
//  - the "as" suffix is added to a note with a flat
//  - the note one half step below C is known as H; H flat is B
enum class Notes(val semitones: Int) {
    A(0),
    AisB(1),
    H(2),
    C(3),
    CisDes(4),
    D(5),
    DisEs(6),
    E(7),
    F(8),
    FisGes(9),
    G(10),
    GisAs(11),
    ;

    companion object {
        fun fromSemitones(semitones: Int): Notes {
            return when (semitones) {
                1 -> AisB
                2 -> H
                3 -> C
                4 -> CisDes
                5 -> D
                6 -> DisEs
                7 -> E
                8 -> F
                9 -> FisGes
                10 -> G
                11 -> GisAs
                else -> A
            }
        }
    }

    fun getName(style: NotationStyle): String = style.noteNames[semitones]
}
