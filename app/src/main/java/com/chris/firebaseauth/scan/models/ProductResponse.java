package com.chris.firebaseauth.scan.models;

import com.google.gson.annotations.SerializedName;

public class ProductResponse {
    @SerializedName("product")
    private Product product;

    public Product getProduct() {
        return product;
    }

}
