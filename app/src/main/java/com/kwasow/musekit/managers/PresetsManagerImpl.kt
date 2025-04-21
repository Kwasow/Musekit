package com.kwasow.musekit.managers

import android.content.Context
import com.kwasow.musekit.data.Preset
import java.io.File

class PresetsManagerImpl(
    private val context: Context,
) : PresetsManager {
    // ====== Fields
    companion object {
        private const val PRESET_DIR = "/presets/"
        private const val VER_1 = "<version1>"
    }

    // ====== Public methods
    override fun savePreset(preset: Preset) {
        // TODO: Someone could name their preset "Default"
        if (preset.name.isNotBlank()) {
            var filename = toFileName(preset.name) + 0
            val directory = File(context.filesDir, PRESET_DIR)

            if (!directory.exists()) {
                directory.mkdir()
            }

            var i = 1
            while (File(directory, filename).exists()) {
                filename = filename.dropLast(1) + i
                i++
            }

            val file = File(directory, filename)
            val contents =
                """
                $VER_1
                ${preset.name}${if (i == 1) "" else " - (${i - 1})"}
                ${preset.semitones}
                ${preset.octave}
                ${preset.pitch}
                """.trimIndent()

            file.writeText(contents)
        }
    }

    override fun getPresets(): List<Preset> {
        val directory = File(context.filesDir, PRESET_DIR)
        val presets = mutableListOf<Preset>()
        directory.listFiles()?.forEach {
            val lines = it.readLines()
            if (lines.size == 5) {
                if (lines[0] == VER_1) {
                    val preset =
                        Preset(
                            name = lines[1],
                            semitones = lines[2].toInt(),
                            octave = lines[3].toInt(),
                            pitch = lines[4].toInt(),
                        )
                    presets.add(preset)
                }
            }
        }

        return presets
    }

    override fun removePreset(name: String) {
        if (name.isNotBlank()) {
            val directory = File(context.filesDir, PRESET_DIR)

            directory.listFiles()?.forEach {
                if (it.isFile) {
                    val nameFromFile = it.readLines()[1]
                    if (name == nameFromFile) {
                        it.delete()
                        return@forEach
                    }
                }
            }
        }
    }

    // ====== Private methods
    private fun toFileName(name: String): String {
        return name
            .filter { !isIllegalCharacter(it) }
    }

    private fun isIllegalCharacter(c: Char): Boolean {
        return when (c) {
            '"' -> true
            '*' -> true
            '/' -> true
            ':' -> true
            '<' -> true
            '>' -> true
            '?' -> true
            '\\' -> true
            '|' -> true
            else -> false
        }
    }
}
