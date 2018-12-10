package com.android.androidtaskhard.retrofit;

import com.android.androidtaskhard.datamodel.FoodItem;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface FoodListApi {

    String BASE_URL = "https://android-full-time-task.firebaseio.com/";

    @GET("data.json")
    Call<List<FoodItem>> getFoodItems();
}
