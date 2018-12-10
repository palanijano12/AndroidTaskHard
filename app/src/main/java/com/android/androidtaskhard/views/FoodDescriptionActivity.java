package com.android.androidtaskhard.views;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.android.androidtaskhard.R;
import com.android.androidtaskhard.constants.Constants;
import com.android.androidtaskhard.database.CartItem;
import com.android.androidtaskhard.datamodel.FoodItem;
import com.android.androidtaskhard.model.CartUpdateItem;
import com.android.androidtaskhard.viewmodel.CartViewModel;
import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LifecycleRegistry;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

public class FoodDescriptionActivity extends AppCompatActivity implements View.OnClickListener, LifecycleOwner {

    private AppCompatImageView descFoodImageView;
    private AppCompatTextView descFoodNameView, descFoodPriceView, descFoodRatingView, descFoodCartCount, descFoodButtonCount;
    private MaterialButton descButtonAdd, descButtonMinus;
    private CartViewModel cartViewModel;
    private LifecycleRegistry mLifecycleRegistry;
    private int cartFoodItemCount;
    private FoodItem foodItem;
    private String buttonFoodItemCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_description);
        initViews();

        descButtonAdd.setOnClickListener(this);
        descButtonMinus.setOnClickListener(this);

        mLifecycleRegistry = new LifecycleRegistry(this);
        mLifecycleRegistry.markState(Lifecycle.State.CREATED);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        foodItem = (FoodItem) getIntent().getSerializableExtra("foodItem");
        buttonFoodItemCount = getIntent().getStringExtra("foodItemQuantity");
        descFoodButtonCount.setText(buttonFoodItemCount);

        if (foodItem != null) {
            Glide.with(this)
                    .load(foodItem.getFoodImageUrl())
                    .into(descFoodImageView);
            descFoodNameView.setText(foodItem.getFoodName());
            setTitle(foodItem.getFoodName());
            String priceText = getString(R.string.rupee_value) + " " + foodItem.getFoodPrice();
            descFoodPriceView.setText(priceText);
            descFoodRatingView.setText(foodItem.getFoodRating());
        }

        cartViewModel = ViewModelProviders.of(this).get(CartViewModel.class);
        cartViewModel.getAllCartItems().observe(this, new Observer<List<CartItem>>() {
            @Override
            public void onChanged(List<CartItem> cartItemList) {
                cartFoodItemCount = 0;
                //cartFoodItemCount = cartItemList.size();  //Total number of Items
                for (CartItem cartItem : cartItemList) {
                    cartFoodItemCount = cartFoodItemCount + cartItem.getFoodItemQuantity();  //Total number of quantity Items
                    if (cartItem.getFoodItemName().equals(foodItem.getFoodName())) {
                        buttonFoodItemCount = "" + cartItem.getFoodItemQuantity();
                        descFoodButtonCount.setText(buttonFoodItemCount);
                    }
                }
                if (descFoodCartCount != null) {
                    descFoodCartCount.setText(String.valueOf(cartFoodItemCount));
                }

                if (buttonFoodItemCount.equals("0")) {
                    descButtonMinus.setEnabled(false);
                } else {
                    descButtonMinus.setEnabled(true);
                }
            }
        });


    }

    private void initViews() {
        descFoodImageView = findViewById(R.id.descFoodImageView);
        descFoodNameView = findViewById(R.id.descFoodNameView);
        descFoodPriceView = findViewById(R.id.descFoodPriceView);
        descFoodRatingView = findViewById(R.id.descFoodRatingView);
        descButtonAdd = findViewById(R.id.descFoodAddButton);
        descButtonMinus = findViewById(R.id.descFoodMinusButton);
        descFoodButtonCount = findViewById(R.id.descFoodItemCount);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.food_menu, menu);
        if (menu != null) {
            menu.getItem(0).setVisible(false);
            MenuItem menuItem = menu.findItem(R.id.navFoodCart);
            descFoodCartCount = menuItem.getActionView().findViewById(R.id.foodCartCounter);

            descFoodCartCount.setText(String.valueOf(cartFoodItemCount));

            menuItem.getActionView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(cartFoodItemCount == 0)
                    {
                        ShowSnack(descButtonAdd);
                    }
                    else {
                        startActivity(new Intent(FoodDescriptionActivity.this, CartActivity.class));
                    }

                }
            });
        }
        return true;
    }


    private void ShowSnack(View view)
    {
        Snackbar snackbar = Snackbar.make(view, "Cart is empty. Add items to the cart.", Snackbar.LENGTH_SHORT);
        snackbar.getView().setBackgroundColor(ContextCompat.getColor(this,R.color.colorAccent));
        AppCompatTextView tv =  snackbar.getView().findViewById(com.google.android.material.R.id.snackbar_text);
        tv.setTextColor(Color.WHITE);
        snackbar.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                finish();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.descFoodAddButton: {
                if (buttonFoodItemCount.equals("0")) {
                    cartViewModel.addCartItem(new CartItem(Constants.CartId, foodItem.getFoodName(), foodItem.getFoodPrice(), 1));
                } else {
                    int quantity = Integer.parseInt(buttonFoodItemCount) + 1;
                    cartViewModel.updateQuantity(new CartUpdateItem(foodItem.getFoodName(), quantity));
                }

                break;
            }
            case R.id.descFoodMinusButton: {
                if (buttonFoodItemCount.equals("1")) {
                    cartViewModel.RemoveCartItem(foodItem.getFoodName());
                    buttonFoodItemCount = "0";
                    descFoodButtonCount.setText(buttonFoodItemCount);
                } else {
                    int quantity = Integer.parseInt(buttonFoodItemCount) - 1;
                    cartViewModel.updateQuantity(new CartUpdateItem(foodItem.getFoodName(), quantity));
                }
                break;
            }
        }
    }

    @Override
    @NonNull
    public Lifecycle getLifecycle() {
        return mLifecycleRegistry;
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
