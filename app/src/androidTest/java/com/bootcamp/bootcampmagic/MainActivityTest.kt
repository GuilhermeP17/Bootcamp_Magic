package com.bootcamp.bootcampmagic

import android.app.usage.NetworkStatsManager
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.wifi.WifiManager
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import com.bootcamp.bootcampmagic.ui.MainActivity

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Rule

@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    @get:Rule
    val activityRule = ActivityTestRule(MainActivity::class.java)

    private fun turnOnConnetion() {
        val wifi: WifiManager =
            activityRule.activity.getSystemService(Context.WIFI_SERVICE) as WifiManager

        wifi.setWifiEnabled(false)
    }

    private fun turnOffConnection() {
        val wifi: WifiManager =
            activityRule.activity.getSystemService(Context.WIFI_SERVICE) as WifiManager
        wifi.setWifiEnabled(true)
    }

    @Test
    fun givenInitialState_shouldDisplayTabLayout() {
        Espresso.onView(ViewMatchers.withId(R.id.tab_set_favorites))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun givenSearchCard_shouldDisplayNewRecyclerView() {
        Espresso.onView(ViewMatchers.withId(R.id.search_cards))
            .perform(ViewActions.typeText("Archangel"))

        Thread.sleep(5000)

        Espresso.onView(ViewMatchers.withId(R.id.recycler_cards))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun giveCancelButtonAction_shouldClearEditText() {
        Espresso.onView(ViewMatchers.withId(R.id.btn_cancelar))
            .perform(ViewActions.click())

        Espresso.onView(ViewMatchers.withId(R.id.loadingContent))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        Espresso.onView(ViewMatchers.withId(R.id.search_cards))
            .check(ViewAssertions.matches(ViewMatchers.withText("")))

    }

    /*@Test
    fun givenNoNetworkState_whenScrolling_shouldDisplayErrorMessage() {
        turnOffWifi()

        Espresso.onView(ViewMatchers.withId(R.id.recycler_cards))
            .perform(ViewActions.scrollTo(), ViewActions.click())

        turnOnWifi()
    }*/

    @Test
    fun givenNoNetworkState_whenTryingSearchCard_shouldDisplayErrorMessage() {
        turnOffConnection()

        Espresso.onView(ViewMatchers.withId(R.id.search_cards))
            .perform(ViewActions.typeText("Archangel"))

        Espresso.onView(ViewMatchers.withText(R.string.generic_network_error))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        turnOnConnetion()
    }

}
