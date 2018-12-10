package com.android.androidtaskhard.database;


import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "user_cart")
public class CartItem {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "userId")
    private int UserId;

    @ColumnInfo(name = "foodCartId")
    private int foodCartId;

    @ColumnInfo(name = "foodItemName")
    private String foodItemName;

    @ColumnInfo(name = "foodItemPrice")
    private String foodItemPrice;

    @ColumnInfo(name = "foodItemQuantity")
    private int foodItemQuantity;

    public CartItem() {
    }

    public CartItem(int _cartId, String _name, String _price, int _quantity) {
        this.foodCartId = _cartId;
        this.foodItemName = _name;
        this.foodItemPrice = _price;
        this.foodItemQuantity = _quantity;
    }

    @NonNull
    public int getUserId() {
        return UserId;
    }

    public void setUserId(@NonNull int userId) {
        UserId = userId;
    }

    public int getFoodCartId() {
        return foodCartId;
    }

    public void setFoodCartId(int foodCartId) {
        this.foodCartId = foodCartId;
    }

    public String getFoodItemName() {
        return foodItemName;
    }

    public void setFoodItemName(String foodItemName) {
        this.foodItemName = foodItemName;
    }

    public String getFoodItemPrice() {
        return foodItemPrice;
    }

    public void setFoodItemPrice(String foodItemPrice) {
        this.foodItemPrice = foodItemPrice;
    }

    public int getFoodItemQuantity() {
        return foodItemQuantity;
    }

    public void setFoodItemQuantity(int foodItemQuantity) {
        this.foodItemQuantity = foodItemQuantity;
    }
}
