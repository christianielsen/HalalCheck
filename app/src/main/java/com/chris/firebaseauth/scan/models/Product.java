package com.chris.firebaseauth.scan.models;

import com.google.gson.annotations.SerializedName;

public class Product {

    @SerializedName("ingredients_text")
    private String ingredientsText;
    @SerializedName("ingredients_n")
    private int ingredientNumber;
    @SerializedName("product_name")
    private String name;
    @SerializedName("image_url")
    private String imageUrl;


    public String getIngredientsText() {
        return ingredientsText;
    }

    public String getName() {
        return name;
    }

    public int getIngredientNumber() {
        return ingredientNumber;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
