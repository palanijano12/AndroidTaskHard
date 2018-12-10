package com.android.androidtaskhard.datamodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class FoodItem implements Serializable {

    @SerializedName("item_name")
    @Expose
    private String foodName;
    @SerializedName("item_price")
    @Expose
    private String foodPrice;
    @SerializedName("average_rating")
    @Expose
    private String foodRating;
    @SerializedName("image_url")
    @Expose
    private String foodImageUrl;


    public FoodItem(String _foodName, String _foodPrice, String _foodRating, String _foodImageUrl) {
        this.foodName = _foodName;
        this.foodPrice = _foodPrice;
        this.foodRating = _foodRating;
        this.foodImageUrl = _foodImageUrl;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public String getFoodPrice() {
        return foodPrice;
    }

    public void setFoodPrice(String foodPrice) {
        this.foodPrice = foodPrice;
    }

    public String getFoodRating() {
        return foodRating;
    }

    public void setFoodRating(String foodRating) {
        this.foodRating = foodRating;
    }

    public String getFoodImageUrl() {
        return foodImageUrl;
    }

    public void setFoodImageUrl(String foodImageUrl) {
        this.foodImageUrl = foodImageUrl;
    }
}
