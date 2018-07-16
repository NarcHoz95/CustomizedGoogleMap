package com.aranteknoloji.trainingfinalproject;

import com.aranteknoloji.trainingfinalproject.models.RetrofitGetModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RetroDataService {

    @GET("/maps/api/place/textsearch/json?")
    Call<RetrofitGetModel> getPlaces(@Query("query") String query, @Query("key") String key);
}
