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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hkim00.moves.HomeActivity;
import com.hkim00.moves.LocationActivity;
import com.hkim00.moves.MovesActivity;
import com.hkim00.moves.R;
import com.hkim00.moves.TripActivity;
import com.hkim00.moves.adapters.MoveCategoryAdapter;
import com.hkim00.moves.models.Cuisine;
import com.hkim00.moves.models.Event;
import com.hkim00.moves.models.Move;
import com.hkim00.moves.models.MoveCategory;
import com.hkim00.moves.models.Restaurant;
import com.hkim00.moves.models.UserLocation;
import com.hkim00.moves.util.MoveCategoriesHelper;
import com.hkim00.moves.util.StatusCodeHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.parse.ParseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import cz.msebera.android.httpclient.Header;

import static android.app.Activity.RESULT_OK;

public class HomeFragment extends Fragment {

    public final static String TAG = "HomeFragment";
    public static final String API_BASE_URL = "https://maps.googleapis.com/maps/api";
    public static final String API_BASE_URL_TM = "https://app.ticketmaster.com/discovery/v2/events";
    public static final int LOCATION_REQUEST_CODE = 20;

    ParseUser currUser = ParseUser.getCurrentUser();
    ParseUser friend;

    private String moveType = "";

    private int distance;
    private int priceLevel;
    private UserLocation location;

    private TextView tvDistance, tvPriceLevel;
    private ImageView ivDistance, ivPrice;
    private Button btnDistance, btnPrice;

    private Button btnFood, btnEvents;
    private ImageView ivAddFriends;
    private View vFoodIndicator, vEventIndicator;


    private ConstraintLayout clPrice;
    private TextView tvRightPopupTitle, tvMiles;
    private EditText etDistance;
    private Button btnPriceLevel1, btnPriceLevel2, btnPriceLevel3, btnPriceLevel4;

    private TextView tvFriend;
    private Boolean isFriendMove = false;

    private Button btnMove, btnRiskyMove, btnAddFriends, btnAddFriend;

    private RecyclerView rvMoveResults;
    private MoveCategoryAdapter adapter;
    private List<MoveCategory> moveResults, foodResults, eventResults;
    private ProgressBar progressBar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        location = HomeActivity.location;

        getViewIds(view);

        setupDesign();

        setupButtons();

        setupRecyclerView();

