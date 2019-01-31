package com.madfree.mybakery.service.data;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.madfree.mybakery.service.model.Recipe;

import java.util.List;


@Dao
public interface RecipeDao {

    @Query("SELECT * FROM Recipe ORDER BY id")
    LiveData<List<Recipe>> loadAllRecipes();

    @Query("SELECT count(*) FROM Recipe")
    int count();

    @Query("SELECT * FROM Recipe WHERE id=:recipeId")
    Recipe loadRecipe(int recipeId);

    @Query("UPDATE Recipe SET favorite=:isFavorite WHERE id=:recipeId ")
    void setFavorite(boolean isFavorite, int recipeId);

    @Query("SELECT * FROM Recipe WHERE favorite=1")
    int getFavorite();

    @Query("UPDATE Recipe SET favorite=0")
    void removeFavorite();

    @Insert
    void insertRecipe(Recipe recipe);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateRecipe(Recipe recipe);

    @Delete
    void deleteRecipe(Recipe recipe);

    @Query("DELETE FROM Recipe")
    void deleteAllRecipes();

}