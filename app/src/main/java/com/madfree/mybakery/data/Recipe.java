package com.madfree.mybakery.data;

import com.google.gson.annotations.SerializedName;

public class Recipe {

    @SerializedName("recipeId")
    private Integer recipeId;
    @SerializedName("name")
    private String name;

    public Recipe(Integer recipeId, String name) {
        this.recipeId = recipeId;
        this.name = name;
    }

    public Integer getRecipeId() {
        return recipeId;
    }

    public String getName() {
        return name;
    }

}
