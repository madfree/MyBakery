package com.madfree.mybakery.view.ui;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.madfree.mybakery.R;
import com.madfree.mybakery.service.data.AppDatabase;

import com.madfree.mybakery.widget.IngredientsWidget;


public class DetailActivity extends AppCompatActivity {

    private static final String LOG_TAG = DetailActivity.class.getSimpleName();
    private int recipeId;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_detail_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_favourite:
                saveFavoriteRecipe();
        }
        return true;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent = getIntent();
        recipeId = intent.getIntExtra("recipeId", 0);
        Log.d(LOG_TAG, "This is the recipeId from MainActivity: " + recipeId);

        IngredientsFragment ingredientsFragment = new IngredientsFragment();
        ingredientsFragment.setRecipeId(recipeId);

        StepsFragment stepsFragment = new StepsFragment();
        stepsFragment.setRecipeId(recipeId);

        FragmentManager fragmentManager = getSupportFragmentManager();

        fragmentManager.beginTransaction()
                .add(R.id.ingredients_frame, ingredientsFragment)
                .add(R.id.steps_frame, stepsFragment)
                .commit();
    }

    public void saveFavoriteRecipe() {
        AppDatabase database = AppDatabase.getsInstance(getApplicationContext());

        // Delete the old favorite
        database.recipeDao().removeFavorite();
        Log.d(LOG_TAG, "favorite recipe from database is: " + database.recipeDao().getFavorite());

        // save the new favorite
        database.recipeDao().setFavorite(true, recipeId);
        Log.d(LOG_TAG, "Save new favorite recipe: " + recipeId);

        updateWidget();
    }

    public void updateWidget() {
        int[] ids = AppWidgetManager.getInstance(getApplication()).getAppWidgetIds(new ComponentName(getApplication(), IngredientsWidget.class));
        IngredientsWidget newWidget = new IngredientsWidget();
        newWidget.onUpdate(this, AppWidgetManager.getInstance(this),ids);
    }
}
