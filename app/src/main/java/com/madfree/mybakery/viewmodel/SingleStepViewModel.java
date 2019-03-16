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
import com.madfree.mybakery.service.model.Step;
import com.madfree.mybakery.service.repository.Repository;

import java.util.concurrent.ExecutionException;

public class SingleStepViewModel extends AndroidViewModel implements ViewModelProvider.Factory{

    private static final String LOG_TAG = SingleStepViewModel.class.getSimpleName();

    private final Application mApplication;
    private int mRecipeId;
    private int mStepId;
    private boolean mTwoPane;
    private int mStepUID;

    private final LiveData<Step> singleStepObservable;

    public SingleStepViewModel(@NonNull Application application, int recipeId, int stepId, boolean twoPane) {
        super(application);
        this.mApplication = application;
        this.mRecipeId = recipeId;
        this.mStepId = stepId;
        this.mTwoPane = twoPane;
        Log.d(LOG_TAG, "Received recipeId " + recipeId + " and stepid " + stepId);
        AppExecutors executors = AppExecutors.getInstance();
        AppDatabase database = AppDatabase.getsInstance(application);
        Repository repository = Repository.getInstance(executors, database);
        try {
            if (mTwoPane) {
                mStepUID = repository.loadStepWithId(recipeId, 0);
                //singleStepObservable = repository.loadStartingStep(recipeId);
                Log.d(LOG_TAG, "Calling in TwoPane Mode for stepUID: " + mStepUID);
            } else {
                mStepUID = repository.loadStepWithId(recipeId, stepId);
                //singleStepObservable = repository.loadStepWithId(recipeId, stepId);
                Log.d(LOG_TAG, "Calling in SinglePane Mode for stepUID: " + mStepUID);
            }
        } finally {
            Log.d(LOG_TAG, "Here is the stepUID from Database: " + mStepUID);
            singleStepObservable = repository.loadSingleStep(mStepUID);
        }
    }

    public LiveData<Step> getStepListObservable() {
        return singleStepObservable;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new SingleStepViewModel(mApplication, mRecipeId, mStepId, mTwoPane);
    }
}
