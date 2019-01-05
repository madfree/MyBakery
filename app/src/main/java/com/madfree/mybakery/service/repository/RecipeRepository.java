package com.madfree.mybakery.service.repository;

import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.util.Log;

import com.madfree.mybakery.service.data.AppDatabase;
import com.madfree.mybakery.service.data.RecipeDao;
import com.madfree.mybakery.service.model.Recipe;
import com.madfree.mybakery.service.network.RetrofitInstance;

import java.util.List;

public class RecipeRepository {

    private static final String LOG_TAG = RecipeRepository.class.getSimpleName();
    private LiveData<List<Recipe>> mRecipesList;

    public LiveData<List<Recipe>> getmRecipesList() {
        return mRecipesList;
    }

    public RecipeRepository(Context context) {

        AppDatabase db = AppDatabase.getsInstance(context);
        RecipeDao recipeDao = db.recipeDao();

        //for testing purposes only - to delete before productive use
        //db.recipeDao().deleteAllRecipes();

        boolean recipesExist = recipeDao.count() != 0;
        if (!recipesExist) {
            mRecipesList = new RetrofitInstance().fetchRecipeData(context);
            Log.d(LOG_TAG, "Fetching new recipes");
        }
        mRecipesList = recipeDao.loadAllRecipes();
        Log.d(LOG_TAG, "Loading recipes from the database");
    }
}
