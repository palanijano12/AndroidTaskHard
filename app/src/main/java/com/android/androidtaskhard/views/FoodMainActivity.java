package com.android.androidtaskhard.views;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.android.androidtaskhard.R;
import com.android.androidtaskhard.adapter.FoodRecycleAdapter;
import com.android.androidtaskhard.database.CartItem;
import com.android.androidtaskhard.datamodel.FoodItem;
import com.android.androidtaskhard.viewmodel.CartViewModel;
import com.android.androidtaskhard.viewmodel.FoodViewModel;
import com.google.android.material.snackbar.Snackbar;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.PopupMenu;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LifecycleRegistry;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class FoodMainActivity extends AppCompatActivity implements LifecycleOwner, PopupMenu.OnMenuItemClickListener {

    private RecyclerView foodRecyclerView;
    private FoodRecycleAdapter foodRecycleAdapter;
    private LifecycleRegistry mLifecycleRegistry;
    private List<FoodItem> foodItemsList;
    private AppCompatTextView foodCartCount;
    private CartViewModel cartViewModel;

    private int cartFoodItemCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        intiViews();
        foodRecyclerView.setHasFixedSize(true);
        foodRecyclerView.setLayoutManager(new GridLayoutManager(this,2));

        mLifecycleRegistry = new LifecycleRegistry(this);
        mLifecycleRegistry.markState(Lifecycle.State.CREATED);

        cartViewModel = ViewModelProviders.of(this).get(CartViewModel.class);
        cartViewModel.getAllCartItems().observe(this, new Observer<List<CartItem>>() {
            @Override
            public void onChanged(List<CartItem> cartItemList) {
                cartFoodItemCount = 0;
                //cartFoodItemCount = cartItemList.size();  //Total number of Items
                for (CartItem cartItem : cartItemList) {
                    Log.d("FOOD ITEMS", cartItem.getFoodItemName() + "::" + cartItem.getFoodItemQuantity());
                    cartFoodItemCount = cartFoodItemCount + cartItem.getFoodItemQuantity();  //Total number of quantity Items
                }
                if (foodCartCount != null) {
                    foodCartCount.setText(String.valueOf(cartFoodItemCount));
                }
            }
        });

        FoodViewModel foodViewModel = ViewModelProviders.of(this).get(FoodViewModel.class);
        foodViewModel.getFoodItems().observe(this, new Observer<List<FoodItem>>() {
            @Override
            public void onChanged(List<FoodItem> foodItems) {
                foodRecycleAdapter = new FoodRecycleAdapter(FoodMainActivity.this, foodItems, cartViewModel);
                foodItemsList = foodItems;
                foodRecyclerView.setAdapter(foodRecycleAdapter);
            }
        });

    }

    private void intiViews() {
        foodRecyclerView = findViewById(R.id.foodsRecycleView);
    }

    @Override
    @NonNull
    public Lifecycle getLifecycle() {
        return mLifecycleRegistry;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.food_menu, menu);

        MenuItem menuItem = menu.findItem(R.id.navFoodCart);
        foodCartCount = menuItem.getActionView().findViewById(R.id.foodCartCounter);

        foodCartCount.setText(String.valueOf(cartFoodItemCount));

        menuItem.getActionView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (cartFoodItemCount == 0) {
                    ShowSnack(foodRecyclerView);
                } else {
                    startActivity(new Intent(FoodMainActivity.this, CartActivity.class));
                }
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.navFoodFilter: {
                showPopup(id);
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void setUpCartBadge() {

    }

    public void showPopup(int menuId) {

        PopupMenu popupMenu = new PopupMenu(this, findViewById(menuId));
        popupMenu.getMenuInflater().inflate(R.menu.filter_menu, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(this);
        popupMenu.show();
    }

    private void ShowSnack(View view) {
        Snackbar snackbar = Snackbar.make(view, "Cart is empty. Add items to the cart.", Snackbar.LENGTH_SHORT);
        snackbar.getView().setBackgroundColor(ContextCompat.getColor(this, R.color.colorAccent));
        AppCompatTextView tv = snackbar.getView().findViewById(com.google.android.material.R.id.snackbar_text);
        tv.setTextColor(Color.WHITE);
        snackbar.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        sortFoodItems(item.getItemId());
        return true;
    }

    private void sortFoodItems(int popItemId) {
        switch (popItemId) {
            case R.id.popFilterPrice: {
                Collections.sort(foodItemsList, new Comparator<FoodItem>() {
                    @Override
                    public int compare(FoodItem item1, FoodItem item2) {
                        return Float.compare(Float.parseFloat(item1.getFoodPrice()), Float.parseFloat(item2.getFoodPrice()));
                    }
                });
                foodRecycleAdapter.notifyDataSetChanged();
                break;
            }
            case R.id.popFilterRating: {
                Collections.sort(foodItemsList, new Comparator<FoodItem>() {
                    @Override
                    public int compare(FoodItem item1, FoodItem item2) {
                        return Float.compare(Float.parseFloat(item1.getFoodRating()), Float.parseFloat(item2.getFoodRating()));
                    }
                });
                foodRecycleAdapter.notifyDataSetChanged();
                break;
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mLifecycleRegistry.markState(Lifecycle.State.STARTED);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLifecycleRegistry.markState(Lifecycle.State.DESTROYED);
    }


}
