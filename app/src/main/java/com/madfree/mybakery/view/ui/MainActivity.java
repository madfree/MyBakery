package com.madfree.mybakery.view.ui;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.madfree.mybakery.R;
import com.madfree.mybakery.service.EspressoIdlingResource;
import com.madfree.mybakery.service.data.AppDatabase;
import com.madfree.mybakery.service.data.AppExecutors;
import com.madfree.mybakery.service.model.Recipe;
import com.madfree.mybakery.service.repository.Repository;
import com.madfree.mybakery.view.adapter.RecipeAdapter;
import com.madfree.mybakery.viewmodel.RecipeListViewModel;


import java.util.List;

public class MainActivity extends AppCompatActivity implements RecipeAdapter.ItemClickListener {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    private RecipeAdapter mAdapter;
    private EspressoIdlingResource mEspressoIdlingResource;

    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource() {
        if (mEspressoIdlingResource == null) {
            mEspressoIdlingResource = new EspressoIdlingResource();
        }
        return mEspressoIdlingResource;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getIdlingResource();
        if (mEspressoIdlingResource != null) {
            mEspressoIdlingResource.setIdleState(false);
        }

        RecyclerView mRecyclerView = findViewById(R.id.recipeRecyclerView);
        mAdapter = new RecipeAdapter(this, this);
        if (getApplication().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        } else {
            mRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        }
        mRecyclerView.setAdapter(mAdapter);


        setViewModel();
        if (mEspressoIdlingResource != null) {
            mEspressoIdlingResource.setIdleState(true);
        }
    }

    private void setViewModel() {
        RecipeListViewModel mRecipeListViewModel = ViewModelProviders.of(this).get
                (RecipeListViewModel.class);
        mRecipeListViewModel.getRecipesListObservable().observe(this, new Observer<List<Recipe>>() {
            @Override
            public void onChanged(@Nullable List<Recipe> recipes) {
                Log.d(LOG_TAG,
                        "Updating list of recipes from LiveData in ViewModel with " + recipes.size() + " recipes");
                    mAdapter.setRecipes(recipes);
            }
        });
    }

    @Override
    public void onItemClickListener(int itemId) {
        int recipeId = mAdapter.getRecipeList().get(itemId).getId();
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra("recipeId", recipeId);
        Log.d(LOG_TAG, "This is the recipeId set for the intent: " + recipeId);
        startActivity(intent);
    }
}
