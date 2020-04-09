package com.bootcamp.bootcampmagic

import androidx.test.espresso.Espresso
import androidx.test.espresso.ViewAssertion
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import com.bootcamp.bootcampmagic.ui.MainActivity
import com.google.android.material.tabs.TabLayout

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Rule

@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    @get:Rule
    val activityRule = ActivityTestRule(MainActivity::class.java)

    @Test
    fun givenInitialState_shouldDisplayTabLayout(){
        Espresso.onView(ViewMatchers.withId(R.id.tab_set_favorites))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }
}
