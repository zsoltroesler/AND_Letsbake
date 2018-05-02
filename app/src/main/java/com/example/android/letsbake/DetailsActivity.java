package com.example.android.letsbake;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ScrollView;

import com.example.android.letsbake.adapters.IngredientsAdapter;
import com.example.android.letsbake.adapters.StepsAdapter;
import com.example.android.letsbake.fragments.RecipeDetailsFragment;
import com.example.android.letsbake.fragments.RecipeListFragment;
import com.example.android.letsbake.models.Ingredient;
import com.example.android.letsbake.models.Recipe;
import com.example.android.letsbake.models.Step;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.android.letsbake.fragments.RecipeListFragment.DETAILS_RECIPE_KEY;

/**
 * Created by Zsolt on 09.04.2018.
 */

public class DetailsActivity extends AppCompatActivity {

    /**
     * Tag for the log messages
     */
    private static final String LOG_TAG = DetailsActivity.class.getSimpleName();

    @BindView(R.id.sv_details)
    ScrollView scrollView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        ButterKnife.bind(this);

        Bundle data = getIntent().getExtras();

        FragmentManager fragmentManager = getSupportFragmentManager();

        RecipeDetailsFragment recipeDetailsFragment = new RecipeDetailsFragment();

        recipeDetailsFragment.setArguments(data);

        fragmentManager.beginTransaction()
                .replace(R.id.container_recipe_details, recipeDetailsFragment)
                .commit();

    }
}
