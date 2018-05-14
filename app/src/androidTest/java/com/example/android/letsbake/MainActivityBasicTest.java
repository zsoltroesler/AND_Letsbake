package com.example.android.letsbake;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.Matchers.instanceOf;

import android.support.v7.widget.Toolbar;
import android.widget.TextView;

/**
 * Created by Zsolt on 09.05.2018.
 * This test demonstrates a user clicking on the recipe list and verifies
 * if it opens the DetailsActivity
 */

@RunWith(AndroidJUnit4.class)
public class MainActivityBasicTest {

    @Rule
    public ActivityTestRule<MainActivity> activityActivityTestRule =
            new ActivityTestRule<MainActivity>(MainActivity.class);

    @Test
    public void clickOnRecipe_OpensDetailsActivity() {

        // Click on the first recipe from the list by RecyclerView in RecipeListFragment
        onView(withId(R.id.rv_recipe_list))
                .perform(actionOnItemAtPosition(0, click()));

        // Verify that the Nutella Pie as recipe name is displayed as activity title
        onView(allOf(instanceOf(TextView.class), withParent(isAssignableFrom(Toolbar.class))))
                .check(matches(withText("Nutella Pie")));
    }
}
