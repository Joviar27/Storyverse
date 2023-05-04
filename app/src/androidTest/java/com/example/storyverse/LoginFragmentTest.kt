package com.example.storyverse

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.example.storyverse.ui.liststory.ListStoryFragment
import com.example.storyverse.ui.login.LoginFragment
import com.example.storyverse.utils.EspressoIdlingResource
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@MediumTest
class LoginFragmentTest{

    @get:Rule
    val activity = ActivityScenarioRule(MainActivity::class.java)

    @Before
    fun setUp() {
        IdlingRegistry.getInstance().register(EspressoIdlingResource.countingIdlingResource)
    }

    @After
    fun tearDown() {
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.countingIdlingResource)
    }

    @Test
    fun login_success() {
        launchFragmentInContainer<LoginFragment>(themeResId = R.style.Theme_Storyverse)

        onView(withId(R.id.ed_login_password))
            .check(ViewAssertions.matches((ViewMatchers.isDisplayed())))

        onView(withId(R.id.ed_login_email))
            .perform(typeText("tesjovi2@gmail.com"), closeSoftKeyboard());
        onView(withId(R.id.ed_login_password))
            .perform(typeText("12345678"), closeSoftKeyboard());

        onView(withId(R.id.btn_signIn)).perform(click());

        launchFragmentInContainer<ListStoryFragment>(themeResId = R.style.Theme_Storyverse)

        onView(withId(R.id.rv_story))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun logout_success(){
        activity.scenario.onActivity { activity ->
            val listStoryFragment = ListStoryFragment()
            activity.supportFragmentManager.beginTransaction()
                .add(android.R.id.content, listStoryFragment, "")
                .commitNow()
        }

        onView(withId(R.id.logout)).perform(click());

        launchFragmentInContainer<LoginFragment>(themeResId = R.style.Theme_Storyverse)

        onView(withId(R.id.ed_login_email))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }
}