package com.hkim00.moves;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.*;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.hkim00.moves.adapters.MoveDetailsAdapter;
import com.hkim00.moves.adapters.UserAdapter;
import com.hkim00.moves.models.Cuisine;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import com.hkim00.moves.fragments.SearchFragment;

import com.hkim00.moves.models.Event;
import com.hkim00.moves.models.Move;
import com.hkim00.moves.models.MoveText;
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
import static com.hkim00.moves.util.ParseUtil.getParseQuery;

public class MoveDetailsActivity extends AppCompatActivity {

    private final static String TAG = "MoveDetailsActivity";

    private RecyclerView rvMove;
    private MoveDetailsAdapter adapter;
    private Move move;
    private List<Move> moves;


    private boolean isTrip;
    private List<Move> selectedMoves, newSelectedMoves, deleteFromServerMoves;


    Restaurant restaurant;
    Event event;
    MoveText prefCuisine;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_move_details);

        setupRecyclerView();

        getMoveDetails();
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.left_in, R.anim.right_out);
    }


    private void setupRecyclerView() {
        rvMove = findViewById(R.id.rvMove);

        moves = new ArrayList<>();
        move = Parcels.unwrap(getIntent().getParcelableExtra("move"));
        moves.add(move);

        rvMove.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        adapter = new MoveDetailsAdapter(MoveDetailsActivity.this, moves);
        rvMove.setAdapter(adapter);
    }


    private void getMoveDetails() {
        if (move.moveType.equals("food")) {
            getFoodDetails();
        } else {
            getEventDetails();
        }
    }

    private void getMove() {


//        if (move.moveType.equals("food")) {
//            getFoodView();
//        } else if(move.moveType.equals("event")) {
//            getEventView();
//        } else {
//            prefCuisine = (MoveText) move;
//            Toast.makeText(getApplicationContext(), prefCuisine.Cuisine, Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        isTrip = getIntent().getBooleanExtra("isTrip", false);
//        btnAddToTrip.setVisibility(isTrip ? View.VISIBLE : View.INVISIBLE);
//        if (isTrip) {
//            selectedMoves = TripActivity.selectedMoves;
//            newSelectedMoves = TripActivity.newSelectedMoves;
//            deleteFromServerMoves = TripActivity.deleteFromServerMoves;
//
//            if (selectedMoves.contains(move)) {
//                btnAddToTrip.setText("Remove From Trip");
//            } else {
//                btnAddToTrip.setText("Add To Trip");
//            }
//        }
     }

//    @Override
//    public void onMapReady(GoogleMap map) {
//        mMap = map;
//        mUiSettings = mMap.getUiSettings();
//        mUiSettings.setZoomControlsEnabled(true);
//
//        LatLng moveLatLng = new LatLng(move.lat, move.lng);
//        map.addMarker(new MarkerOptions().position(moveLatLng).title(move.name));
//        map.moveCamera(CameraUpdateFactory.newLatLngZoom(moveLatLng, 15)); // second argument controls how zoomed in map is
//    }
//
//    // map container is a frameLayout in activity_move_details
//    private void addMapFragment() {
//        SupportMapFragment mapFragment = SupportMapFragment.newInstance();
//        mapFragment.getMapAsync(this);
//        getSupportFragmentManager().beginTransaction()
//                .replace(R.id.map_container, mapFragment)
//                .commit();
//    }

