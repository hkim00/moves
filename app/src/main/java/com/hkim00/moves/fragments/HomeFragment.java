package com.hkim00.moves.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.hkim00.moves.GoogleClient;
import com.hkim00.moves.HomeActivity;
import com.hkim00.moves.LocationActivity;
import com.hkim00.moves.MovesActivity;
import com.hkim00.moves.R;
import com.hkim00.moves.util.MoveCategories;
import com.hkim00.moves.models.Event;
import com.hkim00.moves.models.Restaurant;
import com.hkim00.moves.models.UserLocation;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.parse.ParseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cz.msebera.android.httpclient.Header;

import static android.app.Activity.RESULT_OK;

public class HomeFragment extends Fragment {

    public final static String TAG = "HomeFragment";
    public static final String API_BASE_URL = "https://maps.googleapis.com/maps/api";
    public static final String API_BASE_URL_TM = "https://app.ticketmaster.com/discovery/v2/events";
    public static final int LOCATION_REQUEST_CODE = 20;

    private String moveType = "";

    private GoogleClient googleClient;

    private String time;
    private String numberOfPeople;
    private int distance;
    private int priceLevel;
    private List<Restaurant> restaurantResults;
    private List<Event> eventResults;
    private UserLocation location;

    private TextView tvLocation, tvDistance, tvPriceLevel;
    private ImageView ivDistance, ivPrice;
    private Button btnTime, btnPeople, btnDistance, btnPrice, btnLocation;

    private Button btnFood, btnEvents;

    private ConstraintLayout clCategories;
    private ImageView ivFood, ivActivities, ivAttractions, ivEvents;

    private ConstraintLayout clPrice;
    private TextView tvRightPopupTitle, tvMiles;
    private EditText etDistance;
    private Button btnPriceLevel1, btnPriceLevel2, btnPriceLevel3, btnPriceLevel4;

    private Button btnMove, btnRiskyMove;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        location = new UserLocation();
        googleClient = new GoogleClient();

        // initialize arrays to add JSON objects (Restaurant or Event objects) to
        restaurantResults = new ArrayList<>();
        eventResults = new ArrayList<>();

        getViewIds(view);

        setupDesign();

        setupButtons();

