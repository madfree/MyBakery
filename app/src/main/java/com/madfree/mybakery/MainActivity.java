package com.madfree.mybakery;


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

public class MainActivity extends AppCompatActivity {

    public static final String LOG_TAG = MainActivity.class.getSimpleName();

    private RecipeAdapter adapter;
    private RecyclerView recyclerView;
    private AppDatabase mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDb = AppDatabase.getsInstance(getApplicationContext());

        GetDataService service = FetchData.getRetrofitInstance().create(GetDataService.class);
        Call<List<Recipe>> call = service.getAllRecipes();
        call.enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                //generateRecipeList(response.body());
                if (response.isSuccessful()) {
                    List<Recipe> recipeList = response.body();
                    Log.d(LOG_TAG, "Size of the recipeList: " + recipeList);
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
                    Log.d(LOG_TAG, "Something went wrong trying to get the data");
                }
            }

            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Ops! Something went wrong.", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        List<Recipe> recipeList = mDb.recipeDao().loadAllRecipes();
        recyclerView = findViewById(R.id.recipeRecyclerView);
        adapter = new RecipeAdapter(recipeList,this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }
}
