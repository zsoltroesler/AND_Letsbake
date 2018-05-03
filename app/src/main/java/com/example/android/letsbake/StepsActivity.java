package com.example.android.letsbake;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.android.letsbake.fragments.RecipeStepsFragment;
import com.example.android.letsbake.models.Recipe;
import com.example.android.letsbake.models.Step;

import java.util.ArrayList;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.example.android.letsbake.fragments.RecipeDetailsFragment.STEP_RECIPE_KEY;
import static com.example.android.letsbake.fragments.RecipeListFragment.DETAILS_RECIPE_KEY;

/**
 * Created by Zsolt on 30.04.2018.
 */

public class StepsActivity extends AppCompatActivity {
    /**
     * Tag for the log messages
     */
    private static final String LOG_TAG = StepsActivity.class.getSimpleName();
    public static final String CURRENT_STEP = "current step";
    public static final String CURRENT_STEP_ID = "current step ID";
    public static final String CURRENT_STEPLIST = "current step list";

    private ArrayList<Step> stepsList = new ArrayList<>();
    private Recipe recipe;
    private Step step;
    private int currentStep;

    private int stepId;

    @BindView(R.id.tv_step)
    TextView stepView;
    @BindString(R.string.step_number)
    String stepNumber;
    @BindView(R.id.bt_previous)
    ImageButton buttonPrevious;
    @BindView(R.id.bt_next)
    ImageButton buttonNext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_steps);

        ButterKnife.bind(this);

        if (savedInstanceState != null) {
            step = savedInstanceState.getParcelable(CURRENT_STEP);
            currentStep = savedInstanceState.getInt(CURRENT_STEP_ID);
            stepsList = savedInstanceState.getParcelableArrayList(CURRENT_STEPLIST);

            setStepNumber();
        } else {
            // Get the data from the intent
            Bundle data = getIntent().getExtras();

            recipe = data.getParcelable(DETAILS_RECIPE_KEY);
            stepsList = recipe.getRecipeStepList();

            step = data.getParcelable(STEP_RECIPE_KEY);
            stepId = step.getStepId();
            currentStep = stepId;

            setStepNumber();

            setStepFragment(step);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(CURRENT_STEP, step);
        outState.putInt(CURRENT_STEP_ID, currentStep);
        outState.putParcelableArrayList(CURRENT_STEPLIST, stepsList);
    }

    // Skip to previous until step 0
    @OnClick(R.id.bt_previous)
    public void previous() {
        if (currentStep > 0) {
            currentStep--;
            step = stepsList.get(currentStep);

            setStepNumber();

            setStepFragment(step);
        }
    }

    // Skip to next until final step
    @OnClick(R.id.bt_next)
    public void next() {
        if (currentStep < stepsList.size()-1) {
            currentStep++;
            step = stepsList.get(currentStep);

            setStepNumber();

            setStepFragment(step);
        }
    }

    // Helper method to set the current step number
    private void setStepNumber() {
        stepView.setText(String.format(stepNumber, currentStep, stepsList.size()-1));
    }

    /**
     * Helper method to set the current {@link Step} fragment
     *
     * @param step is the instance of {@link Step} to be transferred
     */
    private void setStepFragment(Step step) {
        Bundle bundle = new Bundle();

        bundle.putParcelable(STEP_RECIPE_KEY, step);

        FragmentManager fragmentManager = getSupportFragmentManager();

        RecipeStepsFragment recipeStepsFragment = new RecipeStepsFragment();

        recipeStepsFragment.setArguments(bundle);

        fragmentManager.beginTransaction()
                .replace(R.id.container_recipe_steps, recipeStepsFragment)
                .commit();
    }
}
