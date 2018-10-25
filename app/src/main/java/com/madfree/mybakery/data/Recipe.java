package com.madfree.mybakery.data;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "recipes")
public class Recipe {

    @PrimaryKey
    @SerializedName("id")
    private int recipeId;

    @SerializedName("name")
    private String recipeName;

    public Recipe(int recipeId, String recipeName) {
        this.recipeId = recipeId;
        this.recipeName = recipeName;
    }

    public int getRecipeId() {
        return recipeId;
    }

    public String getRecipeName() {
        return recipeName;
    }

    public void setRecipeId(int recipeId) {
        this.recipeId = recipeId;
    }

    public void setRecipeName(String recipeName) {
        this.recipeName = recipeName;
    }

}
