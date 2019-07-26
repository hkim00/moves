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

import com.hkim00.moves.models.Event;
import com.hkim00.moves.models.Move;
import com.hkim00.moves.models.Restaurant;
import com.hkim00.moves.models.UserLocation;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import com.lyft.lyftbutton.LyftButton;
import com.lyft.lyftbutton.RideParams;
import com.lyft.networking.ApiConfig;
import com.lyft.deeplink.RideTypeEnum;

import org.json.JSONArray;
import org.json.JSONObject;

import org.parceler.Parcels;

import cz.msebera.android.httpclient.Header;

//import util methods: JSONObject response formatters
import static com.hkim00.moves.util.JSONResponseHelper.getPriceRange;
import static com.hkim00.moves.util.JSONResponseHelper.getStartTime;

public class MoveDetailsActivity extends AppCompatActivity {

    private TextView tvMoveName;
    private TextView tvTime;
    private TextView tvGroupNum;
    private TextView tvDistance;
    private TextView tvPrice;
    private ImageView ivGroupNum;
    private ImageView ivTime;
    private ImageView ivPrice;
    private ImageView ivSave;
    private ImageView ivFavorite;
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
        lyftButton();

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
        ivPrice = findViewById(R.id.ivPrice);
        moveRating = findViewById(R.id.moveRating);
        btnChooseMove = findViewById(R.id.btnChooseMove);
        ivGroupNum = findViewById(R.id.ivGroupNum);
        btnFavorite = findViewById(R.id.btnFavorite);
        ivFavorite = findViewById(R.id.ivFavorite);
        btnSave = findViewById(R.id.btnSave);
        currUser = ParseUser.getCurrentUser();
        ivSave = findViewById(R.id.ivSave);
    }

    private void getFoodView() {
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
        tvMoveName.setText(event.name);
        String id = event.id;

        //hide groupNum and Time tv & iv
        ivGroupNum.setVisibility(View.INVISIBLE);
        tvGroupNum.setVisibility(View.INVISIBLE);

        // make call to Ticketmaster's event detail API
        getEventDetails(id);
    }

    private void getEventDetails(String id) {
        String API_BASE_URL_TMASTER = "https://app.ticketmaster.com/discovery/v2/events";
        String TAG = "MoveDetailsActivity";
        String apiUrl = API_BASE_URL_TMASTER + "/" + id + ".json";

        RequestParams params = new RequestParams();
        params.put("apikey", getString(R.string.api_key_tm));

        HomeActivity.clientTM.get(apiUrl, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                 String startTime = getStartTime(response);
                 String priceRange = getPriceRange(response);
                 tvTime.setText(startTime);
                 tvPrice.setText(priceRange);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.e(TAG, errorResponse.toString());
                throwable.printStackTrace();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                Log.e(TAG, errorResponse.toString());
                throwable.printStackTrace();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.e(TAG, responseString);
                throwable.printStackTrace();
            }
        });
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
                                currUser.addAllUnique("restaurantsCompleted", Arrays.asList(restaurant.name));
                                currUser.saveInBackground();

                                ParseObject currRestaurant = new ParseObject("Restaurant");
                                currRestaurant.put("name", restaurant.name);
                                currRestaurant.put("user", currUser);
                                currRestaurant.put("didComplete", true);
                                currRestaurant.put("priceLevel", restaurant.price_level);
                                currRestaurant.put("lat", restaurant.lat);
                                currRestaurant.put("lng", restaurant.lng);
                                currRestaurant.put("googleRating", restaurant.rating);
                                currRestaurant.saveInBackground();

                                Log.d("Move", "Move Saved in History Successfully");
                            } else {
                                Log.d("Move", "Error: saving move to history");
                            }
                        }
                    });
                }

                else if (event != null) {
                    ParseQuery<ParseObject> didCompleteQuery = ParseQuery.getQuery("Event");
                    didCompleteQuery.whereEqualTo("placeId", event.id);
                    didCompleteQuery.whereEqualTo("user", currUser);
                    didCompleteQuery.findInBackground(new FindCallback<ParseObject>() {
                        @Override
                        public void done(List<ParseObject> objects, ParseException e) {

                                if (e == null) {
                                    currUser.addAllUnique("eventsCompleted", Arrays.asList(event.name));
                                    currUser.saveInBackground();

                                    ParseObject currEvent = new ParseObject("Event");
                                    currEvent.put("name", event.name);
                                    currEvent.put("user", currUser);
                                    currEvent.put("didComplete", true);
                                    currEvent.saveInBackground();

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
                if (restaurant != null) {
                    ivSave.setImageResource(R.drawable.ufi_save_active);
                    ParseQuery<ParseObject> didSaveQuery = ParseQuery.getQuery("Restaurant");
                    didSaveQuery.whereEqualTo("name", restaurant.name);
                    didSaveQuery.whereEqualTo("user", currUser);
                    didSaveQuery.findInBackground(new FindCallback<ParseObject>() {
                        @Override
                        public void done(List<ParseObject> objects, ParseException e) {


                                if (objects.size()>0) {
                                    for (int i = 0; i < objects.size(); i++) {
                                        objects.get(i).put("didSave", true);
                                        objects.get(i).saveInBackground();
                                    }
                                }
                                else {
                                    ParseObject currRest = new ParseObject("Restaurant");
                                    currRest.put("name", restaurant.name);
                                    currRest.put("user", currUser);
                                    currRest.put("didSave", true);
                                    currRest.saveInBackground();
                                }

                            Log.d("MoveDetailsActivity", "saved move");
                        }

                    });

                }

                if (event != null) {
                    ivSave.setImageResource(R.drawable.ufi_save_active);
                        ParseQuery<ParseObject> didSaveQuery = ParseQuery.getQuery("Event");
                        didSaveQuery.whereEqualTo("name", event.name);
                        didSaveQuery.whereEqualTo("user", currUser);
                        didSaveQuery.findInBackground(new FindCallback<ParseObject>() {
                            @Override
                            public void done(List<ParseObject> objects, ParseException e) {
                                    if (objects.size()>0) {
                                        for (int i = 0; i < objects.size(); i++) {
                                            objects.get(0).put("didSave", true);
                                            objects.get(0).saveInBackground();
                                        }
                                    }else {
                                        ParseObject currEvent = new ParseObject("Event");
                                        currEvent.put("name", event.name);
                                        currEvent.put("user", currUser);
                                        currEvent.put("didSave", true);
                                        currEvent.saveInBackground();
                                        Log.d("MoveDetailsActivity", "added move");
                                    }


                                Log.d("MoveDetailsActivity", "saved move");
                            }

                        });
                    }
                }

        });

        btnFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ivFavorite.setImageResource(R.drawable.ufi_heart_active);
                if (restaurant != null) {
                    ParseQuery<ParseObject> didSaveQuery = ParseQuery.getQuery("Restaurant");
                    didSaveQuery.whereEqualTo("name", restaurant.name);
                    didSaveQuery.whereEqualTo("user", currUser);
                    didSaveQuery.findInBackground(new FindCallback<ParseObject>() {
                        @Override
                        public void done(List<ParseObject> objects, ParseException e) {

                            if (objects.size()>0) {
                                for (int i = 0; i < objects.size(); i++) {
                                    objects.get(i).put("didFavorite", true);
                                    objects.get(i).saveInBackground();
                                }
                            }
                            else {
                                ParseObject currRest = new ParseObject("Restaurant");
                                currRest.put("name", restaurant.name);
                                currRest.put("user", currUser);
                                currRest.put("didFavorite", true);
                                currRest.saveInBackground();
                                Log.d("MoveDetailsActivity", "added move");
                            }

                            Log.d("MoveDetailsActivity", "favorited move");
                        }

                    });
                }
                if (event != null) {
                    ParseQuery<ParseObject> didSaveQuery = ParseQuery.getQuery("Event");
                    didSaveQuery.whereEqualTo("name", event.name);
                    didSaveQuery.whereEqualTo("user", currUser);
                    didSaveQuery.findInBackground(new FindCallback<ParseObject>() {
                        @Override
                        public void done(List<ParseObject> objects, ParseException e) {
                            if (objects.size()>0) {
                                for (int i = 0; i < objects.size(); i++) {
                                    objects.get(0).put("didFavorite", true);
                                    objects.get(0).saveInBackground();
                                }
                            }else {
                                ParseObject currEvent = new ParseObject("Event");
                                currEvent.put("name", event.name);
                                currEvent.put("user", currUser);
                                currEvent.put("didFavorite", true);
                                currEvent.saveInBackground();
                                Log.d("MoveDetailsActivity", "added move");
                            }
                            Log.d("MoveDetailsActivity", "favorited move");
                        }

                    });
                }
            }
        });
    }


    private void lyftButton() {
        // add feature to call Lyft to event/restaurant
        ApiConfig apiConfig = new ApiConfig.Builder()
                .setClientId(getString(R.string.client_id_lyft))
                //waiting for Lyft to approve developer signup request
                .setClientToken("...")
                .build();

        LyftButton lyftButton = findViewById(R.id.lyft_button);
        lyftButton.setApiConfig(apiConfig);
        UserLocation currLocation = UserLocation.getCurrentLocation(this);

        RideParams.Builder rideParamsBuilder = new RideParams.Builder()
                .setPickupLocation(Double.valueOf(currLocation.lat), Double.valueOf(currLocation.lng))
                //TODO: add correct dropoff location once Lyft approves developer request
                .setDropoffLocation(37.759234, -122.4135125);
        rideParamsBuilder.setRideTypeEnum(RideTypeEnum.STANDARD);

        lyftButton.setRideParams(rideParamsBuilder.build());
        lyftButton.load();
    }

}
