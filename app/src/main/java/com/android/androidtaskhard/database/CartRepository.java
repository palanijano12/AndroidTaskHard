package com.android.androidtaskhard.database;

import android.app.Application;
import android.os.AsyncTask;

import com.android.androidtaskhard.model.CartUpdateItem;

import java.util.List;

import androidx.lifecycle.LiveData;

public class CartRepository{

    private CartItemDao cartItemDao;
    private LiveData<List<CartItem>> cartList;

    public CartRepository(Application application) {

        CartRoomDatabase db = CartRoomDatabase.getDatabase(application);
        cartItemDao = db.cartItemDao();
        cartList = cartItemDao.getAllCartItems();
    }

    public LiveData<List<CartItem>> getAllItemsFromCartRepository() {
        return cartList;
    }

    public void addCartItem(CartItem cartItem)
    {
       new AddCartAsyncTask(cartItemDao).execute(cartItem);
    }

    public void removeCartItem(String itemName)
    {
        new RemoveCartAsyncTask(cartItemDao).execute(itemName);
    }

    public void updateCartQuantity(CartUpdateItem cartUpdateItem)
    {
        new UpdateCartAsyncTask(cartItemDao).execute(cartUpdateItem);
    }


    private static class AddCartAsyncTask extends AsyncTask<CartItem,Void,Void> {

        private CartItemDao asyncCartItemDao;

        AddCartAsyncTask(CartItemDao cartItemDao){
            this.asyncCartItemDao = cartItemDao;
        }

        @Override
        protected Void doInBackground(CartItem... cartItems) {
            asyncCartItemDao.addCartItem(cartItems[0]);
            return null;
        }
    }

    private static class RemoveCartAsyncTask extends AsyncTask<String,Void,Void> {

        private CartItemDao asyncCartItemDao;

        RemoveCartAsyncTask(CartItemDao cartItemDao){
            this.asyncCartItemDao = cartItemDao;
        }

        @Override
        protected Void doInBackground(String... strings) {
            asyncCartItemDao.removeFoodItem(strings[0]);
            return null;
        }
    }

    private static class UpdateCartAsyncTask extends AsyncTask<CartUpdateItem,Void,Void> {

        private CartItemDao asyncCartItemDao;

        UpdateCartAsyncTask(CartItemDao cartItemDao){
            this.asyncCartItemDao = cartItemDao;
        }

        @Override
        protected Void doInBackground(CartUpdateItem... cartUpdateItems) {
            asyncCartItemDao.updateQuantity(cartUpdateItems[0].getCartFoodItemName(),cartUpdateItems[0].getQuantity());
            return null;
        }
    }
}
