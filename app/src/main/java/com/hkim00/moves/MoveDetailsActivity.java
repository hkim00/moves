package com.hkim00.moves;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.*;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.hkim00.moves.fragments.HistoryFragment;
import com.hkim00.moves.models.Event;
import com.hkim00.moves.models.Move;
import com.hkim00.moves.models.Restaurant;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;


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
    private Button btnFavorite;
    private Button btnSave;

    ParseUser currUser;
    Restaurant restaurant;
    Event event;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_move_details);

        getViewIds();
        ButtonsSetUp();

        Move move = Parcels.unwrap(getIntent().getParcelableExtra("move"));

        if (move.getMoveType() == Move.RESTAURANT) {
            restaurant = (Restaurant) move;
            getFoodView();
        } else {
            event = (Event) move;
            getEventView();
        }

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
        btnFavorite = findViewById(R.id.btnFavorite);
        btnSave = findViewById(R.id.btnSave);
        currUser = ParseUser.getCurrentUser();

    }

    private void getFoodView() {
        //unwrap the restaurant passed in
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

        tvDistance.setText(restaurant.distanceFromLocation(getApplicationContext()) + " mi");

        if (restaurant.rating < 0) {
            moveRating.setVisibility(View.INVISIBLE);
        } else {
            float moveRate = restaurant.rating.floatValue();
            moveRating.setRating(moveRate = moveRate > 0 ? moveRate / 2.0f : moveRate);
        }
    }

    private void getEventView() {
        Log.d("MovieDetailsActivity", String.format("Showing details for '%s'", event.name));
        tvMoveName.setText(event.name);
        //TODO set other details or hide unnecessary details 


    }



    private void ButtonsSetUp() {
        btnChooseMove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (restaurant != null) {
                    ParseQuery<ParseObject> didCompleteQuery = ParseQuery.getQuery("Restaurant");
                    didCompleteQuery.whereEqualTo("placeId", restaurant.id);
                    didCompleteQuery.whereEqualTo("user", currUser);
                    didCompleteQuery.findInBackground(new FindCallback<ParseObject>() {
                        @Override
                        public void done(List<ParseObject> objects, ParseException e) {
                            if (e == null) {
                                for (int i = 0; i < objects.size(); i++) {
                                    objects.get(i).put("didComplete", "true");
                                    objects.get(i).saveInBackground();
                                }
                                Log.d("Move", "Move Saved in History Successfully");
                            } else {
                                Log.d("Move", "Error: saving move to history");
                            }
                        }
                    });
                }

                if (event != null) {
                    ParseQuery<ParseObject> didCompleteQuery = ParseQuery.getQuery("Event");
                    didCompleteQuery.whereEqualTo("placeId", event.id);
                    didCompleteQuery.whereEqualTo("user", currUser);
                    didCompleteQuery.findInBackground(new FindCallback<ParseObject>() {
                        @Override
                        public void done(List<ParseObject> objects, ParseException e) {
                            if (e == null) {
                                for (int i = 0; i < objects.size(); i++) {
                                    objects.get(i).put("didComplete", "true");
                                    objects.get(i).saveInBackground();
                                }
                                Log.d("Move", "Move Saved in History Successfully");
                            } else {
                                Log.d("Move", "Error: saving move to history");
                            }
                        }
                    });
                }

                Toast.makeText(getApplicationContext(), "Added to History", Toast.LENGTH_SHORT).show();
            }
        });


        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO add moves to saved
            }
        });
    }

}
