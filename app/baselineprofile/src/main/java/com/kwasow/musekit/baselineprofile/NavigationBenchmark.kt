package com.kwasow.musekit.baselineprofile

import android.Manifest
import androidx.benchmark.macro.FrameTimingMetric
import androidx.benchmark.macro.StartupMode
import androidx.benchmark.macro.junit4.MacrobenchmarkRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.kwasow.musekit.baselineprofile.extensions.clickOnText
import com.kwasow.musekit.baselineprofile.extensions.waitForTextShown
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class NavigationBenchmark {
    @get:Rule
    val benchmarkRule = MacrobenchmarkRule()

    @Before
    fun setup() {
        InstrumentationRegistry.getInstrumentation().uiAutomation.grantRuntimePermission(
            "com.kwasow.musekit",
            Manifest.permission.RECORD_AUDIO
        )
    }

    @Test
    fun forkToMetronome() = benchmarkRule.measureRepeated(
        packageName = "com.kwasow.musekit",
        metrics = listOf(FrameTimingMetric()),
        iterations = Config.ITERATIONS,
        startupMode = StartupMode.WARM,
        setupBlock = {
            pressHome()
            startActivityAndWait()
        }
    ) {
        clickOnText("Metronome")
        waitForTextShown("Set beat")
    }

    @Test
    fun forkToSettings() = benchmarkRule.measureRepeated(
        packageName = "com.kwasow.musekit",
        metrics = listOf(FrameTimingMetric()),
        iterations = Config.ITERATIONS,
        startupMode = StartupMode.WARM,
        setupBlock = {
            pressHome()
            startActivityAndWait()
        }
    ) {
        clickOnText("Settings")
        waitForTextShown("Musekit")
    }

    @Test
    fun metronomeToSettings() = benchmarkRule.measureRepeated(
        packageName = "com.kwasow.musekit",
        metrics = listOf(FrameTimingMetric()),
        iterations = Config.ITERATIONS,
        startupMode = StartupMode.WARM,
        setupBlock = {
            pressHome()
            startActivityAndWait()
            clickOnText("Metronome")
            waitForTextShown("Set beat")
        }
    ) {
        clickOnText("Settings")
        waitForTextShown("Musekit")
    }

    @Test
    fun settingsToMetronome() = benchmarkRule.measureRepeated(
        packageName = "com.kwasow.musekit",
        metrics = listOf(FrameTimingMetric()),
        iterations = Config.ITERATIONS,
        startupMode = StartupMode.WARM,
        setupBlock = {
            pressHome()
            startActivityAndWait()
            clickOnText("Settings")
            waitForTextShown("Musekit")
        }
    ) {
        clickOnText("Metronome")
        waitForTextShown("Set beat")
    }

    @Test
    fun settingsToFork() = benchmarkRule.measureRepeated(
        packageName = "com.kwasow.musekit",
        metrics = listOf(FrameTimingMetric()),
        iterations = Config.ITERATIONS,
        startupMode = StartupMode.WARM,
        setupBlock = {
            pressHome()
            startActivityAndWait()
            clickOnText("Settings")
            waitForTextShown("Musekit")
        }
    ) {
        clickOnText("Tuning")
        waitForTextShown("Automatic")
    }
}
