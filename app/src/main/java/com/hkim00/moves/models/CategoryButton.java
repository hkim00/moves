package com.hkim00.moves.models;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.hkim00.moves.R;



public class CategoryButton {
    public String cuisine;
    public String cuisineImageURL;
    public Boolean isPref = false;

    public CategoryButton (String cuisine, String cuisineImageURL) {
        this.cuisine = cuisine;
        this.cuisineImageURL = cuisineImageURL;
    }
}
