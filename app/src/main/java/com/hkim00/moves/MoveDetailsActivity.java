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

import com.hkim00.moves.util.ParseUtil;
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
import org.json.JSONException;
import org.json.JSONObject;

import org.parceler.Parcels;

import cz.msebera.android.httpclient.Header;

//import util methods: JSONObject response formatters
import static com.hkim00.moves.util.JSONResponseHelper.getPriceRange;
import static com.hkim00.moves.util.JSONResponseHelper.getStartTime;

public class MoveDetailsActivity extends AppCompatActivity {

    private final static String TAG = "MoveDetailsActivity";

    private TextView tvMoveName, tvTime, tvGroupNum, tvDistance, tvPrice;
    private ImageView ivGroupNum, ivTime, ivPrice, ivSave, ivFavorite;
    private RatingBar moveRating;
    private Button btnChooseMove, btnFavorite, btnSave, btnAddToTrip;

    private ParseUser currUser;
    private Move move;
    private Restaurant restaurant;
    private Event event;
    private boolean isTrip;

    private List<Move> selectedMoves;
    private List<Move> newSelectedMoves;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_move_details);

        getViewIds();

        setupButtons();

        lyftButton();

        getMove();
    }


    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.left_in, R.anim.right_out);
    }

    private void getMove() {
        move = Parcels.unwrap(getIntent().getParcelableExtra("move"));
        if (move.getMoveType() == Move.RESTAURANT) {
            restaurant = (Restaurant) move;
            getFoodView();
        } else {
            event = (Event) move;
            getEventView();
        }

        isTrip = getIntent().getBooleanExtra("isTrip", false);
        btnAddToTrip.setVisibility(isTrip ? View.VISIBLE : View.INVISIBLE);
        if (isTrip) {
            selectedMoves = TripActivity.selectedMoves;
            newSelectedMoves = TripActivity.newSelectedMoves;

            if (selectedMoves.contains(move)) {
                btnAddToTrip.setText("Remove From Trip");
            } else {
                btnAddToTrip.setText("Add To Trip");
            }
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

        btnAddToTrip = findViewById(R.id.btnAddToTrip);
    }

    private void getFoodView() {
         tvMoveName.setText(restaurant.name);

         if (restaurant.lat == null) {
             getFoodDetails();
             return;
         }

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

    private void getFoodDetails() {
        String apiUrl = "https://maps.googleapis.com/maps/api/place/details/json";

        RequestParams params = new RequestParams();
        params.put("placeid", restaurant.id);
        params.put("key", getString(R.string.api_key));

        HomeActivity.client.get(apiUrl, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                JSONObject result;
                try {
                    result = response.getJSONObject("result");

                    Restaurant restaurantResult = Restaurant.fromJSON(result);
                    restaurant = restaurantResult;

                    if (restaurant.lat != null) {
                        getFoodView();
                    }

                } catch (JSONException e) {
                    Log.e(TAG, "Error getting restaurant");

                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Log.e(TAG, errorResponse.toString());
                throwable.printStackTrace();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Log.e(TAG, errorResponse.toString());
                throwable.printStackTrace();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Log.e(TAG, responseString);
                throwable.printStackTrace();
            }
        });
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

    private void setupButtons() {
        btnChooseMove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (restaurant != null) {
                    ParseQuery didCompleteQuery = ParseUtil.getParseQuery("food", currUser, false, restaurant);
                    didCompleteQuery.findInBackground(new FindCallback<ParseObject>() {
                        @Override
                        public void done(List<ParseObject> objects, ParseException e) {
                            if (e == null) {
                                currUser.addAllUnique("restaurantsCompleted", Arrays.asList(restaurant.name));
                                currUser.saveInBackground();
                                if (objects.size() == 0) {
                                    ParseObject currRestaurant = new ParseObject("Move");
                                    currRestaurant.put("name", restaurant.name);
                                    currRestaurant.put("placeId", restaurant.id);
                                    currRestaurant.put("moveType", "food");
                                    currRestaurant.put("user", currUser);
                                    currRestaurant.put("didComplete", true);
                                    currRestaurant.saveInBackground();
                                } else {
                                    for (int i = 0; i < objects.size(); i++) {
                                        objects.get(i).put("didComplete", true);
                                        objects.get(i).put("didSave", false); // user cannot save a move that has been done
                                        objects.get(i).saveInBackground();
                                    }
                                }


                                Log.d("Move", "Move Saved in History Successfully");
                            } else {

                                Log.d("Move", "Error: saving move to history");
                            }
                        }
                    });
                }

                else if (event != null) {
                    ParseQuery didCompleteQuery = ParseUtil.getParseQuery("event", currUser, false, event);
                    didCompleteQuery.findInBackground(new FindCallback<ParseObject>() {
                        @Override
                        public void done(List<ParseObject> objects, ParseException e) {
                                if (e == null) {
                                    currUser.addAllUnique("eventsCompleted", Arrays.asList(event.name));
                                    currUser.saveInBackground();

                                    ParseObject currEvent = new ParseObject("Move");
                                    currEvent.put("name", event.name);
                                    currEvent.put("placeId", event.id);
                                    currEvent.put("moveType", "event");
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
                    ParseQuery didSaveQuery = ParseUtil.getParseQuery("food", currUser, true, restaurant);
                    didSaveQuery.findInBackground(new FindCallback<ParseObject>() {
                        @Override
                        public void done(List<ParseObject> objects, ParseException e) {
                            ParseUtil.getDidSave("food", objects, restaurant);
                        }
                    });
                }

                if (event != null) {
                    ivSave.setImageResource(R.drawable.ufi_save_active);
                    ParseQuery didSaveQuery = ParseUtil.getParseQuery("Event", currUser, true, event);
                    didSaveQuery.findInBackground(new FindCallback<ParseObject>() {
                        @Override
                        public void done(List<ParseObject> objects, ParseException e) {
                            ParseUtil.getDidSave("Event", objects, event);
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
                    ParseQuery didFavoriteQuery = ParseUtil.getParseQuery("food", currUser, true, restaurant);
                    didFavoriteQuery.findInBackground(new FindCallback<ParseObject>() {
                        @Override
                        public void done(List<ParseObject> objects, ParseException e) {
                            ParseUtil.getDidFavorite("food", objects, restaurant);
                        }

                    });
                }
                if (event != null) {
                    ParseQuery didFavoriteQuery = ParseUtil.getParseQuery("event", currUser, true, event);
                    didFavoriteQuery.findInBackground(new FindCallback<ParseObject>() {
                        @Override
                        public void done(List<ParseObject> objects, ParseException e) {
                            ParseUtil.getDidFavorite("event", objects, event);
                        }
                    });
                }
            }
        });

        btnAddToTrip.setOnClickListener(view -> saveToTrip());
    }

    private void saveToTrip() {
        if (TripActivity.isEditingTrip) {
            if (!newSelectedMoves.contains(move)) {
                newSelectedMoves.add(move);
            } else {
                newSelectedMoves.remove(move);
            }
        }

        if (!selectedMoves.contains(move)) {
            selectedMoves.add(move);
            btnAddToTrip.setText("Remove From Trip");
        } else {
            selectedMoves.remove(move);
            btnAddToTrip.setText("Add To Trip");
        }
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
