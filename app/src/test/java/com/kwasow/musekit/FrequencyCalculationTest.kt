package com.kwasow.musekit

import com.kwasow.musekit.data.Note
import com.kwasow.musekit.data.Notes
import junit.framework.TestCase.assertEquals
import org.junit.Test

class FrequencyCalculationTest {
    @Test
    fun testStandardPitchBaseNotesFrequencies() {
        val a6 = Note(note = Notes.A, octave = 6)
        val a5 = Note(note = Notes.A, octave = 5)
        val a4 = Note(note = Notes.A, octave = 4)
        val a3 = Note(note = Notes.A, octave = 3)
        val a2 = Note(note = Notes.A, octave = 2)

        assertEquals(1760.0, a6.getFrequency())
        assertEquals(880.0, a5.getFrequency())
        assertEquals(440.0, a4.getFrequency())
        assertEquals(220.0, a3.getFrequency())
        assertEquals(110.0, a2.getFrequency())
    }

    @Test
    fun testCustomPitchBaseNotesFrequencies() {
        val a6 = Note(note = Notes.A, octave = 6, pitch = 438)
        val a5 = Note(note = Notes.A, octave = 5, pitch = 438)
        val a4 = Note(note = Notes.A, octave = 4, pitch = 438)
        val a3 = Note(note = Notes.A, octave = 3, pitch = 438)
        val a2 = Note(note = Notes.A, octave = 2, pitch = 438)

        assertEquals(1752.0, a6.getFrequency())
        assertEquals(876.0, a5.getFrequency())
        assertEquals(438.0, a4.getFrequency())
        assertEquals(219.0, a3.getFrequency())
        assertEquals(109.5, a2.getFrequency())
    }

    @Test
    fun testStandardPitchLowerNotesFrequencies() {
        val f6 = Note(note = Notes.F, octave = 6)
        val f5 = Note(note = Notes.F, octave = 5)
        val f4 = Note(note = Notes.F, octave = 4)
        val f3 = Note(note = Notes.F, octave = 3)
        val f2 = Note(note = Notes.F, octave = 2)

        assertEquals(1396.91, f6.getFrequency(), 0.1)
        assertEquals(698.46, f5.getFrequency(), 0.1)
        assertEquals(349.23, f4.getFrequency(), 0.1)
        assertEquals(174.61, f3.getFrequency(), 0.1)
        assertEquals(87.31, f2.getFrequency(), 0.1)
    }

    @Test
    fun testStandardPitchHigherNotesFrequencies() {
        val h6 = Note(note = Notes.H, octave = 6)
        val h5 = Note(note = Notes.H, octave = 5)
        val h4 = Note(note = Notes.H, octave = 4)
        val h3 = Note(note = Notes.H, octave = 3)
        val h2 = Note(note = Notes.H, octave = 2)

        assertEquals(1975.53, h6.getFrequency(), 0.1)
        assertEquals(987.77, h5.getFrequency(), 0.1)
        assertEquals(493.88, h4.getFrequency(), 0.1)
        assertEquals(246.94, h3.getFrequency(), 0.1)
        assertEquals(123.47, h2.getFrequency(), 0.1)
    }

    @Test
    fun testCustomPitchLowerNotesFrequencies() {
        val f6 = Note(note = Notes.F, octave = 6, pitch = 438)
        val f5 = Note(note = Notes.F, octave = 5, pitch = 438)
        val f4 = Note(note = Notes.F, octave = 4, pitch = 438)
        val f3 = Note(note = Notes.F, octave = 3, pitch = 438)
        val f2 = Note(note = Notes.F, octave = 2, pitch = 438)

        assertEquals(1390.56, f6.getFrequency(), 0.1)
        assertEquals(695.28, f5.getFrequency(), 0.1)
        assertEquals(347.64, f4.getFrequency(), 0.1)
        assertEquals(173.82, f3.getFrequency(), 0.1)
        assertEquals(86.91, f2.getFrequency(), 0.1)
    }

    @Test
    fun testCustomPitchHigherNotesFrequencies() {
        val h6 = Note(note = Notes.H, octave = 6, pitch = 438)
        val h5 = Note(note = Notes.H, octave = 5, pitch = 438)
        val h4 = Note(note = Notes.H, octave = 4, pitch = 438)
        val h3 = Note(note = Notes.H, octave = 3, pitch = 438)
        val h2 = Note(note = Notes.H, octave = 2, pitch = 438)

        assertEquals(1966.55, h6.getFrequency(), 0.1)
        assertEquals(983.28, h5.getFrequency(), 0.1)
        assertEquals(491.64, h4.getFrequency(), 0.1)
        assertEquals(245.82, h3.getFrequency(), 0.1)
        assertEquals(122.91, h2.getFrequency(), 0.1)
    }
}