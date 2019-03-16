package com.madfree.mybakery.service.network;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.util.Log;

import com.madfree.mybakery.service.data.AppDatabase;
import com.madfree.mybakery.service.data.IngredientDao;
import com.madfree.mybakery.service.data.RecipeDao;
import com.madfree.mybakery.service.data.StepDao;
import com.madfree.mybakery.service.model.Ingredient;
import com.madfree.mybakery.service.model.Recipe;
import com.madfree.mybakery.service.model.Step;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitInstance {

    private static Retrofit retrofit;
    private static final String BASE_URL = "https://d17h27t6h515a5.cloudfront.net";
    private static final String LOG_TAG = RetrofitInstance.class.getSimpleName();
    private static RecipeService sInstance;
    private static final Object sLock = new Object();

    private static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        Log.d(LOG_TAG, "Retrofit Instance started!");
        return retrofit;
    }

    public static RecipeService getsInstance() {
        synchronized (sLock) {
            if (sInstance == null) {
                sInstance = getRetrofitInstance().create(RecipeService.class);
            }
            return sInstance;
        }
    }
}