        checkForCurrentLocation();
    }

    private void checkForCurrentLocation() {

        location = UserLocation.getCurrentLocation(getContext());

        if (location.lat.equals("0.0") && location.name.equals("")) {
            tvLocation.setText("Choose location");
        } else {
            tvLocation.setText(location.name);
        }
    }

    private void getViewIds(View view) {
        btnLocation = view.findViewById(R.id.btnLocation);

        tvLocation = view.findViewById(R.id.tvLocation);
//        btnTime = view.findViewById(R.id.btnTime);
//        btnPeople = view.findViewById(R.id.btnPeople);
        ivDistance = view.findViewById(R.id.ivDistance);
        tvDistance = view.findViewById(R.id.tvDistance);
        btnDistance = view.findViewById(R.id.btnDistance);
        ivPrice = view.findViewById(R.id.ivPrice);
        btnPrice = view.findViewById(R.id.btnPrice);
        tvPriceLevel = view.findViewById(R.id.tvPriceLevel);

        btnFood = view.findViewById(R.id.btnFood);
        btnEvents = view.findViewById(R.id.btnEvents);

        clCategories = view.findViewById(R.id.clCategories);
        ivFood = view.findViewById(R.id.ivFood);
//        ivActivities = view.findViewById(R.id.ivActivities);
//        ivAttractions = view.findViewById(R.id.ivAttractions);
        ivEvents = view.findViewById(R.id.ivEvents);

        clPrice = view.findViewById(R.id.clPrice);
        tvRightPopupTitle = view.findViewById(R.id.tvRightPopupTitle);
        tvMiles = view.findViewById(R.id.tvMiles);
        etDistance = view.findViewById(R.id.etDistance);
        btnPriceLevel1 = view.findViewById(R.id.btnPriceLevel1);
        btnPriceLevel2 = view.findViewById(R.id.btnPriceLevel2);
        btnPriceLevel3 = view.findViewById(R.id.btnPriceLevel3);
        btnPriceLevel4 = view.findViewById(R.id.btnPriceLevel4);

        btnMove = view.findViewById(R.id.btnMove);
        btnRiskyMove = view.findViewById(R.id.btnRiskyMove);
    }

    private void setupDesign() {
//        btnTime.getLayoutParams().width= HomeActivity.screenWidth/4;
//        btnPeople.getLayoutParams().width= HomeActivity.screenWidth/4;
        btnDistance.getLayoutParams().width= HomeActivity.screenWidth/2;
        btnPrice.getLayoutParams().width= HomeActivity.screenWidth/2;

        clPrice.setVisibility(View.INVISIBLE);

        etDistance.addTextChangedListener(textWatcher);
        tvDistance.setVisibility(View.INVISIBLE);
        distance = milesToMeters(1);

        tvPriceLevel.setVisibility(View.INVISIBLE);
        priceLevel = 0;

        moveType = "";

        clCategories.post(new Runnable() {
            @Override
            public void run() {
                int constraintHeight = clCategories.getLayoutParams().height;
                ivFood.getLayoutParams().height = constraintHeight/4;
//                ivActivities.getLayoutParams().height = constraintHeight/4;
//                ivAttractions.getLayoutParams().height = constraintHeight/4;
                ivEvents.getLayoutParams().height = constraintHeight/4;
            }
        });
    }

    private void setupButtons() {
        btnPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleRightPopup("price");
            }
        });

        btnDistance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleRightPopup("distance");
            }
        });

        btnPriceLevel1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                priceLevelSelected(1);
            }
        });

        btnPriceLevel2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                priceLevelSelected(2);
            }
        });

        btnPriceLevel3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                priceLevelSelected(3);
            }
        });

        btnPriceLevel4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                priceLevelSelected(4);
            }
        });

        // TODO: find more user-friendly way to show state (i.e. whether user has selected food or event before clicking move)
        btnFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View view){
                moveType = "food";
                Toast.makeText(getContext(), "movetype is: " + moveType, Toast.LENGTH_SHORT).show();
            }
        });

        btnEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moveType = "event";
                Toast.makeText(getContext(), "movetype is: " + moveType, Toast.LENGTH_SHORT).show();
            }
        });

        btnLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), LocationActivity.class);
                startActivityForResult(intent, LOCATION_REQUEST_CODE );
            }
        });


        btnMove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                typeMoveSelected();
            }
        });

        btnRiskyMove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getRiskyMove();
            }
        });
    }

    private void toggleRightPopup(String type) {
        if (!(!tvRightPopupTitle.getText().toString().toLowerCase().equals(type) && clPrice.getVisibility() == View.VISIBLE)) {
            clPrice.setVisibility((clPrice.getVisibility() == View.INVISIBLE) ? View.VISIBLE : View.INVISIBLE);
        }

        if (type.equals("price")) {
            tvRightPopupTitle.setText("Price");

            btnPriceLevel1.setVisibility(View.VISIBLE);
            btnPriceLevel2.setVisibility(View.VISIBLE);
            btnPriceLevel3.setVisibility(View.VISIBLE);
            btnPriceLevel4.setVisibility(View.VISIBLE);
            tvMiles.setVisibility(View.INVISIBLE);
            etDistance.setVisibility(View.INVISIBLE);

        } else if (type.equals("distance")) {
            tvRightPopupTitle.setText("Distance");

            btnPriceLevel1.setVisibility(View.INVISIBLE);
            btnPriceLevel2.setVisibility(View.INVISIBLE);
            btnPriceLevel3.setVisibility(View.INVISIBLE);
            btnPriceLevel4.setVisibility(View.INVISIBLE);
            tvMiles.setVisibility(View.VISIBLE);
            etDistance.setVisibility(View.VISIBLE);
        }
    }

    private void typeMoveSelected() {
        if (location.lat.equals("0.0") && location.lng.equals("0.0")) {
            Toast.makeText(getContext(), "Set a location", Toast.LENGTH_LONG).show();
            return;
        }

        if (moveType.equals("")){
            Toast.makeText(getContext(), "Please select food or event!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (moveType.equals("food")) {
            getNearbyRestaurants(new ArrayList<>());
        }
        else if (moveType.equals("event")) {
            checkForPostalCode();
        }
    }

    private void priceLevelSelected(int priceLevel) {
        int selectedColor = getResources().getColor(R.color.selected_blue);

        btnPriceLevel1.setBackgroundColor(getResources().getColor(R.color.white));
        btnPriceLevel1.setTextColor(getResources().getColor(R.color.black));
        btnPriceLevel2.setBackgroundColor(getResources().getColor(R.color.white));
        btnPriceLevel2.setTextColor(getResources().getColor(R.color.black));
        btnPriceLevel3.setBackgroundColor(getResources().getColor(R.color.white));
        btnPriceLevel3.setTextColor(getResources().getColor(R.color.black));
        btnPriceLevel4.setBackgroundColor(getResources().getColor(R.color.white));
        btnPriceLevel4.setTextColor(getResources().getColor(R.color.black));

        if (this.priceLevel == priceLevel) {
            this.priceLevel = 0;

            ivPrice.setVisibility(View.VISIBLE);
            tvPriceLevel.setVisibility(View.INVISIBLE);
        } else {
            this.priceLevel = priceLevel;

            tvPriceLevel.setVisibility(View.VISIBLE);
            ivPrice.setVisibility(View.INVISIBLE);

            if (priceLevel == 1) {
                btnPriceLevel1.setBackgroundColor(selectedColor);
                btnPriceLevel1.setTextColor(getResources().getColor(R.color.white));

                tvPriceLevel.setText("$");

            } else if (priceLevel == 2) {
                btnPriceLevel2.setBackgroundColor(selectedColor);
                btnPriceLevel2.setTextColor(getResources().getColor(R.color.white));

                tvPriceLevel.setText("$$");

            } else if (priceLevel == 3) {
                btnPriceLevel3.setBackgroundColor(selectedColor);
                btnPriceLevel3.setTextColor(getResources().getColor(R.color.white));

                tvPriceLevel.setText("$$$");

            } else if (priceLevel == 4) {
                btnPriceLevel4.setBackgroundColor(selectedColor);
                btnPriceLevel4.setTextColor(getResources().getColor(R.color.white));

                tvPriceLevel.setText("$$$$");
            }
        }
    }

    private final TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String distanceString = etDistance.getText().toString().trim();

            if (distanceString.equals("")) {
                ivDistance.setVisibility(View.VISIBLE);
                tvDistance.setVisibility(View.INVISIBLE);
            } else {
                ivDistance.setVisibility(View.INVISIBLE);
                tvDistance.setVisibility(View.VISIBLE);

                tvDistance.setText(distanceString + "mi");
            }
        }

        @Override
        public void afterTextChanged(Editable s) {}
    };

    private void checkForPostalCode() {
        if (location.postalCode.equals("")) {

            if (location.lat.equals("0.0") && location.lng.equals("0.0")) {
                Toast.makeText(getContext(), "Set a location", Toast.LENGTH_LONG).show();
                return;

            } else {
                String apiUrl = API_BASE_URL + "/geocode/json";

                RequestParams params = new RequestParams();
                params.put("latlng",location.lat + "," + location.lng);
                params.put("key", getString(R.string.api_key));

                HomeActivity.client.get(apiUrl, params, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);

                        UserLocation newLocation = UserLocation.addingPostalCodeFromJSON(getContext(), location, response);
                        location.postalCode = newLocation.postalCode;

                        if (!newLocation.equals("")) {
                            getNearbyEvents(new ArrayList<String>());
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
        } else {
            getNearbyEvents(new ArrayList<String>());
        }
    }

    private void getNearbyEvents(List<String> nonPreferredList) {
        String apiUrl = API_BASE_URL_TM + ".json";

        RequestParams params = new RequestParams();

        if (nonPreferredList.size() == 0) {
            JSONArray jsonPrefList = ParseUser.getCurrentUser().getJSONArray("eventPrefList");
            if (jsonPrefList != null) {
                try {
                    for (int i = 0; i < jsonPrefList.length(); i++) {
                        String pref = jsonPrefList.get(i).toString();
                        params.put("keyword", pref);
                    }
                } catch (JSONException e) {
                    Log.e(TAG, "error getting events");
                    e.printStackTrace();
                }
            }
        } else {
            //this is for risky move
            for (int i = 0; i < nonPreferredList.size(); i++) {
                params.put("keyword", nonPreferredList.get(i));
            }
        }

        params.put("postalCode", location.postalCode);
        params.put("sort", "date,asc");
        params.put("apikey", getString(R.string.api_key_tm));

        HomeActivity.clientTM.get(apiUrl, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                eventResults.clear();

                JSONArray events;
                if (response.has("_embedded")) {
                    try {
                        events = (response.getJSONObject("_embedded")).getJSONArray("events");
                        for (int i = 0; i < events.length(); i++) {
                            Event event = Event.fromJSON(events.getJSONObject(i));
                            Log.d(TAG, "got event");
                            eventResults.add(event);
                        }

                        Intent intent = new Intent(getContext(), MovesActivity.class);
                        intent.putExtra("moves", Parcels.wrap(eventResults));
                        startActivity(intent);

                    } catch (JSONException e) {
                        Log.e(TAG, "Error getting events");
                        e.printStackTrace();
                    }
                } else {
                    Intent intent = new Intent(getContext(), MovesActivity.class);
                    intent.putExtra("moves", Parcels.wrap(eventResults));
                    startActivity(intent);
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

    private void getNearbyRestaurants(List<String> nonPreferredList) {
        String apiUrl = API_BASE_URL + "/place/nearbysearch/json";

        String distanceString = etDistance.getText().toString().trim();
        distance = (distanceString.equals("")) ? milesToMeters(1) : milesToMeters(Float.valueOf(distanceString));


        RequestParams params = new RequestParams();
        params.put("location",location.lat + "," + location.lng);
        params.put("radius", (distance > 50000) ? 50000 : distance);
        params.put("type","restaurant");

        String userFoodPref = getUserFoodPreferenceString(nonPreferredList);

        if (!userFoodPref.equals("")) {
            params.put("keyword", userFoodPref);
        }

        if (priceLevel > 0) {
            params.put("maxprice", priceLevel);
        }

        params.put("key", getString(R.string.api_key));

        HomeActivity.client.get(apiUrl, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                restaurantResults.clear();

                JSONArray results;
                try {
                    results = response.getJSONArray("results");

                    for (int i = 0; i < results.length(); i++) {
                        Restaurant restaurant = Restaurant.fromJSON(results.getJSONObject(i));
                        Log.d(TAG, "got restaurant");

                        restaurantResults.add(restaurant);
                    }

                    Intent intent = new Intent(getContext(), MovesActivity.class);
                    intent.putExtra("moves", Parcels.wrap(restaurantResults));
                    startActivity(intent);

                } catch (JSONException e) {
                    Log.e(TAG, "Error getting nearby");

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

    private String getUserFoodPreferenceString(List<String> nonPreferredList) {
        if (ParseUser.getCurrentUser().getJSONArray("foodPrefList") == null || ParseUser.getCurrentUser().getJSONArray("foodPrefList").length() == 0) {
            return "";
        }

        List<String> preferredList = new ArrayList<>();

        if (nonPreferredList.size() == 0) {
             preferredList = MoveCategories.JSONArrayToList(ParseUser.getCurrentUser().getJSONArray("foodPrefList"));
        } else {
            preferredList = nonPreferredList;
        }

        String userFoodPref = "";
        for (int i = 0; i < preferredList.size(); i++) {
            userFoodPref += preferredList.get(i);
            userFoodPref += "+";
        }

        userFoodPref = userFoodPref.substring(0, userFoodPref.length() -1);

        return userFoodPref;
    }


    private void getRiskyMove() {
        if (moveType.equals("")) {
            return;
        }
        MoveCategories helper = new MoveCategories();
        List<String> nonPreferredList = new ArrayList<>();

        if (moveType.equals("food")) {
            if (ParseUser.getCurrentUser().getJSONArray("foodPrefList") != null || ParseUser.getCurrentUser().getJSONArray("foodPrefList").length() != 0) {

                List<String> preferredList = helper.JSONArrayToList(ParseUser.getCurrentUser().getJSONArray("foodPrefList"));
                nonPreferredList = helper.getPreferenceDiff(moveType, preferredList);
            }
        } else {
            if (ParseUser.getCurrentUser().getJSONArray("eventPrefList") != null || ParseUser.getCurrentUser().getJSONArray("eventPrefList").length() != 0) {

                List<String> preferredList = helper.JSONArrayToList(ParseUser.getCurrentUser().getJSONArray("eventPrefList"));
                nonPreferredList = helper.getPreferenceDiff(moveType, preferredList);
            }
        }

        if ((moveType.equals("food"))) {
            getNearbyRestaurants(nonPreferredList);
        } else {
            getNearbyEvents(nonPreferredList);
        }

    }


    private int milesToMeters(float miles) {
        return (int) (miles/0.000621317);
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == LOCATION_REQUEST_CODE ) {
            checkForCurrentLocation();
        }
    }

    public void UpdateMoveList() {

        Map<String, Integer> PrefDict = new HashMap<String, Integer>();
        ArrayList<Map<String, Integer>> al = new ArrayList();

        PrefDict.put ("key1", 1);
        PrefDict.put ("key2", 2);
        PrefDict.put ("key3", 3);
        PrefDict.put ("key4", 4);

        al.add(PrefDict);

        ParseUser currUser = ParseUser.getCurrentUser();
        currUser.put("tester", al);
        currUser.saveInBackground();
    }


}
