package com.example.android.letsbake.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.RemoteViews;

import com.example.android.letsbake.MainActivity;
import com.example.android.letsbake.R;

import static com.example.android.letsbake.fragments.RecipeListFragment.PREFS_NAME;
import static com.example.android.letsbake.fragments.RecipeListFragment.RECIPE_NAME;

/**
 * Implementation of App Widget functionality.
 */
public class RecipeWidgetProvider extends AppWidgetProvider {

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {

            // Construct the RemoteViews object
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.recipe_widget_provider);

            // Get the recipe name from SharedPreferences and display it the widget title.
            SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
            String recipeName = sharedPreferences.getString(RECIPE_NAME, null);
            if (recipeName == null){
                views.setTextViewText(R.id.tv_widget, context.getString(R.string.app_name));
            } else {
                views.setTextViewText(R.id.tv_widget, recipeName);
            }

            // Create an Intent to launch MainActivity when clicked
            Intent activityOpenerIntent = new Intent(context, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, activityOpenerIntent, 0);

            // Widget allows click handlers to only launch pending intents
            views.setOnClickPendingIntent(R.id.ll_widget, pendingIntent);

            // Set up the intent that starts the MyRemoteViewsService, which will
            // provide the views for ingredients list.
            Intent remoteViewServiceIntent = new Intent(context, MyRemoteViewsService.class);

            // Set up the RemoteViews object to use a RemoteViews adapter.
            // This adapter connects to a RemoteViewsService  through the specified intent.
            // This is how the data is populated.
            views.setRemoteAdapter(R.id.lv_widget, remoteViewServiceIntent);

            //Trigger data update to handle the ListView collection view and force a data refresh
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.lv_widget);

            // The empty view is displayed when the collection has no items.
            // It should be in the same layout used to instantiate the RemoteViews
            // object above.
            views.setEmptyView(R.id.lv_widget, R.id.tv_empty_widget);

            // Instruct the widget manager to update the widget
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
    }

    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
    }
}

