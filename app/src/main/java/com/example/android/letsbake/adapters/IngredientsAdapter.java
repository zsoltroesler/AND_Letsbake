package com.example.android.letsbake.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.letsbake.R;
import com.example.android.letsbake.models.Ingredient;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Zsolt on 09.04.2018.
 */

public class IngredientsAdapter extends RecyclerView.Adapter<IngredientsAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Ingredient> ingredientsList;

    /**
     * Create a new {@link IngredientsAdapter} object.
     *
     * @param ingredientsList is the list of {@link Ingredient}s to be displayed.
     */
    public IngredientsAdapter(ArrayList<Ingredient> ingredientsList) {
        this.ingredientsList = ingredientsList;
    }

    @Override
    public IngredientsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new ViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.item_recipe_ingredient, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientsAdapter.ViewHolder holder, int position) {
        final Ingredient ingredient = ingredientsList.get(position);

        // Get the ingredient and capitalize the first word
        String ingredientIngredient = capitalizeString(ingredient.getIngredientIngredient());

        // Get the ingredient quantity and convert it to string
        double quantity = ingredient.getIngredientQuantity();
        String ingredientQuantity = String.valueOf(quantity);

        // Get the ingredient quantity measure and lowercase it
        String ingredientMeasure = lowercaseString(ingredient.getIngredientMeasure());

        // Set all 3 ingredient components on ingredient View
        holder.ingredient.setText(context.getString(R.string.ingredient_format,
                ingredientIngredient, ingredientQuantity, ingredientMeasure));
    }

    @Override
    public int getItemCount() {
        if (ingredientsList == null) {
            return 0;
        } else {
            return ingredientsList.size();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_ingredient)
        TextView ingredient;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    // Helper method to clear or update ingredient list
    public void setIngredientList(ArrayList<Ingredient> ingredientsList) {
        this.ingredientsList = ingredientsList;
    }

    // Helper method to capitalize the first character
    @NonNull
    public static String capitalizeString (String ingredient) {
        return ingredient.substring(0, 1).toUpperCase() + ingredient.substring(1);
    }

    // Helper method to lowercase the whole String
    @NonNull
    public static String lowercaseString (String measure) {
        return measure.toLowerCase();
    }
}
