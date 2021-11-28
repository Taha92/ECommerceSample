package com.example.ecommerceapp.interfaces;

import com.example.ecommerceapp.models.BaseResponse;
import com.example.ecommerceapp.models.ItemData;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface iWebServices {

    @GET("/ecommerce/products")
    Call<ArrayList<ItemData>> getItems();

    @POST("/ecommerce/products/{id}")
    Call<BaseResponse> updateItem(@Path(value = "id", encoded = true) String id, @Body ItemData productData);


}
