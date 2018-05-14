package com.example.android.letsbake;

import android.support.test.espresso.IdlingRegistry;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by Zsolt on 09.05.2018.
 */

public class IdlingResourceRecipeListTest {

    private static final int RECIPE_LIST_POSITION = 3;
    private static final String RECIPE_NAME = "Cheesecake";

    @Rule
    public ActivityTestRule<MainActivity> activityTestRule =
            new ActivityTestRule<>(MainActivity.class);

    private IdlingResource idlingResource;

    @Before
    public void registerIdlingResource() {
        idlingResource = activityTestRule.getActivity().getIdlingResource();
        IdlingRegistry.getInstance().register(idlingResource);
    }

    // Test whether the recipe in the given position matches to the given recipe
    @Test
    public void testRecipeNameAtPosition() {

        // Perform scroll action on the list by RecyclerView in RecipeListFragment
        onView(withId(R.id.rv_recipe_list))
                .perform(RecyclerViewActions.actionOnItemAtPosition(RECIPE_LIST_POSITION, click()));

        // Check if recipe name as displayed is same with our testing recipe name
        onView(withText(RECIPE_NAME))
                .check(matches(isDisplayed()));
    }

    @After
    public void unregisterIdlingResource() {
        if (idlingResource != null) {
            IdlingRegistry.getInstance().unregister(idlingResource);
        }
    }
}
