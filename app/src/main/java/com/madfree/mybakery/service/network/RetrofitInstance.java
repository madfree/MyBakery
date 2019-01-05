package com.madfree.mybakery.service.network;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.util.Log;

import com.madfree.mybakery.service.data.AppDatabase;
import com.madfree.mybakery.service.data.IngredientDao;
import com.madfree.mybakery.service.data.RecipeDao;
import com.madfree.mybakery.service.data.StepDao;
import com.madfree.mybakery.service.model.Ingredient;
import com.madfree.mybakery.service.model.Recipe;
import com.madfree.mybakery.service.model.Step;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitInstance {

    private static Retrofit retrofit;
    private static final String BASE_URL = "https://d17h27t6h515a5.cloudfront.net";
    private static final String LOG_TAG = RetrofitInstance.class.getSimpleName();
    private RecipeDao recipeDao;
    private IngredientDao ingredientDao;
    private StepDao stepDao;

    private static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        Log.d(LOG_TAG, "Retrofit Instance started!");
        return retrofit;
    }

    public LiveData<List<Recipe>> fetchRecipeData(Context context) {

        final MutableLiveData<List<Recipe>> data = new MutableLiveData<>();
        AppDatabase db = AppDatabase.getsInstance(context);
        recipeDao = db.recipeDao();
        ingredientDao = db.ingredientDao();
        stepDao = db.stepDao();

        final RecipeService recipeService = RetrofitInstance.getRetrofitInstance().create
                (RecipeService.class);

        Call<List<Recipe>> call = recipeService.getAllRecipes();
        Log.d(LOG_TAG, "URL called: " + call.request().url());

        call.enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                Integer statusCode = response.code();
                Log.d(LOG_TAG, "Status Code: " + statusCode.toString());
                List<Recipe> recipesDataList = response.body();

                for (int i=0; i < recipesDataList.size(); i++) {
                    String recipeName = recipesDataList.get(i).getName();
                    int recipeId = recipesDataList.get(i).getId();
                    Recipe netRecipe = new Recipe(recipeId, recipeName);
                    recipeDao.insertRecipe(netRecipe);
                    Log.d(LOG_TAG, "Inserted recipe: " + recipeName);

                    List<Ingredient> ingredientList = recipesDataList.get(i).getIngredients();
                    for (int j=0; j < ingredientList.size(); j++) {
                        String ingredientName = ingredientList.get(j).getIngredient();
                        String ingredientMeasure = ingredientList.get(j).getMeasure();
                        Double ingredientQuantity = ingredientList.get(j).getQuantity();
                        Ingredient ingredient = new Ingredient(ingredientName, ingredientMeasure, ingredientQuantity, recipeId);
                        ingredientDao.insertIngredient(ingredient);
                        Log.d(LOG_TAG, "Inserted ingredient: " + ingredientName);
                    }

                    List<Step> stepList = recipesDataList.get(i).getSteps();
                    for (int k=0; k < stepList.size(); k++) {
                        int stepId = stepList.get(k).getId();
                        String stepShortDesc = stepList.get(k).getShortDescription();
                        String stepDesc = stepList.get(k).getDescription();
                        String stepThumbNailUrl = stepList.get(k).getThumbnailURL();
                        String stepVideoUrl = stepList.get(k).getVideoURL();
                        Step step = new Step(stepId, stepShortDesc, stepDesc, stepThumbNailUrl, stepVideoUrl, recipeId);
                        stepDao.insertStep(step);
                        Log.d(LOG_TAG, "Inserted step: " + stepShortDesc);
                    }
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                Log.d(LOG_TAG, "Error!" + t.getMessage());
            }
        });
        return data;
    }
}
