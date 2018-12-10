package com.android.androidtaskhard.viewmodel;

import android.app.Application;

import com.android.androidtaskhard.database.CartItem;
import com.android.androidtaskhard.database.CartRepository;
import com.android.androidtaskhard.model.CartUpdateItem;

import java.util.List;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class CartViewModel extends AndroidViewModel {

    private CartRepository cartRepository;

    private LiveData<List<CartItem>> cartItems;


    public CartViewModel(Application application) {
        super(application);
        cartRepository = new CartRepository(application);
        cartItems = cartRepository.getAllItemsFromCartRepository();
    }

    public void addCartItem(CartItem cartItem) {
        cartRepository.addCartItem(cartItem);
    }

    public void RemoveCartItem(String FoodName) {
        cartRepository.removeCartItem(FoodName);
    }

    public void updateQuantity(CartUpdateItem cartUpdateItem) {
        cartRepository.updateCartQuantity(cartUpdateItem);
    }

    public LiveData<List<CartItem>> getAllCartItems() {
        return cartItems;
    }

}
