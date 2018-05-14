package com.example.android.letsbake;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.android.letsbake.utils.SimpleIdlingResource;

public class MainActivity extends AppCompatActivity {

    // This will be null in production
    @Nullable
    private SimpleIdlingResource simpleIdlingResource;

    /**
     * instantiate a new instance of SimpleIdlingResource if the IdlingResource is null.
     * This method will only be called from test.
     */
    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource() {
        if (simpleIdlingResource == null) {
            simpleIdlingResource = new SimpleIdlingResource();
        }
        return simpleIdlingResource;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get IdlingResource instance
        getIdlingResource();
    }

}
