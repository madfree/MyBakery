package com.madfree.mybakery.view.ui;

import android.app.Application;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.madfree.mybakery.R;
import com.madfree.mybakery.service.model.Step;
import com.madfree.mybakery.view.adapter.StepAdapter;
import com.madfree.mybakery.viewmodel.StepListViewModel;

import java.util.List;

public class StepsFragment extends Fragment{

    private static final String LOG_TAG = StepsFragment.class.getSimpleName();

    private StepAdapter mStepsAdapter;
    private int recipeId;
    private Application application;
    private Context mContext;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_steps, container, false);

        mStepsAdapter = new StepAdapter();
        RecyclerView stepsListView = rootView.findViewById(R.id.steps_recycler_view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mContext);
        stepsListView.setLayoutManager(layoutManager);
        stepsListView.setAdapter(mStepsAdapter);

        setViewModel();

        return rootView;
    }

    private void setViewModel() {
        StepListViewModel mStepListViewModel = ViewModelProviders.of(this, new StepListViewModel(application, recipeId)).get(StepListViewModel.class);
        mStepListViewModel.getStepListObservable().observe(this, new Observer<List<Step>>() {
            @Override
            public void onChanged(@Nullable List<Step> steps) {
                Log.d(LOG_TAG, "Updating list of steps from LiveData in ViewModel with " + steps.size() + " steps");
                mStepsAdapter.setmStepList(steps);
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public int getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(int recipeId) {
        this.recipeId = recipeId;
    }
}