package com.madfree.mybakery.view.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.madfree.mybakery.R;

public class DetailActivity extends AppCompatActivity {

    private static final String LOG_TAG = DetailActivity.class.getSimpleName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent = getIntent();
        int recipeId = intent.getIntExtra("recipeId", 0);
        Log.d(LOG_TAG, "This is the recipeId from MainActivity: " + recipeId);

        IngredientsFragment ingredientsFragment = new IngredientsFragment();
        ingredientsFragment.setRecipeId(recipeId);
        FragmentManager fragmentManager = getSupportFragmentManager();

        fragmentManager.beginTransaction()
                .add(R.id.ingredients_frame, ingredientsFragment)
                .commit();
    }
}
