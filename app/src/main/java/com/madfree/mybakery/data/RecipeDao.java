package com.madfree.mybakery.data;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface RecipeDao {

    @Query("SELECT * FROM recipes ORDER BY recipeId")
    List<Recipe> loadAllRecipes();

    @Insert
    void insertRecipe(Recipe recipe);


    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateRecipe(Recipe recipe);

    @Delete
    void deleteRecipe(Recipe recipe);

}