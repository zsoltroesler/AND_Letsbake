package com.example.android.letsbake.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Zsolt on 03.04.2018.
 */

public class Recipe implements Parcelable {

    public static final Creator<Recipe> CREATOR = new Creator<Recipe>() {
        @Override
        public Recipe createFromParcel(Parcel in) {
            return new Recipe(in);
        }

        @Override
        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };

    // @SerializedName  is needed for GSON to map the JSON keys with the attributes of a {@link Recipe} object.
    @SerializedName("id")
    private int recipeId;

    @SerializedName("name")
    private String recipeName;

    @SerializedName("servings")
    private int recipeServing;

    @SerializedName("ingredients")
    private ArrayList<Ingredient> recipeIngredientList = new ArrayList<>();

    @SerializedName("steps")
    private ArrayList<Step> recipeStepList = new ArrayList<>();

    @SerializedName("image")
    private String recipeImage;

    /**
     * Constructs a new {@link Recipe} object.
     *
     * @param recipeId                  is the id of the recipe
     * @param recipeName                is the recipe name
     * @param recipeServing             how many servings are in the recipe
     * @param recipeIngredientList      list of {@link Ingredient} objects
     * @param recipeStepList            list of {@link Step} objects
     * @param recipeImage               is the image of the recipe
     */
    public Recipe(int recipeId, String recipeName, int recipeServing,
                  ArrayList<Ingredient> recipeIngredientList, ArrayList<Step> recipeStepList, String recipeImage) {
        this.recipeId = recipeId;
        this.recipeName = recipeName;
        this.recipeServing = recipeServing;
        this.recipeIngredientList = recipeIngredientList;
        this.recipeStepList = recipeStepList;
        this.recipeImage = recipeImage;
    }

    // Parcelling part
    public Recipe(Parcel in) {
        recipeId = in.readInt();
        recipeName = in.readString();
        recipeServing = in.readInt();
        in.readTypedList(recipeIngredientList, Ingredient.CREATOR);
        in.readTypedList(recipeStepList, Step.CREATOR);
        recipeImage = in.readString();
    }

    public int getRecipeId() { return recipeId; }

    public String getRecipeName() {
        return recipeName;
    }

    public int getRecipeServing() { return recipeServing; }

    public ArrayList<Ingredient> getRecipeIngredientList() { return recipeIngredientList; }

    public ArrayList<Step> getRecipeStepList() { return recipeStepList; }

    public String getRecipeImage() { return recipeImage; }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(recipeId);
        dest.writeString(recipeName);
        dest.writeInt(recipeServing);
        dest.writeTypedList(recipeIngredientList);
        dest.writeTypedList(recipeStepList);
        dest.writeString(recipeImage);
    }
}
