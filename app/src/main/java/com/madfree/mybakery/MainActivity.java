package com.madfree.mybakery;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.madfree.mybakery.adapter.RecipeAdapter;
import com.madfree.mybakery.data.Recipe;
import com.madfree.mybakery.network.FetchData;
import com.madfree.mybakery.network.GetDataService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private RecipeAdapter adapter;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        GetDataService service = FetchData.getRetrofitInstance().create(GetDataService.class);
        Call<List<Recipe>> call = service.getAllRecipes();
        call.enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                generateRecipeList(response.body());
            }

            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Ops! Something went wrong.", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void generateRecipeList(List<Recipe> recipeList) {
        recyclerView = findViewById(R.id.recipeRecyclerView);
        adapter = new RecipeAdapter(recipeList,this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

}
