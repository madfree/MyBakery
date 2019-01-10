package com.madfree.mybakery.view.ui;

import android.app.Application;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.madfree.mybakery.R;
import com.madfree.mybakery.service.model.Ingredient;
import com.madfree.mybakery.view.adapter.IngredientAdapter;
import com.madfree.mybakery.viewmodel.IngredientListViewModel;

import java.util.List;

public class IngredientsFragment extends Fragment {

    private static final String LOG_TAG = IngredientsFragment.class.getSimpleName();

    private IngredientAdapter mIngredientsAdapter;
    private int recipeId;
    private Application application;
    private Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_ingrendients, container, false);

        mIngredientsAdapter = new IngredientAdapter();
        RecyclerView ingredientsListView = rootView.findViewById(R.id.ingredients_recycler_view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        ingredientsListView.setLayoutManager(layoutManager);
        ingredientsListView.setAdapter(mIngredientsAdapter);

        setViewModel();

        return rootView;
    }

    private void setViewModel() {
        IngredientListViewModel mIngredientListViewModel = ViewModelProviders.of(this, new
                IngredientListViewModel(application, recipeId)).get(IngredientListViewModel.class);
        mIngredientListViewModel.getIngredientListObservable().observe(this, new Observer<List<Ingredient>>() {
            @Override
            public void onChanged(@Nullable List<Ingredient> ingredients) {
                Log.d(LOG_TAG, "Updating list of ingredients from LiveData in ViewModel with " + ingredients.size() + " ingredients");
                mIngredientsAdapter.setmIngredientList(ingredients);
            }
        });
    }

    public int getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(int recipeId) {
        this.recipeId = recipeId;
    }
}
