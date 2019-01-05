package com.madfree.mybakery.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;
import android.util.Log;

import com.madfree.mybakery.service.data.AppDatabase;
import com.madfree.mybakery.service.model.Step;

import java.util.List;

public class StepListViewModel extends AndroidViewModel implements ViewModelProvider.Factory{

    private static final String LOG_TAG = StepListViewModel.class.getSimpleName();

    private final Application mApplication;
    private int mRecipeId;

    private final LiveData<List<Step>> stepListObservable;

    public StepListViewModel(@NonNull Application application, int recipeId) {
        super(application);
        this.mApplication = application;
        this.mRecipeId = recipeId;
        Log.d(LOG_TAG, "Received recipeId " + recipeId);
        AppDatabase db = AppDatabase.getsInstance(application);
        stepListObservable = db.stepDao().loadStepsForRecipe(recipeId);
    }

    public LiveData<List<Step>> getStepListObservable() {
        return stepListObservable;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new StepListViewModel(mApplication, mRecipeId);
    }
}
