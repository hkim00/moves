package com.hkim00.moves;

import androidx.appcompat.app.AppCompatActivity;

import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.hkim00.moves.models.Restaurant;

import org.parceler.Parcels;

public class MoveDetailsActivity extends AppCompatActivity {

    private TextView tvMoveName;
    private TextView tvTime;
    private TextView tvGroupNum;
    private TextView tvDistance;
    private TextView tvPrice;
    private TextView tvCuisine;
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
        tvCuisine = findViewById(R.id.tvCuisine);
    }

    private void getFoodView() {
        //unwrap the restaurant passed in
        restaurant = (Restaurant) Parcels.unwrap(getIntent().getParcelableExtra(Restaurant.class.getSimpleName()));
        Log.d("MovieDetailsActivity", String.format("Showing details for '%s'", restaurant.name));

        //set details
        tvMoveName.setText(restaurant.name);
        tvPrice.setText(restaurant.price_level * '$');
        //hide groupNum and Time tv & iv
        ivGroupNum.setVisibility(View.INVISIBLE);
        tvGroupNum.setVisibility(View.INVISIBLE);
        ivTime.setVisibility(View.INVISIBLE);
        tvTime.setVisibility(View.INVISIBLE);
        //show cuisine
        tvCuisine.setVisibility(View.VISIBLE);
        tvCuisine.setText(restaurant.cuisine);


        float moveRate = restaurant.rating.floatValue();
        moveRating.setRating(moveRate = moveRate > 0 ? moveRate / 2.0f : moveRate);
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
