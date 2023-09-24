package com.chris.firebaseauth.scan.models;

import com.google.gson.annotations.SerializedName;

public class Product {

    @SerializedName("ingredients_text")
    private String ingredientsText;

    public String getIngredientsText() {
        return ingredientsText;
    }
}
