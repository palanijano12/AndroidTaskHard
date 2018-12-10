package com.android.androidtaskhard.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;


@Database(entities = {CartItem.class},version = 1)
public abstract class CartRoomDatabase extends RoomDatabase {

    public abstract CartItemDao cartItemDao();

    private static CartRoomDatabase cartRoomDatabase;

    static CartRoomDatabase getDatabase(Context context) {
        if (cartRoomDatabase == null) {
            synchronized (CartRoomDatabase.class) {
                if (cartRoomDatabase == null) {
                    cartRoomDatabase =
                            Room.databaseBuilder(context.getApplicationContext(),
                                    CartRoomDatabase.class, "cart_database")
                                    .build();

                }
            }
        }
        return cartRoomDatabase;
    }
}
