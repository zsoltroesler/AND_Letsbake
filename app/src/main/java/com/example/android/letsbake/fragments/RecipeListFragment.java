package com.example.android.letsbake.fragments;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.android.letsbake.DetailsActivity;
import com.example.android.letsbake.MainActivity;
import com.example.android.letsbake.R;
import com.example.android.letsbake.adapters.RecipeAdapter;
import com.example.android.letsbake.models.Ingredient;
import com.example.android.letsbake.models.Recipe;
import com.example.android.letsbake.utils.BakingApiClient;
import com.example.android.letsbake.utils.BakingApiInterface;
import com.example.android.letsbake.utils.SimpleIdlingResource;
import com.example.android.letsbake.widget.RecipeWidgetProvider;
import com.google.gson.Gson;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by Zsolt on 09.04.2018.
 */

public class RecipeListFragment extends Fragment {

    public static final String DETAILS_RECIPE_KEY = "Details recipe key";
    public static final String PREFS_NAME = "SharedPreferences";
    public static final String RECIPE_NAME = "Recipe name";
    public static final String RECIPE_INGREDIENTS = "Recipe ingredients";

    private ArrayList<Recipe> recipeList = new ArrayList<>();
    private RecipeAdapter recipeAdapter;

    // This will be null in production
    @Nullable
    private SimpleIdlingResource simpleIdlingResource;

    @BindView(R.id.rv_recipe_list)
    RecyclerView recyclerView;
    @BindView(R.id.pb_loading_indicator)
    ProgressBar progressBar;
    private Unbinder unbinder;

    // Required empty public constructor
    public RecipeListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_recipe_list, container, false);
        unbinder = ButterKnife.bind(this, view);

        progressBar.setVisibility(View.VISIBLE);

        simpleIdlingResource = (SimpleIdlingResource) new MainActivity().getIdlingResource();

        makeRetrofitCall();
        return view;
    }

    private void makeRetrofitCall() {
        /**
         * If the idle state is true, Espresso can perform the next action.
         * If the idle state is false, Espresso will wait until it is true before
         * performing the next action.
         */
        if (simpleIdlingResource != null) {
            simpleIdlingResource.setIdleState(false);
        }

        // Instantiate the BakingApiClient
        BakingApiInterface bakingApiService =
                BakingApiClient.getClient().create(BakingApiInterface.class);

        Call<ArrayList<Recipe>> call = bakingApiService.getRecipe();

        // .enqueue () method makes the call async
        call.enqueue(new Callback<ArrayList<Recipe>>() {
            @Override
            public void onResponse(Call<ArrayList<Recipe>> call, Response<ArrayList<Recipe>> response) {
                if (response.isSuccessful()) {
                    progressBar.setVisibility(View.GONE);
                    // To map Recipe class to the response
                    recipeList = response.body();
                    recipeAdapter = new RecipeAdapter(recipeList, new RecipeAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(Recipe recipe) {
                            Intent detailsIntent = new Intent(getActivity(), DetailsActivity.class);
                            detailsIntent.putExtra(DETAILS_RECIPE_KEY, recipe);
                            startActivity(detailsIntent);

                            saveIntoSharedPreferences(recipe);

                            // Send a broadcast message to inform AppWidgetProvider that recipe name
                            // and ingredients list are saved into SharedPreferences
                            Intent broadcastIntent = new Intent(getActivity(), RecipeWidgetProvider.class);
                            broadcastIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
                            // Use an array and EXTRA_APPWIDGET_IDS instead of AppWidgetManager.EXTRA_APPWIDGET_ID,
                            // since it seems the onUpdate() is only fired on that:
                            int[] ids = AppWidgetManager.getInstance(getActivity()).
                                    getAppWidgetIds(new ComponentName(getActivity(), RecipeWidgetProvider.class));
                            broadcastIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
                            getContext().sendBroadcast(broadcastIntent);
                        }
                    });
                    recyclerView.setLayoutManager(new GridLayoutManager(getContext(), numberOfColumns()));
                    recyclerView.setAdapter(recipeAdapter);
                    recipeAdapter.setRecipeList(recipeList);
                    recipeAdapter.notifyDataSetChanged();
                    if (simpleIdlingResource != null) {
                        simpleIdlingResource.setIdleState(true);
                    }
                } else {
                    Toast.makeText(getContext(), R.string.problem_occurred, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Recipe>> call, Throwable t) {
                Toast.makeText(getContext(), R.string.internet_connection, Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Binding reset
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    // Helper method to calculate dynamically the number of columns in gridlayout
    private int numberOfColumns() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int widthDivider = 600;
        int width = displayMetrics.widthPixels;
        int nColumns = width / widthDivider;
        if (nColumns < 2) return 1;
        return nColumns;
    }

    // Helper method to save recipe name and ingredients list into SharedPreferences, which will be
    // reused by Widgets
    private void saveIntoSharedPreferences(Recipe recipe) {
        SharedPreferences sharedPreferences = getContext().
                getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        // Get the recipe name and ingredients list from {@link Recipe} object
        String recipeName = recipe.getRecipeName();
        ArrayList<Ingredient> ingredientsList = recipe.getRecipeIngredientList();

        // The following code snippet is from:
        // http://androidopentutorials.com/android-how-to-store-list-of-values-in-sharedpreferences/
        // Convert the ingredients ArrayList into JSON String in order to store list of its values
        // in SharedPreferences
        Gson gson = new Gson();
        String jsonIngredients = gson.toJson(ingredientsList);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(RECIPE_NAME, recipeName);
        editor.putString(RECIPE_INGREDIENTS, jsonIngredients);
        editor.apply();
    }
}
