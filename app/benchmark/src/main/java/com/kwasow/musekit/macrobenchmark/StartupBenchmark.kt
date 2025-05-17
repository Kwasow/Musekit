package com.kwasow.musekit.macrobenchmark

import android.Manifest
import androidx.benchmark.macro.StartupMode
import androidx.benchmark.macro.StartupTimingMetric
import androidx.benchmark.macro.junit4.MacrobenchmarkRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class StartupBenchmark {
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
    fun coldStart() = benchmarkRule.measureRepeated(
        packageName = "com.kwasow.musekit",
        metrics = listOf(StartupTimingMetric()),
        iterations = 5,
        startupMode = StartupMode.COLD,
        setupBlock = {
            pressHome()
        }
    ) {
        startActivityAndWait()
    }
}
