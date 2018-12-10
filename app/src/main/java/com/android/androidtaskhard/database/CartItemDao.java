package com.android.androidtaskhard.database;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface CartItemDao {

    @Insert
    void addCartItem(CartItem cartItem);

    @Query("SELECT * FROM user_cart")
    LiveData<List<CartItem>> getAllCartItems();

    @Query("DELETE FROM user_cart WHERE foodItemName = :itemName")
    void removeFoodItem(String itemName);

    @Query("UPDATE user_cart SET foodItemQuantity = :quantity WHERE foodItemName =:foodItemName")
    void updateQuantity(String foodItemName,int quantity);
}
