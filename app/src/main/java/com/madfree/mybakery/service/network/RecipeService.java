package com.madfree.mybakery.service.network;

import com.madfree.mybakery.service.model.Recipe;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

interface RecipeService {

    @GET("/topher/2017/May/59121517_baking/baking.json")
    Call<List<Recipe>> getAllRecipes();

}
