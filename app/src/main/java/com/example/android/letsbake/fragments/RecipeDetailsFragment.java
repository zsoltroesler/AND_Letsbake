package com.example.android.letsbake.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.Toast;

import com.example.android.letsbake.R;
import com.example.android.letsbake.StepsActivity;
import com.example.android.letsbake.adapters.IngredientsAdapter;
import com.example.android.letsbake.adapters.StepsAdapter;
import com.example.android.letsbake.models.Ingredient;
import com.example.android.letsbake.models.Recipe;
import com.example.android.letsbake.models.Step;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.example.android.letsbake.fragments.RecipeListFragment.DETAILS_RECIPE_KEY;

/**
 * Created by Zsolt on 09.04.2018.
 */

public class RecipeDetailsFragment extends Fragment {

    private static final String LOG_TAG = RecipeDetailsFragment.class.getSimpleName();

    public static final String STEP_RECIPE_KEY = "stepDetails";

    private ArrayList<Ingredient> ingredientList = new ArrayList<>();
    private ArrayList<Step> stepsList = new ArrayList<>();
    private IngredientsAdapter ingredientsAdapter;
    private StepsAdapter stepsAdapter;

    private Recipe recipe;
    private Unbinder unbinder;

    @BindView(R.id.rv_recipe_ingredients)
    RecyclerView ingredientsRecyclerView;
    @BindView(R.id.rv_recipe_steps)
    RecyclerView stepsRecyclerView;

    // Required empty public constructor
    public RecipeDetailsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_recipe_details, container, false);
        unbinder = ButterKnife.bind(this, view);

        // Get the data from the Bundle object
        recipe = getArguments().getParcelable(DETAILS_RECIPE_KEY);

        getActivity().setTitle(recipe.getRecipeName());

        ingredientList = recipe.getRecipeIngredientList();
        stepsList = recipe.getRecipeStepList();

        // Attach a LayoutManager to this RecyclerView
        ingredientsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        ingredientsRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        ingredientsAdapter = new IngredientsAdapter(ingredientList);
        ingredientsRecyclerView.setAdapter(ingredientsAdapter);
        ingredientsAdapter.setIngredientList(ingredientList);
        ingredientsAdapter.notifyDataSetChanged();

        stepsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        stepsAdapter = new StepsAdapter(stepsList, new StepsAdapter.OnStepClickListener() {
            @Override
            public void onItemClick(Step step) {
                Toast.makeText(getContext(), step.getStepShortDescription().toString(), Toast.LENGTH_SHORT).show();
                Intent stepsIntent = new Intent(getActivity(), StepsActivity.class);
                stepsIntent.putExtra(STEP_RECIPE_KEY, step);
                stepsIntent.putExtra(DETAILS_RECIPE_KEY, recipe);

                startActivity(stepsIntent);
            }
        });
        stepsRecyclerView.setAdapter(stepsAdapter);
        stepsAdapter.setStepsList(stepsList);
        stepsAdapter.notifyDataSetChanged();


        for (Ingredient ingredient : ingredientList) {
            String ingredientName = ingredient.getIngredientIngredient();
            Log.i(LOG_TAG, "INGREDIENT: " + ingredientName);
        }

        for (Step step : stepsList) {
            String stepName = step.getStepShortDescription();
            Log.i(LOG_TAG, "STEP: " + stepName);
        }
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    // Binding reset
    @Override public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

}
