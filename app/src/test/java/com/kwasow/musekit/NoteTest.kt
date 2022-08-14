package com.kwasow.musekit

import com.kwasow.musekit.data.Note
import com.kwasow.musekit.data.Notes
import org.junit.Test
import kotlin.math.abs
import kotlin.math.pow

class NoteTest {
    companion object {
        const val DOUBLE_EPSILON = 0.1
    }

    private fun equalsWithEpsilon(a: Double, b: Double): Boolean {
        return abs(a - b) <= DOUBLE_EPSILON
    }

    @Test
    fun testConstructor() {
        assert(Note().getFrequency() == 440.0)
        assert(Note(420).getFrequency() == 420.0)
        assert(Note(Notes.A, 3).getFrequency() == 220.0)
        assert(Note(435, Notes.A, 5).getFrequency() == 870.0)
    }

    @Test
    fun testNoteName() {
        val note = Note();
        assert(note.getNoteName() == "A");

        note.up();
        note.up();
        assert(note.getNoteName() == "B");

        note.up();
        assert(note.getNoteName() == "C");

        note.up();
        assert(note.getNoteName() == "C♯/D♭")
    }

    @Test
    fun testFrequencyCalculation() {
        val note1 = Note()
        repeat(12) {
            note1.up()
            assert(equalsWithEpsilon(note1.getFrequency(), 440.0 * 2.0.pow((it + 1) / 12.0))
            )
        }

        val note2 = Note()
        repeat(12) {
            note2.down()
            assert(equalsWithEpsilon(note2.getFrequency(), 440.0 * 2.0.pow(-(it + 1) / 12.0)))
        }
    }

    @Test
    fun testLowerLimit() {
        val note = Note();
        repeat(12 * 4 + 7) {
            note.down();
        }
        assert(note.getNoteName() == "C")
        assert(equalsWithEpsilon(note.getFrequency(), 32.7))
        note.down()
        assert(note.getNoteName() == "C")
        assert(equalsWithEpsilon(note.getFrequency(), 32.7))
        note.up();
        assert(note.getNoteName() == "C♯/D♭")
        assert(equalsWithEpsilon(note.getFrequency(), 34.65))
    }

}