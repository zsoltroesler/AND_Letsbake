package com.example.android.letsbake.utils;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Zsolt on 04.04.2018.
 */

// This class will be never extended.
public final class BakingApiClient {

    private static Retrofit retrofit = null;

    private static final String BASE_URL =
            "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/";

    // This class will never be instantiated therefore constructor is suppressed.
    private BakingApiClient() {}

    // Send network request to the API using BASE_URL
    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
