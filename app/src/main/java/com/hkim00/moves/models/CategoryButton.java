package com.hkim00.moves.models;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.hkim00.moves.R;

public class CategoryButton extends AppCompatActivity {
    public ImageView ivCuisine;
    public TextView tvCuisine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        tvCuisine = findViewById(R.id.cuisine_tv);
        ivCuisine = findViewById(R.id.cuisine_iv);
    }
}
