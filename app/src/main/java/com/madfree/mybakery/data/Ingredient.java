package com.madfree.mybakery.data;

import com.google.gson.annotations.SerializedName;

public class Ingredient {

    @SerializedName("quantity")
    private Float quantity;

    @SerializedName("measure")
    private String measure;

    @SerializedName("ingredientName")
    private String ingredientName;

    public Ingredient(Float quantity, String measure, String  ingredientName) {
        this.quantity = quantity;
        this.measure = measure;
        this.ingredientName = ingredientName;
    }

    public Float getQuantity() {
        return quantity;
    }

    public String getMeasure() {
        return measure;
    }

    public String getIngredientName() {
        return ingredientName;
    }

    public void setQuantity(Float quantity) {
        this.quantity = quantity;
    }

    public void setMeasure(String measure) {
        this.measure = measure;
    }

    public void setIngredientName(String ingredientName) {
        this.ingredientName = ingredientName;
    }
}
