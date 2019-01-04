package com.madfree.mybakery.service.data;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.madfree.mybakery.service.model.Ingredient;

import java.util.List;


@Dao
public interface IngredientDao {

    @Query("SELECT * FROM Ingredient ORDER BY ingredient")
    LiveData<List<Ingredient>> loadAllIngredients();

    //@Query("SELECT * FROM NetIngredient WHERE recipeId=:recipeId")
    //int loadIngredientsForRecipe(int recipeId);

    //@Query("SELECT count(*) FROM NetIngredient WHERE recipeId=:recipeId")
    //int getIngredientCountForRecipe(int recipeId);

    @Insert
    void insertIngredient(Ingredient ingredient);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateRecipe(Ingredient ingredient);

    @Delete
    void deleteRecipe(Ingredient ingredient);

}
