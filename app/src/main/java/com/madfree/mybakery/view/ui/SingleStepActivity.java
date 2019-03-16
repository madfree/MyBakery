package com.madfree.mybakery.view.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.madfree.mybakery.R;

public class SingleStepActivity extends AppCompatActivity {

    private static final String LOG_TAG = SingleStepActivity.class.getSimpleName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_step);

        Intent intent = getIntent();
        int recipeId = intent.getIntExtra("recipeId", 0);
        int stepId = intent.getIntExtra("stepId", 0);

        Log.d(LOG_TAG, "This is the recipeId from DetailActivity: " + recipeId + " and the stepId: " + stepId);

        SingleStepFragment singleStepFragment = new SingleStepFragment();
            singleStepFragment.setRecipeAndStepId(recipeId, stepId);

        FragmentManager fragmentManager = getSupportFragmentManager();

            fragmentManager.beginTransaction()
                    .add(R.id.single_step_frame, singleStepFragment)
                    .commit();
    }

}
