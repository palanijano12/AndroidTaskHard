package com.android.androidtaskhard.model;

public class CartUpdateItem {

    private String cartFoodItemName;

    private int cartFooditemQuantity;

    public CartUpdateItem() {

    }

    public CartUpdateItem(String _foodItemName, int _quantity) {
        this.cartFoodItemName = _foodItemName;
        this.cartFooditemQuantity = _quantity;
    }

    public String getCartFoodItemName() {
        return cartFoodItemName;
    }

    public int getQuantity() {
        return cartFooditemQuantity;
    }
}
