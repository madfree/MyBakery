package com.madfree.mybakery.service.data;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import com.madfree.mybakery.service.model.Step;

import java.util.List;

@Dao
public interface StepDao {

    @Query("SELECT * FROM Step ORDER BY id")
    LiveData<List<Step>> loadAllSteps();

    @Query("SELECT * FROM Step WHERE recipeId=:recipeId")
    LiveData<List<Step>> loadStepsForRecipe(int recipeId);

    @Query("SELECT stepId FROM Step WHERE recipeId=:recipeId AND id=:id")
    Integer loadStepWithIds(int recipeId, int id);

    @Query("SELECT * FROM Step WHERE recipeId=:recipeId AND id=:id")
    LiveData<List<Step>> loadStepWithRecipeAndStepId(int recipeId, int id);

    @Query("SELECT * FROM Step WHERE stepId=:stepId")
    LiveData<Step> loadSingleStep(int stepId);

    @Query("SELECT count(*) FROM Step")
    int count();

    @Insert
    void insertStep(Step step);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateStep(Step step);

    @Delete
    void deleteStep(Step step);

    @Query("DELETE FROM Step")
    void deleteAllSteps();
}
