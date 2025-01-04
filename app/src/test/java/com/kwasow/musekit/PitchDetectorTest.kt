package com.kwasow.musekit

import com.kwasow.musekit.data.Notes
import com.kwasow.musekit.utils.MusekitPitchDetector
import org.junit.Test

class PitchDetectorTest {

    @Test
    fun testEstimationCloseUnderFrequencyUnderMiddle() {
        // G4     - 392Hz
        // middle - 403.65Hz
        // Gis4   - 415.3Hz
        val result = MusekitPitchDetector.findNoteDetails(403.0)
        require(result != null)

        val note = result.first
        assert(note.note == Notes.G)
        assert(note.octave == 4)

        val cents = result.second
        assert(cents > 0)
    }

    @Test
    fun testEstimationCloseUnderFrequencyOverMiddle() {
        // G4     - 392Hz
        // middle - 403.65Hz
        // Gis4   - 415.3Hz
        val result = MusekitPitchDetector.findNoteDetails(404.0)
        require(result != null)

        val note = result.first
        assert(note.note == Notes.GisAs)
        assert(note.octave == 4)

        val cents = result.second
        assert(cents < 0)
    }

    @Test
    fun testEstimationFarUnderFrequencyUnderMiddle() {
        // G2     - 98Hz
        // middle - 100.92Hz
        // Gis2   - 103.83Hz
        val result = MusekitPitchDetector.findNoteDetails(100.0)
        require(result != null)

        val note = result.first
        assert(note.note == Notes.G)
        assert(note.octave == 2)

        val cents = result.second
        assert(cents > 0)
    }

    @Test
    fun testEstimationFarUnderFrequencyOverMiddle() {
        // G2     - 98Hz
        // middle - 100.92Hz
        // Gis2   - 103.83Hz
        val result = MusekitPitchDetector.findNoteDetails(101.0)
        require(result != null)

        val note = result.first
        assert(note.note == Notes.GisAs)
        assert(note.octave == 2)

        val cents = result.second
        assert(cents < 0)
    }

    @Test
    fun testEstimationCloseOverFrequencyUnderMiddle() {
        // Ais4   - 466.16Hz
        // middle - 480.02Hz
        // H4     - 493.88Hz
        val result = MusekitPitchDetector.findNoteDetails(479.0)
        require(result != null)

        val note = result.first
        assert(note.note == Notes.AisB)
        assert(note.octave == 4)

        val cents = result.second
        assert(cents > 0)
    }

    @Test
    fun testEstimationCloseOverFrequencyOverMiddle() {
        // Ais4   - 466.16Hz
        // middle - 480.02Hz
        // H4     - 493.88Hz
        val result = MusekitPitchDetector.findNoteDetails(481.0)
        require(result != null)

        val note = result.first
        assert(note.note == Notes.H)
        assert(note.octave == 4)

        val cents = result.second
        assert(cents < 0)
    }

    @Test
    fun testEstimationFarOverFrequencyUnderMiddle() {
        // Ais6   - 1864.66Hz
        // middle - 1920.1Hz
        // H6     - 1975.53Hz
        val result = MusekitPitchDetector.findNoteDetails(1919.0)
        require(result != null)

        val note = result.first
        assert(note.note == Notes.AisB)
        assert(note.octave == 6)

        val cents = result.second
        assert(cents > 0)
    }

    @Test
    fun testEstimationFarOverFrequencyOverMiddle() {
        // Ais6   - 1864.66Hz
        // middle - 1920.1Hz
        // H6     - 1975.53Hz
        val result = MusekitPitchDetector.findNoteDetails(1921.0)
        require(result != null)

        val note = result.first
        assert(note.note == Notes.H)
        assert(note.octave == 6)

        val cents = result.second
        assert(cents < 0)
    }
}
