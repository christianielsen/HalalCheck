package com.chris.firebaseauth;

import com.chris.firebaseauth.map.models.NearbyPlaces;
import com.chris.firebaseauth.scan.models.ProductResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface APICall {
    @GET("maps/api/place/nearbysearch/json?")
    Call<NearbyPlaces> getData(@Query("location") String loc,
                               @Query("radius") String radius,
                               @Query("type") String type,
                               @Query("keyword") String keyword,
                               @Query("key") String key);

    @GET("product/{barcode}.json")
    Call<ProductResponse> getProductByBarcode(@Path("barcode") String barcode);
}

