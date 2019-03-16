package com.madfree.mybakery.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.madfree.mybakery.service.data.AppDatabase;
import com.madfree.mybakery.service.data.AppExecutors;
import com.madfree.mybakery.service.model.Recipe;
import com.madfree.mybakery.service.repository.Repository;

import java.util.List;

public class RecipeListViewModel extends AndroidViewModel {

    private final LiveData<List<Recipe>> recipesListObservable;

    public RecipeListViewModel(@NonNull Application application) {
        super(application);
        //RecipeRepository mRepository = new RecipeRepository(application);
        //recipesListObservable = mRepository.getmRecipesList();
        AppExecutors executors = AppExecutors.getInstance();
        AppDatabase database = AppDatabase.getsInstance(application);
        Repository repository = Repository.getInstance(executors, database);
        recipesListObservable = repository.getAllRecipes();

    }

    public LiveData<List<Recipe>> getRecipesListObservable() {
        return recipesListObservable;
    }

}
