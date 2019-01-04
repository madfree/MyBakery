package com.madfree.mybakery.view.ui;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.madfree.mybakery.R;
import com.madfree.mybakery.service.model.Recipe;
import com.madfree.mybakery.view.adapter.RecipeAdapter;
import com.madfree.mybakery.viewmodel.RecipeListViewModel;


import java.util.List;

public class MainActivity extends AppCompatActivity implements  RecipeAdapter.ItemClickListener {

    public static final String LOG_TAG = MainActivity.class.getSimpleName();

    private RecyclerView mRecyclerView;
    private RecipeAdapter mAdapter;
    private RecipeListViewModel mRecipeListViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRecyclerView = findViewById(R.id.recipeRecyclerView);
        mAdapter = new RecipeAdapter(this, this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter);

        setViewModel();
    }

    private void setViewModel() {
        mRecipeListViewModel = ViewModelProviders.of(this).get(RecipeListViewModel.class);
        mRecipeListViewModel.getRecipesListObservable().observe(this, new Observer<List<Recipe>>() {
            @Override
            public void onChanged(@Nullable List<Recipe> recipes) {
                Log.d(LOG_TAG, "Updating list of recipes from LiveData in ViewModel with " + recipes.size() + " recipes");
                mAdapter.setRecipes(recipes);
            }
        });
    }

    @Override
    public void onItemClickListener(int itemId) {
        int recipeId = mAdapter.getRecipeList().get(itemId).getId();
        Toast.makeText(MainActivity.this, "This recipe has the ID " + recipeId, Toast.LENGTH_SHORT).show();
    }
}
