package com.example.headachediary.presentation.view.auth;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasErrorText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.MutableLiveData;
import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;

import com.example.headachediary.R;
import com.example.headachediary.domain.auth.UserCreate;
import com.example.headachediary.domain.auth.UserResponse;
import com.example.headachediary.presentation.viewmodel.auth.RegisterViewModel;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class RegisterFragmentTest {

    @Rule
    public ActivityScenarioRule<AuthActivity> activityScenarioRule = new ActivityScenarioRule<>(AuthActivity.class);


    private void navigateToRegister() {
        onView(withId(R.id.registerButton)).perform(click());
    }
    @Test
    public void testEmptyEmail() {
        navigateToRegister();
        onView(withId(R.id.emailEditText)).perform(click());
        onView(withId(R.id.emailEditText)).perform(typeText(""), closeSoftKeyboard());
        onView(withId(R.id.registerButton)).perform(click());
        onView(withId(R.id.emailEditText)).check(matches(hasErrorText("Введите email")));
    }

    @Test
    public void testSuccessfulRegistration() {

        RegisterViewModel mockViewModel = mock(RegisterViewModel.class);
        when(mockViewModel.getAuthState()).thenReturn(new MutableLiveData<>());


        activityScenarioRule.getScenario().onActivity(activity -> {
            FragmentManager fm = activity.getSupportFragmentManager();
            RegisterFragment fragment = (RegisterFragment) fm.findFragmentByTag("register_fragment");
            fragment.setViewModel(mockViewModel);
        });

        navigateToRegister();


        onView(withId(R.id.nameEditText)).perform(typeText("Test User"), closeSoftKeyboard());
        onView(withId(R.id.emailEditText)).perform(typeText("test@example.com"), closeSoftKeyboard());
        onView(withId(R.id.passwordEditText)).perform(typeText("password123"), closeSoftKeyboard());
        onView(withId(R.id.confirmPasswordEditText)).perform(typeText("password123"), closeSoftKeyboard());

        onView(withId(R.id.registerButton)).perform(click());


        verify(mockViewModel).registerUser(any(UserCreate.class));
    }
}