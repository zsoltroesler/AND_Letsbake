package com.example.android.letsbake.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.letsbake.R;
import com.example.android.letsbake.models.Recipe;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Zsolt on 06.04.2018.
 */

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.ViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(Recipe recipe);
    }

    private Context context;
    private ArrayList<Recipe> recipeList;
    private final OnItemClickListener listener;

    /**
     * Create a new {@link RecipeAdapter} object.
     *
     * @param recipeList is the list of {@link Recipe}s to be displayed.
     * @param listener   is an object of OnItemClickListener.
     */
    public RecipeAdapter(ArrayList<Recipe> recipeList, OnItemClickListener listener) {
        this.recipeList = recipeList;
        this.listener = listener;
    }

    @Override
    public RecipeAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new ViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.item_recipe_list, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeAdapter.ViewHolder holder, int position) {

        final Recipe recipe = recipeList.get(position);

        // Get and set the corresponding recipe name
        String recipeName = recipe.getRecipeName();
        holder.recipeName.setText(recipeName);

        // Get and set the corresponding cake image
        String recipeImage = recipe.getRecipeImage();
        if (!recipeImage.isEmpty()){
            Picasso.with(holder.itemView.getContext())
                    .load(recipeImage)
                    .into(holder.cakeImage);
        } else holder.cakeImage.setImageResource(imagePlaceholder(recipeName));

        // Get and set the corresponding amount of servings
        int recipeServings = recipe.getRecipeServing();
        holder.recipeServing.setText(context.getString(R.string.servings, recipeServings) );

        // Set the OnClickListener on a single recipe
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(recipe);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (recipeList == null) {
            return 0;
        } else {
            return recipeList.size();
        }
    }

    // Create the ViewHolder class for references
    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.iv_cake)
        ImageView cakeImage;
        @BindView(R.id.tv_recipe_name)
        TextView recipeName;
        @BindView(R.id.tv_recipe_serving)
        TextView recipeServing;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    // Helper method to clear or update recipe list
    public void setRecipeList(ArrayList<Recipe> recipeList) {
        this.recipeList = recipeList;
    }

    // Helper method to set a corresponding image to the @Recipe if the there is no image URL
    private int imagePlaceholder (String cakeName) {
        int imageResourceId;

        // All these images are designed by Freepik, www.freepik.com
        switch (cakeName) {
            case "Nutella Pie":
                imageResourceId = R.drawable.ic_nutella_pie;
                break;
            case "Brownies":
                imageResourceId = R.drawable.ic_brownie;
                break;
            case "Yellow Cake":
                imageResourceId = R.drawable.ic_yellowcake;
                break;
            case "Cheesecake":
                imageResourceId = R.drawable.ic_cheesecake;
                break;
            default:
                imageResourceId = R.drawable.ic_cake_placeholder;
                break;
        }
        return imageResourceId;
    }
}
