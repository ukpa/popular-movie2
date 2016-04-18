package me.unnikrishnanpatel.popular_movie2;

import android.content.Context;

import com.squareup.picasso.Picasso;

/**
 * Created by unnikrishnanpatel on 18/04/16.
 */
public class PabloPicasso {
    private static Picasso instance;

    public static Picasso with(Context context) {
        if (instance == null) {
            instance = new Picasso.Builder(context.getApplicationContext()).build();
        }
        return instance;
    }

    private PabloPicasso() {
        throw new AssertionError("No instances.");
    }
}