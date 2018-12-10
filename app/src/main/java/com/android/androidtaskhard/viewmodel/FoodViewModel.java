package com.android.androidtaskhard.viewmodel;

import com.android.androidtaskhard.datamodel.FoodItem;
import com.android.androidtaskhard.retrofit.FoodListApi;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FoodViewModel extends ViewModel {

    private MutableLiveData<List<FoodItem>> foodLiveItems;

    public LiveData<List<FoodItem>> getFoodItems() {

        if (foodLiveItems == null) {
            foodLiveItems = new MutableLiveData<>();
            loadFoodItems();
        }

        return foodLiveItems;
    }

    private void loadFoodItems() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(FoodListApi.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        FoodListApi foodListApi = retrofit.create(FoodListApi.class);
        Call<List<FoodItem>> call = foodListApi.getFoodItems();


        call.enqueue(new Callback<List<FoodItem>>() {
            @Override
            public void onResponse(@NonNull Call<List<FoodItem>> call,@NonNull Response<List<FoodItem>> response) {

                foodLiveItems.setValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<List<FoodItem>> call, Throwable t) {

            }
        });
    }
}
