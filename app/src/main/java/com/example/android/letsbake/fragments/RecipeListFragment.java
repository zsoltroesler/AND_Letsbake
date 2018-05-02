package com.example.android.letsbake.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.android.letsbake.DetailsActivity;
import com.example.android.letsbake.MainActivity;
import com.example.android.letsbake.R;
import com.example.android.letsbake.adapters.RecipeAdapter;
import com.example.android.letsbake.models.Recipe;
import com.example.android.letsbake.utils.BakingApiClient;
import com.example.android.letsbake.utils.BakingApiInterface;

import java.util.ArrayList;
import java.util.Arrays;

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

    private static final String LOG_TAG = RecipeListFragment.class.getSimpleName();

    public static final String DETAILS_RECIPE_KEY = "recipeDetails";

    private ArrayList<Recipe> recipeList = new ArrayList<>();
    private RecipeAdapter recipeAdapter;

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

        makeRetrofitCall();
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }


    private void makeRetrofitCall() {
        // To instantiate the BakingApiClient
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
                            Toast.makeText(getContext(), recipe.getRecipeName().toString(), Toast.LENGTH_SHORT).show();
                            Intent detailsIntent = new Intent(getActivity(), DetailsActivity.class);
                            detailsIntent.putExtra(DETAILS_RECIPE_KEY, recipe);
                            startActivity(detailsIntent);
                        }
                    });
                    recyclerView.setLayoutManager(new GridLayoutManager(getContext(), numberOfColumns()));
                    recyclerView.setAdapter(recipeAdapter);
                    recipeAdapter.setRecipeList(recipeList);
                    recipeAdapter.notifyDataSetChanged();
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
    @Override public void onDestroyView() {
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
}
