package com.example.android.letsbake.utils;

import com.example.android.letsbake.models.Recipe;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * This interface defines the Endpoint for network call
 * Created by Zsolt on 03.04.2018.
 */

public interface BakingApiInterface {

    // We need only GET HTTP request method
    @GET("baking.json")
    // Return value is ArrayList<Recipe> object
    Call<ArrayList<Recipe>> getRecipe();
}
