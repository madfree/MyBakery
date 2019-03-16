package com.madfree.mybakery.service.data;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import com.madfree.mybakery.service.model.Ingredient;
import com.madfree.mybakery.service.model.Recipe;
import com.madfree.mybakery.service.model.Step;
import com.madfree.mybakery.service.network.RecipeService;
import com.madfree.mybakery.service.network.RetrofitInstance;
import com.madfree.mybakery.service.repository.Repository;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


@Database(entities = {Recipe.class, Ingredient.class, Step.class}, version = 9, exportSchema =
        false)
public abstract class AppDatabase extends RoomDatabase {

    private static final String LOG_TAG = AppDatabase.class.getSimpleName();
    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "mybakery";
    private static AppDatabase sInstance;
    final MutableLiveData<List<Recipe>> data = new MutableLiveData<>();

    public abstract RecipeDao recipeDao();

    public abstract IngredientDao ingredientDao();

    public abstract StepDao stepDao();

    public static AppDatabase getsInstance(Context context) {

        if (sInstance == null) {
            synchronized (LOCK) {
                Log.d(LOG_TAG, "Creating new database instance");
                sInstance = Room.databaseBuilder(context.getApplicationContext(),
                        AppDatabase.class, AppDatabase.DATABASE_NAME)
                        .fallbackToDestructiveMigration()
                        .addCallback(roomCallback)
                        .build();
            }
        }
        Log.d(LOG_TAG, "Getting the database instance");
        return sInstance;
    }

    private static RoomDatabase.Callback roomCallback = new AppDatabase.Callback() {
        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);
            fetchRecipeData();
        }
    };

    private static class PopulateDbAsync extends AsyncTask<List<Recipe>, Void, Void> {

        private final RecipeDao recipeDao;
        private final IngredientDao ingredientDao;
        private final StepDao stepDao;

        PopulateDbAsync(AppDatabase db) {
            recipeDao = db.recipeDao();
            ingredientDao = db.ingredientDao();
            stepDao = db.stepDao();
        }

        @Override
        protected Void doInBackground(List<Recipe>... lists) {
            sInstance.clearAllTables();

            for (int i = 0; i < lists[0].size(); i++) {
                String recipeName = lists[0].get(i).getName();
                int recipeId = lists[0].get(i).getId();
                Recipe netRecipe = new Recipe(recipeId, recipeName);
                recipeDao.insertRecipe(netRecipe);
                //saveRecipe(netRecipe);
                Log.d(LOG_TAG, "Inserted recipe: " + recipeName);

                List<Ingredient> ingredientList = lists[0].get(i).getIngredients();
                for (int j = 0; j < ingredientList.size(); j++) {
                    String ingredientName = ingredientList.get(j).getIngredient();
                    String ingredientMeasure = ingredientList.get(j).getMeasure();
                    Double ingredientQuantity = ingredientList.get(j).getQuantity();
                    Ingredient ingredient = new Ingredient(ingredientName, ingredientMeasure,
                            ingredientQuantity, recipeId);
                    //saveIngredient(ingredient);
                    ingredientDao.insertIngredient(ingredient);
                    Log.d(LOG_TAG, "Inserted ingredient: " + ingredientName);
                }

                List<Step> stepList = lists[0].get(i).getSteps();
                for (int k = 0; k < stepList.size(); k++) {
                    int stepId = stepList.get(k).getId();
                    String stepShortDesc = stepList.get(k).getShortDescription();
                    String stepDesc = stepList.get(k).getDescription();
                    String stepVideoUrl = stepList.get(k).getVideoURL();
                    String stepThumbNailUrl = stepList.get(k).getThumbnailURL();
                    Step step = new Step(stepId, stepShortDesc, stepDesc, stepVideoUrl,
                            stepThumbNailUrl, recipeId);
                    //saveStep(step);
                    stepDao.insertStep(step);
                    Log.d(LOG_TAG, "Inserted step: " + stepShortDesc);
                }
            }
            return null;
        }
    }

    private static void fetchRecipeData() {
        final RecipeService recipeService = RetrofitInstance.getsInstance();

        recipeService.getAllRecipes().enqueue(new retrofit2.Callback<List<Recipe>>() {
            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                Integer statusCode = response.code();
                Log.d(LOG_TAG, "Status Code: " + statusCode.toString());
                List<Recipe> recipeList = response.body();
                new PopulateDbAsync(sInstance).execute(recipeList);
            }

            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {
                Log.d(LOG_TAG, "Error!" + t.getMessage());
            }
        });
    }
}