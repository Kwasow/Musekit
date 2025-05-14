package com.kwasow.musekit.macrobenchmark

import androidx.benchmark.macro.FrameTimingMetric
import androidx.benchmark.macro.StartupMode
import androidx.benchmark.macro.junit4.MacrobenchmarkRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.kwasow.musekit.macrobenchmark.extensions.clickOnText
import com.kwasow.musekit.macrobenchmark.extensions.waitForTextShown
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class NavigationBenchmark {
    @get:Rule
    val benchmarkRule = MacrobenchmarkRule()

    @Test
    fun forkToMetronome() = benchmarkRule.measureRepeated(
        packageName = "com.kwasow.musekit",
        metrics = listOf(FrameTimingMetric()),
        iterations = 5,
        startupMode = StartupMode.COLD,
        setupBlock = {
            pressHome()
            startActivityAndWait()

            waitForTextShown("Metronome")
        }
    ) {
        clickOnText("Metronome")
        waitForTextShown("Set beat")
    }
}
