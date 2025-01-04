package com.kwasow.musekit.utils

import android.content.Context
import com.kwasow.musekit.data.Preset
import java.io.File

object PresetsManager {
    // ====== Fields
    private const val PRESET_DIR = "/presets/"
    private const val VER_1 = "<version1>"

    // ====== Public methods
    fun savePreset(preset: Preset, context: Context) {
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
            val contents = """
            $VER_1
            ${preset.name}${if (i == 1) "" else " - (${i - 1})"}
            ${preset.semitones}
            ${preset.octave}
            ${preset.pitch}
            """.trimIndent()

            file.writeText(contents)
        }
    }

    fun getPresets(context: Context): List<Preset> {
        val directory = File(context.filesDir, PRESET_DIR)
        val presets = mutableListOf<Preset>()
        directory.listFiles()?.forEach {
            val lines = it.readLines()
            if (lines.size == 5) {
                if (lines[0] == VER_1) {
                    val preset = Preset(
                        name = lines[1],
                        semitones = lines[2].toInt(),
                        octave = lines[3].toInt(),
                        pitch = lines[4].toInt()
                    )
                    presets.add(preset)
                }
            }
        }

        return presets
    }

    fun removePreset(name: String, context: Context) {
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
