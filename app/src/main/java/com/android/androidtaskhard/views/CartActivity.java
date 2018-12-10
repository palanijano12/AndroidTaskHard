package com.android.androidtaskhard.views;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.android.androidtaskhard.R;
import com.android.androidtaskhard.constants.Constants;
import com.android.androidtaskhard.database.CartItem;
import com.android.androidtaskhard.viewmodel.CartViewModel;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LifecycleRegistry;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

public class CartActivity extends AppCompatActivity implements LifecycleOwner, View.OnClickListener {

    private LifecycleRegistry lifecycleRegistry;

    private LinearLayout cartItemsLayout;
    private AppCompatTextView cartTotalPay;
    private MaterialButton buttonPay, buttonCouponApply;
    private AppCompatEditText editCouponCode;

    private float foodItemPrice = 0;
    private float totalItemPrice = 0;
    private String totalPay;
    private String couponCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        setTitle("Cart");
        initViews();

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        lifecycleRegistry = new LifecycleRegistry(this);
        lifecycleRegistry.markState(Lifecycle.State.CREATED);

        CartViewModel cartViewModel = ViewModelProviders.of(this).get(CartViewModel.class);
        cartViewModel.getAllCartItems().observe(this, new Observer<List<CartItem>>() {
            @Override
            public void onChanged(List<CartItem> cartItemList) {
                Log.d("CART ITEM COUNT", cartItemList.size() + "");

                for (CartItem cartItem : cartItemList) {


                    //foodItemPrice = Integer.parseInt(cartItem.getFoodItemPrice()) * cartItem.getFoodItemQuantity();

                    foodItemPrice = Float.parseFloat(cartItem.getFoodItemPrice()) * cartItem.getFoodItemQuantity();
                    createCartItemsLayout(cartItem.getFoodItemName(), String.valueOf(foodItemPrice), cartItem.getFoodItemQuantity(), cartItemList.indexOf(cartItem));
                    totalItemPrice = totalItemPrice + foodItemPrice;
                }
                totalItemPrice = totalItemPrice + Constants.DeliveryCharge;
                totalPay = getString(R.string.rupee_value) + String.valueOf(totalItemPrice);
                cartTotalPay.setText(totalPay);
            }
        });

        editCouponCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.length() > 0) {
                    buttonCouponApply.setEnabled(true);
                } else {
                    buttonCouponApply.setEnabled(false);
                }
                couponCode = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }


    private void initViews() {
        cartItemsLayout = findViewById(R.id.cartItemsLayout);
        cartTotalPay = findViewById(R.id.cartTotalView);
        buttonPay = findViewById(R.id.cartPay);
        buttonCouponApply = findViewById(R.id.cartCouponApply);
        editCouponCode = findViewById(R.id.cartEditCouponCode);

        buttonCouponApply.setOnClickListener(this);
        buttonPay.setOnClickListener(this);
    }


    private void createCartItemsLayout(String _foodName, String _foodPrice, int _quantity, int index) {

        LinearLayout foodLayoutItem = new LinearLayout(this);
        foodLayoutItem.setWeightSum(2.3f);
        foodLayoutItem.setOrientation(LinearLayout.HORIZONTAL);
        foodLayoutItem.setGravity(Gravity.CENTER_VERTICAL);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        int margin = (int) getResources().getDimension(R.dimen.foodItem_startEnd_margin);
        params.leftMargin = margin;
        params.rightMargin = margin;
        params.topMargin = margin;
        params.bottomMargin = margin;
        foodLayoutItem.setLayoutParams(params);


        AppCompatTextView foodItemNameView = new AppCompatTextView(this);
        LinearLayout.LayoutParams paramFoodItemName = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT);
        paramFoodItemName.weight = 1.0f;
        foodItemNameView.setLayoutParams(paramFoodItemName);
        foodItemNameView.setText(_foodName);
        foodItemNameView.setTextSize(18);


        AppCompatTextView foodItemQuantityView = new AppCompatTextView(this);
        foodItemQuantityView.setGravity(Gravity.CENTER);
        foodItemQuantityView.setBackgroundResource(R.drawable.cart_count_background);
        LinearLayout.LayoutParams paramFoodItemQuantity = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT);
        paramFoodItemQuantity.weight = 0.3f;
        foodItemQuantityView.setLayoutParams(paramFoodItemQuantity);
        foodItemQuantityView.setText(String.valueOf(_quantity));


        AppCompatTextView foodItemPriceView = new AppCompatTextView(this);
        foodItemPriceView.setGravity(Gravity.CENTER);
        LinearLayout.LayoutParams paramFoodItemPrice = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT);
        paramFoodItemPrice.weight = 1.0f;
        foodItemPriceView.setLayoutParams(paramFoodItemPrice);
        String price = getString(R.string.rupee_value) + _foodPrice;
        foodItemPriceView.setText(price);
        foodItemPriceView.setTextSize(18);
        foodItemPriceView.setTypeface(Typeface.DEFAULT_BOLD);


        foodLayoutItem.addView(foodItemNameView);
        foodLayoutItem.addView(foodItemQuantityView);
        foodLayoutItem.addView(foodItemPriceView);
        cartItemsLayout.addView(foodLayoutItem);
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
            case R.id.cartCouponApply: {
                if (couponCode.equals(Constants.CouponCode1) || couponCode.equals(Constants.CouponCode2)) {

                    if (couponCode.equals(Constants.CouponCode1) && totalItemPrice > 400 || couponCode.equals(Constants.CouponCode2) && totalItemPrice > 100) {


                        if (couponCode.equals(Constants.CouponCode2)) {
                            totalItemPrice = totalItemPrice - Constants.DeliveryCharge;
                            totalPay = getString(R.string.rupee_value) + String.valueOf(totalItemPrice);
                            cartTotalPay.setText(totalPay);
                            buttonCouponApply.setClickable(false);
                            buttonCouponApply.setEnabled(false);
                            editCouponCode.setEnabled(false);
                            editCouponCode.setClickable(false);
                            editCouponCode.setOnClickListener(null);
                            ShowSnack(v, "Coupon Applied Successfully");
                        } else {
                            totalItemPrice = totalItemPrice * 0.8f;
                            totalPay = getString(R.string.rupee_value) + String.valueOf(totalItemPrice);
                            cartTotalPay.setText(totalPay);
                            buttonCouponApply.setClickable(false);
                            buttonCouponApply.setEnabled(false);
                            editCouponCode.setEnabled(false);
                            editCouponCode.setClickable(false);
                            editCouponCode.setOnClickListener(null);
                            ShowSnack(v, "Coupon Applied Successfully");
                        }


                    } else {
                        if (couponCode.equals(Constants.CouponCode1)) {
                            ShowErrorSnack(v, "Minimum order greater than " + getString(R.string.rupee_value) + "400");
                        } else {
                            ShowSnack(v, "Minimum order greater than " + getString(R.string.rupee_value) + "100");
                        }
                    }
                } else {
                    ShowErrorSnack(v, "Invalid Coupon");
                }
                break;
            }


        }

    }

    private void ShowSnack(View view, String message) {
        Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_SHORT);
        snackbar.getView().setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        AppCompatTextView tv = snackbar.getView().findViewById(com.google.android.material.R.id.snackbar_text);
        tv.setTextColor(Color.WHITE);
        snackbar.show();
    }

    private void ShowErrorSnack(View view, String message) {
        Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_SHORT);
        snackbar.getView().setBackgroundColor(ContextCompat.getColor(this, R.color.colorBlack));
        AppCompatTextView tv = snackbar.getView().findViewById(com.google.android.material.R.id.snackbar_text);
        tv.setTextColor(Color.WHITE);
        snackbar.show();
    }

    @Override
    @NonNull
    public Lifecycle getLifecycle() {
        return lifecycleRegistry;
    }

    @Override
    protected void onStart() {
        super.onStart();
        lifecycleRegistry.markState(Lifecycle.State.STARTED);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        lifecycleRegistry.markState(Lifecycle.State.DESTROYED);
    }


}
