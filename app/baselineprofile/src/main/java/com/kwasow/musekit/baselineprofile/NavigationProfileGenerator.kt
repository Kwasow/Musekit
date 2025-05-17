package com.kwasow.musekit.baselineprofile

import android.Manifest
import androidx.benchmark.macro.junit4.BaselineProfileRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry
import com.kwasow.musekit.baselineprofile.extensions.clickOnText
import com.kwasow.musekit.baselineprofile.extensions.waitForTextShown
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class NavigationProfileGenerator {

    @get:Rule
    val rule = BaselineProfileRule()

    @Test
    fun generateForkToMetronomeNavigation() {
        rule.collect(
            packageName = InstrumentationRegistry.getArguments().getString("targetAppId")
                ?: throw Exception("targetAppId not passed as instrumentation runner arg"),
            includeInStartupProfile = true
        ) {
            InstrumentationRegistry.getInstrumentation().uiAutomation.grantRuntimePermission(
                "com.kwasow.musekit",
                Manifest.permission.RECORD_AUDIO
            )

            pressHome()
            startActivityAndWait()

            waitForTextShown("Metronome")
            clickOnText("Metronome")
            waitForTextShown("Set beat")
        }
    }

    @Test
    fun generateForkToSettingsNavigation() {
        rule.collect(
            packageName = InstrumentationRegistry.getArguments().getString("targetAppId")
                ?: throw Exception("targetAppId not passed as instrumentation runner arg"),
            includeInStartupProfile = true
        ) {
            InstrumentationRegistry.getInstrumentation().uiAutomation.revokeRuntimePermission(
                "com.kwasow.musekit",
                Manifest.permission.RECORD_AUDIO
            )

            pressHome()
            startActivityAndWait()

            waitForTextShown("Settings")
            clickOnText("Settings")
            waitForTextShown("Musekit")
        }
    }
}
