package com.madfree.mybakery;


import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.madfree.mybakery.adapter.RecipeAdapter;
import com.madfree.mybakery.data.AppDatabase;
import com.madfree.mybakery.data.Recipe;
import com.madfree.mybakery.network.FetchData;
import com.madfree.mybakery.network.GetDataService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements  RecipeAdapter.ItemClickListener {

    public static final String LOG_TAG = MainActivity.class.getSimpleName();

    private RecyclerView recyclerView;
    private RecipeAdapter mAdapter;
    private AppDatabase mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recipeRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new RecipeAdapter(this, this);
        recyclerView.setAdapter(mAdapter);

        mDb = AppDatabase.getsInstance(getApplicationContext());

        getData();

        retrieveRecipes();
    }

    private void getData(){
        GetDataService service = FetchData.getRetrofitInstance().create(GetDataService.class);
        Call<List<Recipe>> call = service.getAllRecipes();
        call.enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                //generateRecipeList(response.body());
                if (response.isSuccessful()) {
                    List<Recipe> recipeList = response.body();
                    Log.d(LOG_TAG, "Size of the recipeList: " + recipeList.size());
                    if (recipeList != null)
                        for (int i=0; i<recipeList.size(); i++) {
                            String recipeName = recipeList.get(i).getRecipeName();
                            Log.d(LOG_TAG, "This is the recipeName: " + recipeName);
                            int recipeId = recipeList.get(i).getRecipeId();
                            Log.d(LOG_TAG, "This is the recipeId: " +recipeId);
                            Recipe newRecipe = new Recipe(recipeId, recipeName);

                            if (mDb.recipeDao().loadAllRecipes() == null) {
                                mDb.recipeDao().insertRecipe(newRecipe);
                            } else {
                                mDb.recipeDao().updateRecipe(newRecipe);
                            }
                            Log.d(LOG_TAG, "Inserted new recipe: " + recipeName);
                        }
                } else {
                    Log.d(LOG_TAG, "There was not data to insert to the database");
                }
            }
            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Ops! Something went wrong.", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void retrieveRecipes() {
        Log.d(LOG_TAG, "Actively retrieving the recipes from the database");
        LiveData<List<Recipe>> recipeList = mDb.recipeDao().loadAllRecipes();
        recipeList.observe(this, new Observer<List<Recipe>>() {
            @Override
            public void onChanged(@Nullable List<Recipe> recipes) {
                Log.d(LOG_TAG, "Receiving database update from Livedata");
                mAdapter.setRecipes(recipes);
            }
        });
    }

    @Override
    public void onItemClickListener(int itemId) {
        Toast.makeText(MainActivity.this, "You click on recipe: " + itemId, Toast.LENGTH_SHORT).show();
    }

}
