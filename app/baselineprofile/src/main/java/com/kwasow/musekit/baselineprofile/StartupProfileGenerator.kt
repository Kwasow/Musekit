package com.kwasow.musekit.baselineprofile

import android.Manifest
import androidx.benchmark.macro.junit4.BaselineProfileRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class StartupProfileGenerator {

    @get:Rule
    val rule = BaselineProfileRule()

    @Test
    fun generatePermissionGranted() {
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
        }
    }

    @Test
    fun generatePermissionNotGranted() {
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
        }
    }
}
