package com.example.android.letsbake.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.letsbake.R;
import com.example.android.letsbake.adapters.IngredientsAdapter;
import com.example.android.letsbake.adapters.StepsAdapter;
import com.example.android.letsbake.models.Ingredient;
import com.example.android.letsbake.models.Recipe;
import com.example.android.letsbake.models.Step;

import java.util.ArrayList;

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

    private Unbinder unbinder;

    @BindView(R.id.rv_recipe_ingredients)
    RecyclerView ingredientsRecyclerView;
    @BindView(R.id.rv_recipe_steps)
    RecyclerView stepsRecyclerView;

    // Define a new interface OnVideoStepClickListener that triggers a callback in the host activity
    OnVideoStepClickListener callback;

    // OnVideoStepClickListener interface, calls a method in the host activity named onVideoSelected
    public interface OnVideoStepClickListener {
        void onVideoSelected(int position);
    }

    // Required empty public constructor
    public RecipeDetailsFragment() {
    }

    // Override onAttach to make sure that the container activity has implemented the callback
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // This makes sure that the host activity has implemented the callback interface
        // If not, it throws an exception
        try {
            callback = (OnVideoStepClickListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + R.string.class_cast_exception_text);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_recipe_details, container, false);
        unbinder = ButterKnife.bind(this, view);

        // Get the data from the Bundle object
        Recipe recipe = getArguments().getParcelable(DETAILS_RECIPE_KEY);

        getActivity().setTitle(recipe.getRecipeName());

        ArrayList<Ingredient> ingredientList = recipe.getRecipeIngredientList();
        ArrayList<Step> stepsList = recipe.getRecipeStepList();

        // Attach a LayoutManager to this RecyclerView
        ingredientsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        ingredientsRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        IngredientsAdapter ingredientsAdapter = new IngredientsAdapter(ingredientList);
        ingredientsRecyclerView.setAdapter(ingredientsAdapter);
        ingredientsAdapter.setIngredientList(ingredientList);
        ingredientsAdapter.notifyDataSetChanged();

        stepsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        StepsAdapter stepsAdapter = new StepsAdapter(stepsList, new StepsAdapter.OnStepClickListener() {
            @Override
            public void onItemClick(Step step) {
                int selectedStep = step.getStepId();
                callback.onVideoSelected(selectedStep);
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

    // Binding reset
    @Override public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
