package com.kwasow.musekit

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class MainNavigationTest {

    @get:Rule
    var activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun navigateToTunerTest() {
        // Navigate away from default view
        onView(withId(R.id.action_about)).perform(click())

        // Click back
        onView(withId(R.id.action_fork)).perform(click())
        onView(withId(R.id.noteForkTabs)).check(matches(isDisplayed()))
    }

    @Test
    fun navigateToMetronomeTest() {
        onView(withId(R.id.action_metronome)).perform(click())
        onView(withId(R.id.sliderBeat)).check(matches(isDisplayed()))
    }

    @Test
    fun navigateToSettingsTest() {
        onView(withId(R.id.action_about)).perform(click())
        onView(withId(R.id.imageAppIcon)).check(matches(isDisplayed()))
    }

}