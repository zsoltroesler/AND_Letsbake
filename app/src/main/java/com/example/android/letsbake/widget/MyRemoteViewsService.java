package com.example.android.letsbake.widget;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.android.letsbake.R;
import com.example.android.letsbake.models.Ingredient;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.example.android.letsbake.adapters.IngredientsAdapter.capitalizeString;
import static com.example.android.letsbake.adapters.IngredientsAdapter.lowercaseString;
import static com.example.android.letsbake.fragments.RecipeListFragment.PREFS_NAME;
import static com.example.android.letsbake.fragments.RecipeListFragment.RECIPE_INGREDIENTS;

/**
 * Created by Zsolt on 07.05.2018.
 */

public class MyRemoteViewsService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new ListRemoteViewsFactory(this.getApplicationContext());
    }
}

class ListRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private static final String LOG_TAG = ListRemoteViewsFactory.class.getSimpleName();

    private Context context;
    private List<Ingredient> ingredientsList;

    // Constructor
    public ListRemoteViewsFactory(Context applicationContext) {
        context = applicationContext;
    }

    @Override
    public void onCreate() {
    }

    @Override
    public void onDataSetChanged() {
        try {
            SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
            String jsonIngredients = sharedPreferences.getString(RECIPE_INGREDIENTS, null);

            // The following code snippet is from
            // http://androidopentutorials.com/android-how-to-store-list-of-values-in-sharedpreferences/
            // Convert back the ingredient list from JSON string to ArrayList<>.
            Gson gson = new Gson();
            Ingredient[] ingredientItems = gson.fromJson(jsonIngredients,
                    Ingredient[].class);

            ingredientsList = Arrays.asList(ingredientItems);
            ingredientsList = new ArrayList<Ingredient>(ingredientsList);
        }catch (NullPointerException e){
            Log.e(LOG_TAG, Log.getStackTraceString(e));
        }
    }

    @Override
    public void onDestroy() {
        if (ingredientsList != null) ingredientsList.clear();
    }

    @Override
    public int getCount() {
        if (ingredientsList == null || ingredientsList.isEmpty()) {
            return 0;
        } return ingredientsList.size();
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public RemoteViews getViewAt(int position) {

        Ingredient ingredient = ingredientsList.get(position);

        // Get the ingredient and capitalize the first word
        String ingredientIngredient = capitalizeString(ingredient.getIngredientIngredient());

        // Get the ingredient quantity and convert it to string
        double quantity = ingredient.getIngredientQuantity();
        String ingredientQuantity = String.valueOf(quantity);

        // Get the ingredient quantity measure and lowercase it
        String ingredientMeasure = lowercaseString(ingredient.getIngredientMeasure());

        // Set all 3 ingredient components on ingredient View
        RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.item_recipe_ingredient);
        rv.setTextViewText(R.id.tv_ingredient, context.getString(R.string.ingredient_format,
                ingredientIngredient, ingredientQuantity, ingredientMeasure));

        int whiteColorValue = Color.WHITE;
        rv.setTextColor(R.id.tv_ingredient, whiteColorValue);

        return rv;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
}
