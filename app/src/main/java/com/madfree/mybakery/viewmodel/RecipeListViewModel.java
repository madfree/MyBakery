package com.madfree.mybakery.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.madfree.mybakery.service.model.Recipe;
import com.madfree.mybakery.service.repository.RecipeRepository;

import java.util.List;

public class RecipeListViewModel extends AndroidViewModel {

    private final LiveData<List<Recipe>> recipesListObservable;

    public RecipeListViewModel(@NonNull Application application) {
        super(application);
        RecipeRepository mRepository = new RecipeRepository(application);
        recipesListObservable = mRepository.getmRecipesList();
    }

    public LiveData<List<Recipe>> getRecipesListObservable() {
        return recipesListObservable;
    }

}