        toggleMoveType(true);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            friend = bundle.getParcelable("friend");
            ivAddFriends.setVisibility(View.INVISIBLE);
            tvFriend.setText(friend.getUsername());
            isFriendMove = true;
        }
    }

    private void setupRecyclerView() {
        moveResults = new ArrayList<>();
        foodResults = new ArrayList<>();
        eventResults = new ArrayList<>();

        rvMoveResults.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new MoveCategoryAdapter(HomeFragment.this.getContext(), moveResults);
        rvMoveResults.setAdapter(adapter);
    }

    private void checkForPostalCode() {
        if (location.postalCode.equals("")) {

            if (location.lat.equals(null) && location.lng.equals(null)) {
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

                        UserLocation newLocation = UserLocation.addingPostalCodeFromJSON(getContext(), false, location, response);
                        location.postalCode = newLocation.postalCode;

                        if (newLocation.equals("")) {
                            Log.e(TAG, "No postal code found.");
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                        new StatusCodeHandler(TAG, statusCode);
                        throwable.printStackTrace();
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                        new StatusCodeHandler(TAG, statusCode);
                        throwable.printStackTrace();
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        super.onFailure(statusCode, headers, responseString, throwable);
                        new StatusCodeHandler(TAG, statusCode);
                        throwable.printStackTrace();
                    }
                });
            }
        } else {
            return;
        }
    }

    private void getViewIds(View view) {
        ivDistance = view.findViewById(R.id.ivDistance);
        tvDistance = view.findViewById(R.id.tvDistance);
        btnDistance = view.findViewById(R.id.btnDistance);
        ivPrice = view.findViewById(R.id.ivPrice);
        btnPrice = view.findViewById(R.id.btnPrice);
        tvPriceLevel = view.findViewById(R.id.tvPriceLevel);

        btnFood = view.findViewById(R.id.btnFood);
        btnEvents = view.findViewById(R.id.btnEvent);
        vFoodIndicator = view.findViewById(R.id.vFoodIndicator);
        vEventIndicator = view.findViewById(R.id.vEventIndicator);

        clPrice = view.findViewById(R.id.clPrice);
        tvRightPopupTitle = view.findViewById(R.id.tvRightPopupTitle);
        tvMiles = view.findViewById(R.id.tvMiles);
        etDistance = view.findViewById(R.id.etDistance);
        btnPriceLevel1 = view.findViewById(R.id.btnPriceLevel1);
        btnPriceLevel2 = view.findViewById(R.id.btnPriceLevel2);
        btnPriceLevel3 = view.findViewById(R.id.btnPriceLevel3);
        btnPriceLevel4 = view.findViewById(R.id.btnPriceLevel4);

        tvFriend = view.findViewById(R.id.tvFriend);
        ivAddFriends = view.findViewById(R.id.ivAddFriends);

        btnMove = view.findViewById(R.id.btnMove);
        btnRiskyMove = view.findViewById(R.id.btnRiskyMove);
        btnAddFriends = view.findViewById(R.id.btnAddFriends);
        btnAddFriend = view.findViewById(R.id.btnAddFriends);

        rvMoveResults = view.findViewById(R.id.rvMoveResults);
        progressBar = view.findViewById(R.id.progressBar);
    }

    private void setupDesign() {
        progressBar.setVisibility(View.INVISIBLE);

        btnDistance.getLayoutParams().width= HomeActivity.screenWidth/3;
        btnPrice.getLayoutParams().width= HomeActivity.screenWidth/3;
        btnAddFriend.getLayoutParams().width=HomeActivity.screenWidth/3;

        clPrice.setVisibility(View.INVISIBLE);

        etDistance.addTextChangedListener(textWatcher);
        tvDistance.setVisibility(View.INVISIBLE);
        distance = MoveCategoriesHelper.milesToMeters(1);

        tvPriceLevel.setVisibility(View.INVISIBLE);
        priceLevel = 0;

        tvFriend.setText("");

        moveType = "";
    }

    private void setupButtons() {
        btnPrice.setOnClickListener(view -> toggleRightPopup("price"));

        btnDistance.setOnClickListener(view -> toggleRightPopup("distance"));

        btnPriceLevel1.setOnClickListener(view -> priceLevelSelected(1));

        btnPriceLevel2.setOnClickListener(view -> priceLevelSelected(2));

        btnPriceLevel3.setOnClickListener(view -> priceLevelSelected(3));

        btnPriceLevel4.setOnClickListener(view -> priceLevelSelected(4));

        // TODO: find more user-friendly way to show state (i.e. whether user has selected food or event before clicking move)
        btnFood.setOnClickListener(view -> toggleMoveType(true));

        btnEvents.setOnClickListener(view -> toggleMoveType(false));

        btnMove.setOnClickListener(view -> typeMoveSelected());

        btnRiskyMove.setOnClickListener(view -> {
            if (moveType == "food") {
                getNearbyRestaurants(new ArrayList<>(), true, isFriendMove);
            }
            if (moveType == "event") {
                getNearbyEvents(new ArrayList<>(), true, isFriendMove);
            }
        });

        btnAddFriends.setOnClickListener(view -> {
            Fragment fragment = new SearchFragment();
            ((SearchFragment) fragment).isAddFriend = true;
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.flContainer, fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        });

    }

    private void toggleMoveType(boolean isFoodType) {
        moveType = (isFoodType) ? "food" : "event";

        vFoodIndicator.setVisibility(isFoodType ? View.VISIBLE : View.INVISIBLE);
        vEventIndicator.setVisibility(isFoodType ? View.INVISIBLE : View.VISIBLE);

        if (isFoodType) {
            if (foodResults.size() == 0) {
                getNearbyRestaurants(new ArrayList<>(), true, isFriendMove);
            } else {
                updateRecycler(foodResults);
            }
        } else {
            if (eventResults.size() == 0) {
                getNearbyEvents(new ArrayList<>(), true, isFriendMove);
            } else {
                updateRecycler(eventResults);
            }
        }
    }



    private void toggleRightPopup(String type) {
        if (!(!tvRightPopupTitle.getText().toString().toLowerCase().equals(type) && clPrice.getVisibility() == View.VISIBLE)) {
            clPrice.setVisibility((clPrice.getVisibility() == View.INVISIBLE) ? View.VISIBLE : View.INVISIBLE);
        }

        tvRightPopupTitle.setText((type.equals("price")) ? "Price" : "Distance");

        btnPriceLevel1.setVisibility((type.equals("price")) ? View.VISIBLE : View.INVISIBLE);
        btnPriceLevel2.setVisibility((type.equals("price")) ? View.VISIBLE : View.INVISIBLE);
        btnPriceLevel3.setVisibility((type.equals("price")) ? View.VISIBLE : View.INVISIBLE);
        btnPriceLevel4.setVisibility((type.equals("price")) ? View.VISIBLE : View.INVISIBLE);
        tvMiles.setVisibility((type.equals("price")) ? View.INVISIBLE: View.VISIBLE);
        etDistance.setVisibility((type.equals("price")) ? View.INVISIBLE: View.VISIBLE);

    }

    private final TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String distanceString = etDistance.getText().toString().trim();
            ivDistance.setVisibility(distanceString.equals("") ? View.VISIBLE : View.INVISIBLE);
            tvDistance.setVisibility((distanceString.equals("")) ? View.INVISIBLE : View.VISIBLE);
            tvDistance.setText((distanceString.equals("")) ? "" : distanceString + "mi");
        }

        @Override
        public void afterTextChanged(Editable s) {}
    };

    private void typeMoveSelected() {
        if (location.lat == null && location.lng == null) {
            Toast.makeText(getContext(), "Set a location", Toast.LENGTH_LONG).show();
            return;
        }

        if (moveType.equals("")){
            Toast.makeText(getContext(), "Please select food or event!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (moveType.equals("food")) {
            getNearbyRestaurants(new ArrayList<>(), false, isFriendMove);
        }
        else if (moveType.equals("event")) {
            getNearbyEvents(new ArrayList<>(), false, isFriendMove);
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

    // helper method for getting prefs from user and adding them as params and to the list of total pref
    private void addToPref(List<String> totalPref, JSONArray prefList, RequestParams params) {
        try {
            for (int i = 0; i < prefList.length(); i++) {
                String pref = prefList.get(i).toString();
                params.put("keyword", pref);
                totalPref.add(pref);
            }
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
            e.printStackTrace();
        }
    }

    private void getNearbyEvents(List<String> totalPref, Boolean isRisky, Boolean isFriendMove) {
        progressBar.setVisibility(View.VISIBLE);
        checkForPostalCode();

        String apiUrl = API_BASE_URL_TM + ".json";

        RequestParams params = new RequestParams();

        params.put("apikey", getString(R.string.api_key_tm));
        params.put("postalCode", location.postalCode);
        params.put("sort", "date,asc");

        if (!isRisky) {
            if (!isFriendMove) {
                if (totalPref.size() == 0) {
                    JSONArray currUserPrefList = currUser.getJSONArray("eventPrefList");
                    if (currUserPrefList != null) {
                        addToPref(totalPref, currUserPrefList, params);
                    }
                } else {
                    for (int i = 0; i < totalPref.size(); i++) {
                        params.put("keyword", totalPref.get(i));
                    }
                }
            } else {
                if (totalPref.size() == 0) {
                    JSONArray currUserPrefList = currUser.getJSONArray("eventPrefList");
                    JSONArray friendPrefList = friend.getJSONArray("eventPrefList");
                    if (currUserPrefList != null || friendPrefList != null) {
                        addToPref(totalPref, currUserPrefList, params);
                        addToPref(totalPref, friendPrefList, params);
                    }
                } else {
                    for (int i = 0; i < totalPref.size(); i++) {
                        params.put("keyword", totalPref.get(i));
                    }
                }
            }
        }

        Set<String> uniqueTotalPref = new HashSet<>(totalPref); //convert totalpref list to set to remove duplicates
        Log.i("HomeFragment", uniqueTotalPref.toString());

        HomeActivity.clientTM.get(apiUrl, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                progressBar.setVisibility(View.INVISIBLE);

                List<Move> moves = new ArrayList<>();

                if (response.has("_embedded")) {
                    try {
                        JSONArray jsonArray = (response.getJSONObject("_embedded")).getJSONArray("events");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            try {
                                Event event = new Event();
                                event.fromJSON(jsonArray.getJSONObject(i), moveType);
                                moves.add(event);
                            } catch (JSONException e) {
                                progressBar.setVisibility(View.INVISIBLE);
                                e.printStackTrace();
                            }
                        }

                        if (moves.size() > 0) {
                            MoveCategory moveCategory = new MoveCategory("", moves);
                            eventResults.add(moveCategory);
                            progressBar.setVisibility(View.INVISIBLE);

                            updateRecycler(eventResults);
                        }

                    } catch (JSONException e) {
                        progressBar.setVisibility(View.INVISIBLE);
                        Log.e(TAG, "Error getting events");
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                progressBar.setVisibility(View.INVISIBLE);
                new StatusCodeHandler(TAG, statusCode);
                Log.i(TAG, errorResponse.toString());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                progressBar.setVisibility(View.INVISIBLE);
                new StatusCodeHandler(TAG, statusCode);
                Log.i(TAG, errorResponse.toString());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                progressBar.setVisibility(View.INVISIBLE);
                new StatusCodeHandler(TAG, statusCode);
                Log.i(TAG, responseString);
            }
        });
    }

    private void getNearbyRestaurants(List<String> totalPref, Boolean isRisky, Boolean isFriendMove) {
        progressBar.setVisibility(View.VISIBLE);
        String apiUrl = API_BASE_URL + "/place/nearbysearch/json";

        String distanceString = etDistance.getText().toString().trim();
        distance = (distanceString.equals("")) ? MoveCategoriesHelper.milesToMeters(1) : MoveCategoriesHelper.milesToMeters(Float.valueOf(distanceString));

        RequestParams params = new RequestParams();
        params.put("key", getString(R.string.api_key));
        params.put("location", location.lat + "," + location.lng);
        params.put("radius", (distance > 50000) ? 50000 : distance);
        params.put("type", "restaurant");

        if (priceLevel > 0) {
            params.put("maxprice", priceLevel);
        }

        params.put("key", getString(R.string.api_key));

        if (!isRisky) {
            if (!isFriendMove) {
                if (totalPref.size() == 0) {
                    JSONArray currUserPrefList = currUser.getJSONArray("foodPrefList");
                    if (currUserPrefList != null) {
                        addToPref(totalPref, currUserPrefList, params);
                    }
                } else {
                    for (int i = 0; i < totalPref.size(); i++) {
                        String pref = totalPref.get(i);
                    }
                }
            } else {
                if (totalPref.size() == 0) {
                    JSONArray currUserPrefList = currUser.getJSONArray("foodPrefList");
                    JSONArray friendPrefList = friend.getJSONArray("foodPrefList");
                    if (currUserPrefList != null || friendPrefList != null) {
                        for (int i = 0; i < currUserPrefList.length(); i++) {
                            addToPref(totalPref, currUserPrefList, params);
                        }
                        for (int i = 0; i < friendPrefList.length(); i++) {
                            addToPref(totalPref, friendPrefList, params);
                        }
                    }
                } else {
                    for (int i = 0; i < totalPref.size(); i++) {
                        String pref = totalPref.get(i);
                    }
                }
            }
        }

        Set<String> uniqueTotalPref = new HashSet<>(totalPref); //convert totalpref list to set to remove duplicates
        Log.i("HomeFragment", uniqueTotalPref.toString());

        if (uniqueTotalPref.size() == 0) {
            progressBar.setVisibility(View.INVISIBLE);
            updateRecycler(new ArrayList<>());
            return;
        }

        for (String pref : uniqueTotalPref) {
            params.put("keyword", pref);
            HomeActivity.client.get(apiUrl, params, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);

                    JSONArray results;

                    List<Move> moves = new ArrayList<>();

                    try {
                        results = response.getJSONArray("results");

                        for (int i = 0; i < results.length(); i++) {
                            Restaurant restaurant = new Restaurant();
                            restaurant.fromJSON(results.getJSONObject(i), "food");
                            restaurant.cuisine = pref;
                            moves.add(restaurant);
                        }


                        if (moves.size() > 0) {
                            MoveCategory moveCategory = new MoveCategory(pref, moves);
                            foodResults.add(moveCategory);
                            progressBar.setVisibility(View.INVISIBLE);

                            updateRecycler(foodResults);
                        }
                    } catch (JSONException e) {
                        progressBar.setVisibility(View.INVISIBLE);
                        Log.e(TAG, e.getMessage());
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    progressBar.setVisibility(View.INVISIBLE);
                    new StatusCodeHandler(TAG, statusCode);
                    throwable.printStackTrace();
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                    progressBar.setVisibility(View.INVISIBLE);
                    new StatusCodeHandler(TAG, statusCode);
                    throwable.printStackTrace();
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    progressBar.setVisibility(View.INVISIBLE);
                    new StatusCodeHandler(TAG, statusCode);
                    throwable.printStackTrace();
                }
            });
        }
    }


    private void updateRecycler(List<MoveCategory> replacementArray) {
        moveResults.clear();
        moveResults.addAll(replacementArray);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == LOCATION_REQUEST_CODE ) {
            location = UserLocation.getCurrentLocation(getContext());
        } else {
            new StatusCodeHandler(TAG, requestCode);
        }
    }

}