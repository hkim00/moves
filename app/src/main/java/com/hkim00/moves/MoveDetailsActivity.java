package com.hkim00.moves;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import java.util.*;

import androidx.appcompat.app.AppCompatActivity;

import com.hkim00.moves.models.Restaurant;


import org.parceler.Parcels;

public class MoveDetailsActivity extends AppCompatActivity {

    private TextView tvMoveName;
    private TextView tvTime;
    private TextView tvGroupNum;
    private TextView tvDistance;
    private TextView tvPrice;
    private ImageView ivGroupNum;
    private ImageView ivTime;
    private RatingBar moveRating;
    private Button btnChooseMove;


    Restaurant restaurant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_move_details);

        getViewIds();

        //if category == Food, then:
        //TODO repeat for other categories when models are created
        getFoodView();


        btnChooseMove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO save move to history
            }
        });


    }

    private void getViewIds() {
        tvMoveName = findViewById(R.id.tvMoveName);
        tvTime = findViewById(R.id.tvTime);
        ivTime = findViewById(R.id.ivTime);
        tvGroupNum = findViewById(R.id.tvGroupNum);
        tvDistance = findViewById(R.id.tvDistance);
        tvPrice = findViewById(R.id.tvPrice);
        moveRating = findViewById(R.id.moveRating);
        btnChooseMove = findViewById(R.id.btnChooseMove);
        ivGroupNum = findViewById(R.id.ivGroupNum);

    }

    private void getFoodView() {
        //unwrap the restaurant passed in
        restaurant = (Restaurant) Parcels.unwrap(getIntent().getParcelableExtra("move"));
        Log.d("MovieDetailsActivity", String.format("Showing details for '%s'", restaurant.name));

        //set details
        tvMoveName.setText(restaurant.name);
        String price = "";

        if (restaurant.price_level < 0) {
            price = "Unknown";
        } else {
            for (int i = 0; i < restaurant.price_level; i++) {
                price += '$';
            }
        }

        tvPrice.setText(price);
        //hide groupNum and Time tv & iv
        ivGroupNum.setVisibility(View.INVISIBLE);
        tvGroupNum.setVisibility(View.INVISIBLE);
        ivTime.setVisibility(View.INVISIBLE);
        tvTime.setVisibility(View.INVISIBLE);


        if (restaurant.rating < 0) {
            moveRating.setVisibility(View.INVISIBLE);
        } else {
            float moveRate = restaurant.rating.floatValue();
            moveRating.setRating(moveRate = moveRate > 0 ? moveRate / 2.0f : moveRate);
        }
    }

    private void getActivityView() {
        //unwrap activity passed in
        //set details
    }

    private void getAttractionView() {
        //unwrap activity passed in
        //set details
    }

    private void getEventView() {
        //unwrap activity passed in
        //set details
    }
}
