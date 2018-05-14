package com.example.android.letsbake;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.VisibleForTesting;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.example.android.letsbake.fragments.RecipeDetailsFragment;
import com.example.android.letsbake.fragments.RecipeStepsFragment;
import com.example.android.letsbake.models.Recipe;
import com.example.android.letsbake.models.Step;

import java.util.ArrayList;

import butterknife.BindBool;
import butterknife.ButterKnife;

import static com.example.android.letsbake.StepsActivity.CURRENT_STEP;
import static com.example.android.letsbake.StepsActivity.CURRENT_STEP_ID;
import static com.example.android.letsbake.fragments.RecipeDetailsFragment.STEP_RECIPE_KEY;
import static com.example.android.letsbake.fragments.RecipeListFragment.DETAILS_RECIPE_KEY;

/**
 * Created by Zsolt on 09.04.2018.
 */

public class DetailsActivity extends AppCompatActivity implements RecipeDetailsFragment.OnVideoStepClickListener {

    private Recipe recipe;
    private ArrayList<Step> stepList;
    private FragmentManager fragmentManager;
    private Step step;
    private int stepId;

    // Track whether to display a two-pane or single-pane UI
    // A single-pane display refers to phone screens, and two-pane to larger tablet screens
    @BindBool(R.bool.two_pane_layout)
    boolean twoPaneLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        ButterKnife.bind(this);

        Bundle data = getIntent().getExtras();
        recipe = data.getParcelable(DETAILS_RECIPE_KEY);
        stepList = recipe.getRecipeStepList();

        fragmentManager = getSupportFragmentManager();

        if (savedInstanceState != null){
            step = savedInstanceState.getParcelable(CURRENT_STEP);
            stepId = savedInstanceState.getInt(CURRENT_STEP_ID);
        } else {
            if (twoPaneLayout) {

                RecipeDetailsFragment recipeDetailsFragment = new RecipeDetailsFragment();
                data.putParcelable(DETAILS_RECIPE_KEY, recipe);
                recipeDetailsFragment.setArguments(data);
                fragmentManager.beginTransaction()
                        .replace(R.id.container_recipe_details, recipeDetailsFragment)
                        .commit();

                step = stepList.get(0);
                stepId = step.getStepId();

                Bundle bundleStep = new Bundle();
                bundleStep.putParcelable(STEP_RECIPE_KEY, step);
                RecipeStepsFragment recipeStepsFragment = new RecipeStepsFragment();
                recipeStepsFragment.setArguments(bundleStep);
                fragmentManager.beginTransaction()
                        .replace(R.id.container_recipe_steps_two_pane, recipeStepsFragment)
                        .commit();
            } else {
                RecipeDetailsFragment recipeDetailsFragment = new RecipeDetailsFragment();
                recipeDetailsFragment.setArguments(data);
                fragmentManager.beginTransaction()
                        .replace(R.id.container_recipe_details, recipeDetailsFragment)
                        .commit();
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(CURRENT_STEP, step);
        outState.putInt(CURRENT_STEP_ID, stepId);
    }

    @Override
    public void onVideoSelected(int position) {
        step = stepList.get(position);

        if (twoPaneLayout) {
            Bundle bundleVideo = new Bundle();
            bundleVideo.putParcelable(STEP_RECIPE_KEY, step);
            RecipeStepsFragment recipeStepsFragment = new RecipeStepsFragment();
            recipeStepsFragment.setArguments(bundleVideo);
            fragmentManager.beginTransaction()
                    .replace(R.id.container_recipe_steps_two_pane, recipeStepsFragment)
                    .commit();

        } else {
            Intent stepIntent = new Intent(this, StepsActivity.class);
            stepIntent.putExtra(STEP_RECIPE_KEY, step);
            stepIntent.putExtra(DETAILS_RECIPE_KEY, recipe);
            startActivity(stepIntent);
        }
    }

    // This method serves testing purposes and followed the instruction from:
    // https://github.com/googlesamples/android-testing Espresso Samples -> IntentsBasicSample
    @VisibleForTesting
    static Intent createResultData(Recipe recipe) {
        final Intent resultData = new Intent();
        resultData.putExtra(DETAILS_RECIPE_KEY, recipe);
        return resultData;
    }
}
