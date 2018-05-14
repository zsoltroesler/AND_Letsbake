package com.example.android.letsbake;

import android.app.Activity;
import android.app.Instrumentation;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.example.android.letsbake.models.Recipe;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.intent.Intents.intending;
import static android.support.test.espresso.intent.matcher.ComponentNameMatchers.hasShortClassName;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.intent.matcher.IntentMatchers.isInternal;
import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.core.IsNot.not;

/**
 * Created by Zsolt on 09.05.2018.
 */

@RunWith(AndroidJUnit4.class)
public class DetailsActivityIntentTest {

    private Recipe recipe;

    @Rule
    public IntentsTestRule<MainActivity> intentsTestRule = new IntentsTestRule<>(
            MainActivity.class);


    @Before
    public void stubAllExternalIntents() {
        // By default Espresso Intents does not stub any Intents. Stubbing needs to be setup before
        // every test run. In this case all external Intents will be blocked.
        intending(not(isInternal())).respondWith(new Instrumentation.ActivityResult(Activity.RESULT_OK, null));
    }

    @Test
    public void clickAndSendIntentToDetailsActivity() {
        // Click on the second recipe from the list by RecyclerView in RecipeListFragment
        onView(withId(R.id.rv_recipe_list))
                .perform(actionOnItemAtPosition(1, click()));

        // Stub all Intents to DetailsActivity to return recipe. The Activity
        // is never launched and result is stubbed.
        intending(hasComponent(hasShortClassName(".DetailsActivity")))
                .respondWith(new Instrumentation.ActivityResult(Activity.RESULT_OK,
                        DetailsActivity.createResultData(recipe)));

        // Verify that the Brownies as recipe name is displayed as activity title
        onView(allOf(instanceOf(TextView.class), withParent(isAssignableFrom(Toolbar.class))))
                .check(matches(withText("Brownies")));
    }
}