//    private void getFoodView() {
//         tvMoveName.setText(move.name);
//         if (move.lat == null) {
//             getFoodDetails();
//             return;
//         }
//
//        // add map fragment only after necessary information obtained from getFoodDetails,
//        // since map fragment requires lat and long
//        addMapFragment();
//
//          String price = "";
//          if (((Restaurant) move).price_level < 0) {
//              price = "Unknown";
//          } else {
//              for (int i = 0; i < ((Restaurant) move).price_level; i++) {
//                  price += '$';
//              }
//          }
//          tvPrice.setText(price);
//
//          //hide groupNum and Time tv & iv
//          ivGroupNum.setVisibility(View.INVISIBLE);
//          tvGroupNum.setVisibility(View.INVISIBLE);
//          ivTime.setVisibility(View.INVISIBLE);
//          tvTime.setVisibility(View.INVISIBLE);
//
//          tvDistance.setText(move.distanceFromLocation(getApplicationContext()) + " mi");
//
//          if (((Restaurant) move).rating < 0) {
//              moveRating.setVisibility(View.INVISIBLE);
//          } else {
//              float moveRate = ((Restaurant) move).rating.floatValue();
//              moveRating.setRating(moveRate = moveRate > 0 ? moveRate / 2.0f : moveRate);
//          }
//    }

    private void getFoodDetails() {
        String apiUrl = "https://maps.googleapis.com/maps/api/place/details/json";
        RequestParams params = new RequestParams();
        params.put("placeid", move.id);
        params.put("key", getString(R.string.api_key));

        HomeActivity.client.get(apiUrl, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                JSONObject result;
                try {
                    result = response.getJSONObject("result");
                    Restaurant moveResult = new Restaurant();
                    moveResult.fromJSON(result, "food");
                    move = moveResult;

                    moves.clear();
                    moves.add(move);

                    if (move.lat != null) {
                        adapter.notifyDataSetChanged();
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

//    private void getEventView() {
//        ivGroupNum.setVisibility(View.INVISIBLE);
//        tvGroupNum.setVisibility(View.INVISIBLE);
//        moveRating.setVisibility(View.INVISIBLE);
//
//        String id = move.id;
//
//        if (move.lat == null) {
//            getEventDetails(id);
//            return;
//        }
//
//        tvMoveName.setText(move.name);
//        tvDistance.setText(move.distanceFromLocation(getApplicationContext()) + " mi");
//
//        tvTime.setText(((Event) move).startTime);
//        tvPrice.setText(((Event) move).priceRange);
//
//        // add map fragment only after necessary information obtained from getEventDetails(id)
//        addMapFragment();
//    }


    private void getEventDetails() {
        String API_BASE_URL_TMASTER = "https://app.ticketmaster.com/discovery/v2/events";
        String apiUrl = API_BASE_URL_TMASTER + "/" + move.id + ".json";

        RequestParams params = new RequestParams();
        params.put("apikey", getString(R.string.api_key_tm));

        HomeActivity.clientTM.get(apiUrl, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                try {
                    Event moveResult = new Event();
                    moveResult.fromDetailsJSON(response);
                    move = moveResult;

                    moves.clear();
                    moves.add(move);

                    if (move.lat != null) {
                        adapter.notifyDataSetChanged();
                    }

                  } catch (JSONException e) {
                    e.printStackTrace();
                }
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


//    private void displayButtonStatus() {
//        if (move != null) {
//            ParseQuery<ParseObject> detailsQuery = getParseQuery(currUser, move);
//            detailsQuery.findInBackground((objects, e) -> {
//                for (int i = 0; i < objects.size(); i++) {
//                    ParseObject pObj = objects.get(i);
//                    Move move = Move.fromParseObject(pObj);
//                    if (move.didFavorite == false) {
//                        ivFavorite.setImageResource(R.drawable.ufi_heart);
//                    } else {
//                        ivFavorite.setImageResource(R.drawable.ufi_heart_active);
//                    }
//
//                    if (move.didSave == false) {
//                        ivSave.setImageResource(R.drawable.ufi_save);
//                    } else {
//                        ivSave.setImageResource(R.drawable.ufi_save_active);
//                    }
//                }
//            });
//        } else {
//
//            Log.i(TAG, "Error finding current move.");
//        }
//    }

//    private void setupButtons() {
//        btnChooseMove.setOnClickListener(view -> {
//            if (move != null) {
//                ParseQuery<ParseObject> detailsQuery = getParseQuery(currUser, move);
//                detailsQuery.findInBackground((objects, e) -> {
//                    if (e == null) {
//                        if (move.moveType.equals("food")) {
//                            currUser.addAllUnique("restaurantsCompleted", Arrays.asList(move.name));
//                        } else {
//                            currUser.addAllUnique("eventsCompleted", Arrays.asList(move.name));
//                        }
//
//                        currUser.saveInBackground();
//                        if (objects.size() == 0) { // occurs if the user has not ever completed this move
//                            ParseObject currObj = new ParseObject("Move");
//                            currObj.put("name", move.name);
//                            currObj.put("placeId", move.id);
//                            currObj.put("moveType", (move.moveType));
//                            currObj.put("user", currUser);
//                            currObj.put("didComplete", true);
//                            currObj.put("count", 0);
//                            currObj.saveInBackground();
//                        } else { // the user has already completed the move
//                            for (int i = 0; i < objects.size(); i++) {
//                                objects.get(i).put("didComplete", true);
//                                objects.get(i).put("didSave", false); // user cannot save a move that has been done
//                                ivSave.setImageResource(R.drawable.ufi_save);
//                                objects.get(i).saveInBackground();
//                            }
//                        }
//                        Log.d("Move", "Move saved in History Successfully");
//                        Toast.makeText(MoveDetailsActivity.this, "Saved to History!", Toast.LENGTH_SHORT).show();
//                    } else {
//                        Log.d("Move", "Error: saving move to history");
//                    }
//                });
//            }
//        });
//
//        btnSave.setOnClickListener(view -> {
//            if (move != null) {
//                ParseQuery<ParseObject> detailsQuery = getParseQuery(currUser, move);
//                detailsQuery.findInBackground((objects, e) -> {
//                    if (objects.size() > 0) {
//                        for (int i = 0; i < objects.size(); i++) {
//                            if (objects.get(i).getBoolean("didComplete") == true) { // user cannot save an already completed move
//                                Toast.makeText(MoveDetailsActivity.this, "You cannot save a move you have already completed!",
//                                        Toast.LENGTH_SHORT).show();
//                                return;
//                            }
//                            if (objects.get(i).getBoolean("didSave") == true) {
//                                objects.get(i).put("didSave", false);
//                                ivSave.setImageResource(R.drawable.ufi_save);
//                                objects.get(i).saveInBackground();
//                            } else {
//                                objects.get(i).put("didSave", true);
//                                ivSave.setImageResource(R.drawable.ufi_save_active);
//                                objects.get(i).saveInBackground();
//                            }
//
//
//                        }
//                    } else { // user is saving a move that has not been completed ever before
//                        ivSave.setImageResource(R.drawable.ufi_save_active);
//                        ParseObject currObj = new ParseObject("Move");
//                        currObj.put("name", move.name);
//                        currObj.put("user", currUser);
//                        currObj.put("didSave", true);
//                        currObj.put("didFavorite", false);
//                        currObj.put("moveType", (move.moveType));
//                        currObj.put("placeId", move.id);
//                        currObj.put("didComplete", false);
//                        currObj.saveInBackground();
//                    }
//                });
//            }
//        });
//
//        btnFavorite.setOnClickListener(view -> {
//            if (move != null) {
//                ParseQuery<ParseObject> detailsQuery = getParseQuery(currUser, move);
//                detailsQuery.findInBackground((objects, e) -> {
//                    if (objects.size() > 0) {
//                        for (int i = 0; i < objects.size(); i++) {
//                            if (objects.get(i).getBoolean("didComplete") == false) { // user cannot like move if it has not been completed
//                                Toast.makeText(MoveDetailsActivity.this, "You must complete the move before liking it!",
//                                        Toast.LENGTH_SHORT).show();
//                                return;
//                            }
//                            if (objects.get(i).getBoolean("didFavorite") == true) {
//                                ivFavorite.setImageResource(R.drawable.ufi_heart);
//                                objects.get(i).put("didFavorite", false);
//                                objects.get(i).saveInBackground();
//                            } else {
//                                objects.get(i).put("didFavorite", true);
//                                ivFavorite.setImageResource(R.drawable.ufi_heart_active);
//                                objects.get(i).saveInBackground();
//                            }
//                        }
//                    } else { // we enter this part of code when the user has not completed any moves
//                        Toast.makeText(MoveDetailsActivity.this, "You must complete the move before liking it!",
//                                Toast.LENGTH_SHORT).show();
//                    }
//                });
//            }
//        });
//        btnAddToTrip.setOnClickListener(view -> saveToTrip());
//    }
//
//    private void saveToTrip() {
//        if (TripActivity.isEditingTrip) {
//            if ((selectedMoves.contains(move) && newSelectedMoves.contains(move))) {
//                selectedMoves.remove(move);
//                newSelectedMoves.remove(move);
//                btnAddToTrip.setText("Add To Trip");
//            } else if (selectedMoves.contains(move) && !newSelectedMoves.contains(move)) {
//                selectedMoves.remove(move);
//                deleteFromServerMoves.add(move);
//                btnAddToTrip.setText("Add To Trip");
//            } else if (!selectedMoves.contains(move) && newSelectedMoves.contains(move)) {
//                newSelectedMoves.remove(move);
//                btnAddToTrip.setText("Add To Trip");
//            } else {
//                selectedMoves.add(move);
//                newSelectedMoves.add(move);
//                btnAddToTrip.setText("Remove From Trip");
//            }
//        } else {
//
//            if (!selectedMoves.contains(move)) {
//                selectedMoves.add(move);
//                btnAddToTrip.setText("Remove From Trip");
//            } else {
//                selectedMoves.remove(move);
//                btnAddToTrip.setText("Add To Trip");
//            }
//        }
//    }
//
//    private void lyftButton() {
//        ApiConfig apiConfig = new ApiConfig.Builder()
//                .setClientId(getString(R.string.client_id_lyft))
//                //waiting for Lyft to approve developer signup request
//                .setClientToken("...")
//                .build();
//
//        LyftButton lyftButton = findViewById(R.id.lyft_button);
//        lyftButton.setApiConfig(apiConfig);
//        UserLocation currLocation = UserLocation.getCurrentLocation(this);
//
//        RideParams.Builder rideParamsBuilder = new RideParams.Builder()
//                .setPickupLocation(Double.valueOf(currLocation.lat), Double.valueOf(currLocation.lng))
//                //TODO: add correct dropoff location once Lyft approves developer request
//                .setDropoffLocation(37.759234, -122.4135125);
//        rideParamsBuilder.setRideTypeEnum(RideTypeEnum.STANDARD);
//
//        lyftButton.setRideParams(rideParamsBuilder.build());
//        lyftButton.load();
//    }
}