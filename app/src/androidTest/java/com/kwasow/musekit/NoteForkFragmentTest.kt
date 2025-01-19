package com.kwasow.musekit

import android.content.Context
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.kwasow.musekit.fragments.NoteForkFragment
import com.kwasow.musekit.utils.MusekitPreferences
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class NoteForkFragmentTest {
    private lateinit var context: Context

    @Before
    fun setup() {
        context = getApplicationContext<MainApplication>()

        MusekitPreferences.init(context)
    }

    @Test
    fun automaticTabTest() {
        launchFragmentInContainer<NoteForkFragment>()

        onView(withText(context.getString(R.string.note_fork_automatic))).perform(click())
        onView(withId(R.id.tunerView)).check(matches(isDisplayed()))
    }

    @Test
    fun manualTabTest() {
        launchFragmentInContainer<NoteForkFragment>()

        onView(withText(context.getString(R.string.note_fork_manual))).perform(click())
        onView(withId(R.id.presetsPicker)).check(matches(isDisplayed()))
    }

    @Test
    fun rememberPositionAutomaticTest() {
        MusekitPreferences.noteForkMode = 0
        launchFragmentInContainer<NoteForkFragment>()

        onView(withId(R.id.tunerView)).check(matches(isDisplayed()))
    }

    @Test
    fun rememberPositionManualTest() {
        MusekitPreferences.noteForkMode = 1
        launchFragmentInContainer<NoteForkFragment>()

        onView(withId(R.id.presetsPicker)).check(matches(isDisplayed()))
    }

}