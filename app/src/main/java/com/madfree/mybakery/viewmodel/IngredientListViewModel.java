package com.madfree.mybakery.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;
import android.util.Log;

import com.madfree.mybakery.service.data.AppDatabase;
import com.madfree.mybakery.service.data.AppExecutors;
import com.madfree.mybakery.service.model.Ingredient;
import com.madfree.mybakery.service.repository.Repository;

import java.util.List;

public class IngredientListViewModel extends AndroidViewModel implements ViewModelProvider.Factory {

    private static final String LOG_TAG = IngredientListViewModel.class.getSimpleName();

    private final Application mApplication;
    private final int mRecipeId;

    private final LiveData<List<Ingredient>> ingredientListObservable;

    public IngredientListViewModel(@NonNull Application application, int recipeId) {
        super(application);
        this.mApplication = application;
        this.mRecipeId = recipeId;
        Log.d(LOG_TAG, "Received recipeId " + recipeId);
        AppExecutors executors = AppExecutors.getInstance();
        AppDatabase database = AppDatabase.getsInstance(application);
        Repository repository = Repository.getInstance(executors, database);
        ingredientListObservable = repository.loadIngredientsForRecipe(recipeId);
//        AppDatabase db = AppDatabase.getsInstance(application);
//        ingredientListObservable = db.ingredientDao().loadIngredientsForRecipe(recipeId);
    }

    public LiveData<List<Ingredient>> getIngredientListObservable() {
        return ingredientListObservable;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new IngredientListViewModel(mApplication, mRecipeId);
    }
}
