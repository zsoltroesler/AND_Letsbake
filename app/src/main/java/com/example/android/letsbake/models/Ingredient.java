package com.example.android.letsbake.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Zsolt on 03.04.2018.
 */

public class Ingredient implements Parcelable {

    public static final Creator<Ingredient> CREATOR = new Creator<Ingredient>() {
        @Override
        public Ingredient createFromParcel(Parcel in) {
            return new Ingredient(in);
        }

        @Override
        public Ingredient[] newArray(int size) { return new Ingredient[size]; }
    };

    // @SerializedName is needed for GSON to map the JSON keys with the attributes of a {@link Ingredient} object.
    @SerializedName("quantity")
    private double ingredientQuantity;

    @SerializedName("measure")
    private String ingredientMeasure;

    @SerializedName("ingredient")
    private String ingredientIngredient;

    // Constructs a new {@link Ingredient} object.
    public Ingredient(double ingredientQuantity, String ingredientMeasure, String ingredientIngredient) {
        this.ingredientQuantity = ingredientQuantity;
        this.ingredientMeasure = ingredientMeasure;
        this.ingredientIngredient = ingredientIngredient;
    }

    // Parcelling part
    private Ingredient(Parcel in) {
        ingredientQuantity = in.readDouble();
        ingredientMeasure = in.readString();
        ingredientIngredient = in.readString();
    }

    public double getIngredientQuantity() { return ingredientQuantity; }

    public String getIngredientMeasure() {
        return ingredientMeasure;
    }

    public String getIngredientIngredient() { return ingredientIngredient; }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(ingredientQuantity);
        dest.writeString(ingredientMeasure);
        dest.writeString(ingredientIngredient);
    }
}
