package com.android.androidtaskhard.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.androidtaskhard.R;
import com.android.androidtaskhard.constants.Constants;
import com.android.androidtaskhard.database.CartItem;
import com.android.androidtaskhard.datamodel.FoodItem;
import com.android.androidtaskhard.model.CartUpdateItem;
import com.android.androidtaskhard.viewmodel.CartViewModel;
import com.android.androidtaskhard.views.FoodDescriptionActivity;
import com.android.androidtaskhard.views.FoodMainActivity;
import com.bumptech.glide.Glide;
import com.google.android.material.card.MaterialCardView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

public class FoodRecycleAdapter extends RecyclerView.Adapter<FoodRecycleAdapter.FoodViewHolder> {


    private Context context;
    private List<FoodItem> foodList;
    private CartViewModel cartViewModel;

    public FoodRecycleAdapter(Context _context, List<FoodItem> _foodItems, CartViewModel _cartViewModel) {
        this.context = _context;
        this.foodList = _foodItems;
        this.cartViewModel = _cartViewModel;
    }

    @NonNull
    @Override
    public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_food_item, parent, false);
        return new FoodViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final FoodViewHolder holder, int position) {

        final FoodItem foodItem = foodList.get(holder.getAdapterPosition());

        Glide.with(context)
                .load(foodItem.getFoodImageUrl())
                .into(holder.foodImageView);

        holder.foodNameView.setText(foodItem.getFoodName());
        String priceText = context.getString(R.string.rupee_value) + foodItem.getFoodPrice();
        holder.foodPriceView.setText(priceText);
        holder.foodRatingView.setText(foodItem.getFoodRating());

        holder.foodCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(v.getContext(), FoodDescriptionActivity.class).putExtra("foodItem", foodItem).putExtra("foodItemQuantity", "" + holder.foodCardView.getTag()));
            }
        });

        holder.foodItemAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Log.d("CARD_TAG",holder.foodCardView.getTag()+"");

                if (holder.foodCardView.getTag().equals("0")) {
                    cartViewModel.addCartItem(new CartItem(Constants.CartId, foodItem.getFoodName(), foodItem.getFoodPrice(), 1));
                } else {
                    cartViewModel.updateQuantity(new CartUpdateItem(foodItem.getFoodName(), Integer.parseInt("" + holder.foodCardView.getTag()) + 1));
                }

            }
        });

        holder.foodItemMinusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.foodCardView.getTag().equals("1")) {
                    cartViewModel.RemoveCartItem(foodItem.getFoodName());
                    holder.foodCardView.setTag("0");
                } else {
                    int quantity = Integer.parseInt("" + holder.foodCardView.getTag()) - 1;
                    cartViewModel.updateQuantity(new CartUpdateItem(foodItem.getFoodName(), quantity));
                }
            }
        });

        cartViewModel.getAllCartItems().observe((FoodMainActivity) context, new Observer<List<CartItem>>() {
            @Override
            public void onChanged(List<CartItem> cartItemList) {
                for (CartItem cartItem : cartItemList) {
                    if (cartItem.getFoodItemName().equals(foodItem.getFoodName())) {
                        holder.foodCardView.setTag("" + cartItem.getFoodItemQuantity());
                        break;
                    } else {
                        holder.foodCardView.setTag("0");
                    }
                }
                if (cartItemList.size() == 0) {
                    holder.foodCardView.setTag("0");
                }

                if (holder.foodCardView.getTag().equals("0")) {
                    holder.foodItemMinusButton.setEnabled(false);
                } else {
                    holder.foodItemMinusButton.setEnabled(true);
                }

                holder.foodCartCountView.setText(String.valueOf(holder.foodCardView.getTag()));
            }
        });


    }

    @Override
    public int getItemCount() {
        return foodList.size();
    }

    class FoodViewHolder extends RecyclerView.ViewHolder {

        private AppCompatImageView foodImageView;
        private AppCompatTextView foodNameView, foodRatingView, foodPriceView, foodCartCountView;
        private MaterialCardView foodCardView;
        private AppCompatImageButton foodItemAddButton, foodItemMinusButton;

        private FoodViewHolder(@NonNull View itemView) {
            super(itemView);
            foodCardView = itemView.findViewById(R.id.foodCard);
            foodImageView = itemView.findViewById(R.id.foodImageView);
            foodNameView = itemView.findViewById(R.id.foodNameView);
            foodPriceView = itemView.findViewById(R.id.foodPriceView);
            foodRatingView = itemView.findViewById(R.id.foodRatingView);
            foodItemAddButton = itemView.findViewById(R.id.foodAddButton);
            foodItemMinusButton = itemView.findViewById(R.id.foodMinusButton);
            foodCartCountView = itemView.findViewById(R.id.foodCartRowView);
        }
    }
}
