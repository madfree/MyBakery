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

public class SingleStepViewModel extends AndroidViewModel implements ViewModelProvider.Factory{

    private static final String LOG_TAG = SingleStepViewModel.class.getSimpleName();

    private final Application mApplication;
    private final int mStepUID;

    private final LiveData<Step> singleStepObservable;

    public SingleStepViewModel(@NonNull Application application, int stepUID) {
        super(application);
        this.mApplication = application;
        this.mStepUID = stepUID;
        Log.d(LOG_TAG, "Received stepUID " + stepUID);
        AppDatabase db = AppDatabase.getsInstance(application);
        singleStepObservable = db.stepDao().loadSingleStep(mStepUID);
    }

    public LiveData<Step> getStepListObservable() {
        return singleStepObservable;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new SingleStepViewModel(mApplication, mStepUID);
    }
}
