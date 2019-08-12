package com.hkim00.moves;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hkim00.moves.adapters.MoveAdapter;
import com.hkim00.moves.adapters.UserAdapter;

import com.hkim00.moves.fragments.HomeFragment;

import com.hkim00.moves.models.Event;
import com.hkim00.moves.models.Move;
import com.hkim00.moves.models.Restaurant;
import com.hkim00.moves.models.Trip;
import com.hkim00.moves.models.UserLocation;
import com.hkim00.moves.util.MoveCategoriesHelper;
import com.hkim00.moves.util.StatusCodeHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.prolificinteractive.materialcalendarview.CalendarDay;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import cz.msebera.android.httpclient.Header;

public class TripActivity extends AppCompatActivity {

    private final static String TAG = "TripActivity";
    public static final String API_BASE_URL = "https://maps.googleapis.com/maps/api";
    private static final int LOCATION_REQUEST_CODE = 20;
    public static final int CALENDAR_REQUEST_CODE = 30;

    private EditText etTrip;
    private TextView tvLocation, tvCalendar, tvFood, tvEvents, tvSelected, tvFriends, tvSelectFriends;
    private Button btnLocation, btnCalendar, btnFood, btnEvents, btnSelected, btnFriends, btnSelectFriends;
    private View vFoodView, vEventsView, vSelectedView, vFriendsView;

    private ProgressBar pb;
    private RecyclerView rvMoves;
    private MoveAdapter movesAdapter;
    private UserAdapter userAdapter;

    private Trip currentTrip;
    private UserLocation location;
    private List<Move> foodMoves, eventMoves, moves;

    public static List<CalendarDay> dates;
    public static List<Move> selectedMoves, newSelectedMoves, deleteFromServerMoves;
    public static List<ParseUser> selectedFriends, newSelectedFriends;

    private List<ParseObject> serverMoves;

    public static boolean isEditingTrip;
    private boolean didCheckSavedSelected, didCheckForFriends;

    Button btnRight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip);

        getViewIds();

        setupActionBar();

        setupRecyclerView();

        setupButtons();

        setupView();
    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();
        overridePendingTransition(R.anim.left_in, R.anim.right_out);
    }


    private void setupActionBar() {
        androidx.appcompat.app.ActionBar actionBar = getSupportActionBar();

        this.getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.action_bar_trip);
        getSupportActionBar().setElevation(2);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.action_bar_grey)));

        Button btnLeft = findViewById(R.id.btnLeft);
        btnLeft.setOnClickListener(view -> onBackPressed());

        btnRight = findViewById(R.id.btnRight);
        btnRight.setOnClickListener(view -> saveTrip());
        btnRight.setTextColor(getResources().getColor(R.color.light_grey));
    }

    private void getViewIds() {
        etTrip = findViewById(R.id.etTripName);
        tvLocation = findViewById(R.id.tvLocation);
        btnLocation = findViewById(R.id.btnLocation);
        tvCalendar = findViewById(R.id.tvCalendar);
        btnCalendar = findViewById(R.id.btnCalendar);
        tvSelectFriends = findViewById(R.id.tvSelectFriends);
        btnSelectFriends = findViewById(R.id.btnSelectFriends);

        tvFood = findViewById(R.id.tvFood);
        tvEvents = findViewById(R.id.tvEvent);
        tvSelected = findViewById(R.id.tvSelected);
        tvFriends = findViewById(R.id.tvFriends);

        btnFood = findViewById(R.id.btnFood);
        btnEvents = findViewById(R.id.btnEvent);
        btnSelected = findViewById(R.id.btnSelected);
        btnFriends = findViewById(R.id.btnFriends);

        vFoodView = findViewById(R.id.vFoodView);
        vEventsView = findViewById(R.id.vEventsView);
        vSelectedView = findViewById(R.id.vSelectedView);
        vFriendsView = findViewById(R.id.vFriendsView);

        pb = findViewById(R.id.pb);
        rvMoves = findViewById(R.id.rvMoves);
    }


    private void setupRecyclerView() {
        rvMoves.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        dates = new ArrayList<>();
        foodMoves = new ArrayList<>();
        eventMoves = new ArrayList<>();
        moves = new ArrayList<>();

        selectedMoves = new ArrayList<>();
        newSelectedMoves = new ArrayList<>();
        deleteFromServerMoves = new ArrayList<>();
        selectedFriends = new ArrayList<>();
        newSelectedFriends = new ArrayList<>();

        serverMoves = new ArrayList<>();

        movesAdapter = new MoveAdapter(TripActivity.this, moves);
        userAdapter = new UserAdapter(TripActivity.this, selectedFriends);

        rvMoves.setAdapter(movesAdapter);
    }


    private void setupButtons() {
        btnLocation.setOnClickListener(view -> goToLocationActivity());
        btnCalendar.setOnClickListener(view -> goToCalendarActivity());
        btnSelectFriends.setOnClickListener(view -> goToSelectUsersActivity());

        btnFood.setOnClickListener(view -> toggleSection("food"));
        btnEvents.setOnClickListener(view -> toggleSection("events"));
        btnSelected.setOnClickListener(view -> toggleSection("selected"));
        btnFriends.setOnClickListener(view -> toggleSection("friends"));
    }

    private void toggleSection(String type) {
        vFoodView.setVisibility((type.equals("food")) ? View.VISIBLE : View.INVISIBLE);
        vEventsView.setVisibility((type.equals("events")) ? View.VISIBLE : View.INVISIBLE);
        vSelectedView.setVisibility((type.equals("selected")) ? View.VISIBLE : View.INVISIBLE);
        vFriendsView.setVisibility((type.equals("friends")) ? View.VISIBLE : View.INVISIBLE);

        if (location.lat == null) {
            return;
        }

        if (type.equals("food")) {
            if (foodMoves.size() == 0) {
                getNearbyRestaurants();
            } else {
                updateMoves(foodMoves);
            }
        } else if (type.equals("events")) {
            if (eventMoves.size() == 0) {
                getNearbyEvents();
            } else {
                updateMoves(eventMoves);
            }
        } else if (type.equals("friends")) {
            if (isEditingTrip && selectedFriends.size() == 0 && !didCheckForFriends) {
                getTripFriends();
            } else {
                rvMoves.setAdapter(userAdapter);
                userAdapter.notifyDataSetChanged();
            }
        } else {
            if (selectedMoves.size() == 0 && !didCheckSavedSelected && isEditingTrip) {
                getSavedTripMoves(currentTrip);
            } else {
                updateMoves(selectedMoves);
            }
        }
    }


    private void setupView() {
        etTrip.addTextChangedListener(charTextWatcher);

        pb.setVisibility(View.INVISIBLE);
        vEventsView.setVisibility(View.INVISIBLE);
        vSelectedView.setVisibility(View.INVISIBLE);
        vFriendsView.setVisibility(View.INVISIBLE);

        didCheckSavedSelected = false;
        didCheckForFriends = false;

        tvSelectFriends.setTextColor(getResources().getColor(R.color.theme));

        if (getIntent().hasExtra("trip")) {
            currentTrip = Parcels.unwrap(getIntent().getParcelableExtra("trip"));

            location = currentTrip.location;
            UserLocation.saveLocation(getApplicationContext(), true, location);

            etTrip.setText(currentTrip.name);

            dates.add(currentTrip.startDay);
            dates.add(currentTrip.endDay);

            tvLocation.setText(currentTrip.location.name);
            tvCalendar.setText(Trip.getDateRangeString(dates.get(0), dates.get(dates.size() - 1)));

            tvLocation.setTextColor(getResources().getColor(R.color.black));
            tvCalendar.setTextColor(getResources().getColor(R.color.black));

            isEditingTrip = true;

            isReadyToSave(false);

            getNearbyRestaurants();
            getSavedTripMoves(currentTrip);
        } else {
            location = UserLocation.clearCurrentTripLocation(this);

            tvLocation.setTextColor(getResources().getColor(R.color.theme));
            tvCalendar.setTextColor(getResources().getColor(R.color.theme));

            isEditingTrip = false;

            btnRight.setTextColor(getResources().getColor(R.color.light_grey));

            toggleMovesView(false);
        }
    }


    private void toggleMovesView(boolean isShowing) {
        tvFood.setVisibility(isShowing ? View.VISIBLE : View.INVISIBLE);
        btnFood.setVisibility(isShowing ? View.VISIBLE : View.INVISIBLE);
        vFoodView.setVisibility(isShowing ? View.VISIBLE : View.INVISIBLE);

        tvEvents.setVisibility(isShowing ? View.VISIBLE : View.INVISIBLE);
        btnEvents.setVisibility(isShowing ? View.VISIBLE : View.INVISIBLE);

        tvSelected.setVisibility(isShowing ? View.VISIBLE : View.INVISIBLE);
        btnSelected.setVisibility(isShowing ? View.VISIBLE : View.INVISIBLE);

        tvFriends.setVisibility(isShowing ? View.VISIBLE : View.INVISIBLE);
        btnFriends.setVisibility(isShowing ? View.VISIBLE : View.INVISIBLE);

        rvMoves.setVisibility(isShowing ? View.VISIBLE : View.INVISIBLE);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == LOCATION_REQUEST_CODE) {
            checkForCurrentLocation();
        } else if (resultCode == RESULT_OK && requestCode == CALENDAR_REQUEST_CODE) {
            if (dates.size() > 0) {
                tvCalendar.setTextColor(getResources().getColor(R.color.black));
                tvCalendar.setText(Trip.getDateRangeString(dates.get(0), dates.get(dates.size() - 1)));
            } else {
                tvCalendar.setText("When?");
                tvCalendar.setTextColor(getResources().getColor(R.color.selected_blue));
            }

            isReadyToSave(false);
        }
    }


    private void checkForCurrentLocation() {
        location = UserLocation.getCurrentLocation(TripActivity.this);

        isReadyToSave(false);

        tvLocation.setText((location.lat == null) ? "Where?" : location.name);
        tvLocation.setTextColor(getResources().getColor((location.lat == null) ? R.color.selected_blue : R.color.black));

        if (location.lat != null) {
            toggleMovesView(true);
            getNearbyRestaurants();
        }
    }

    private void goToSelectUsersActivity() {
        Intent intent = new Intent(this, SelectUsersActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.right_in, R.anim.left_out);
    }


    private void goToLocationActivity() {
        Intent intent = new Intent(getApplicationContext(), LocationActivity.class);
        intent.putExtra("isTrip", true);
        startActivityForResult(intent, LOCATION_REQUEST_CODE);
        overridePendingTransition(R.anim.right_in, R.anim.left_out);
    }

    private void goToCalendarActivity() {
        Intent intent = new Intent(getApplicationContext(), CalendarActivity.class);
        intent.putExtra("isTrip", true);
        startActivityForResult(intent, CALENDAR_REQUEST_CODE);
        overridePendingTransition(R.anim.right_in, R.anim.left_out);
    }


    private void updateMoves(List<Move> replacementArray) {
        rvMoves.setAdapter(movesAdapter);

        moves.clear();
        moves.addAll(replacementArray);
        movesAdapter.notifyDataSetChanged();
    }


    private void getTripFriends() {
        pb.setVisibility(View.VISIBLE);
        ParseQuery<ParseObject> friendQuery = ParseQuery.getQuery("Friend");
        friendQuery.whereEqualTo("trip", currentTrip.parseObject);
        friendQuery.include("receiver");
        friendQuery.findInBackground(((objects, e) -> {
            if (e == null) {
                for (int i = 0; i < objects.size(); i++) {
                    ParseUser receiver = objects.get(i).getParseUser("receiver");
                    selectedFriends.add(receiver);
                }

                pb.setVisibility(View.INVISIBLE);
                didCheckForFriends = true;

                rvMoves.setAdapter(userAdapter);
                userAdapter.notifyDataSetChanged();
            } else {
                pb.setVisibility(View.INVISIBLE);
                didCheckForFriends = true;

                Toast.makeText(getApplicationContext(), "error getting friends", Toast.LENGTH_SHORT).show();
                Log.e(TAG, e.getMessage());
                e.printStackTrace();
            }
        }));
    }


    private boolean isReadyToSave(boolean needsToast) {
        if (etTrip.getText().toString().trim().equals("") || location.lat == null || dates.size() == 0) {
            btnRight.setTextColor(getResources().getColor(R.color.light_grey));
            if (needsToast) {
                String toastTitle = "";

                if (etTrip.getText().toString().trim().equals("")) {
                    toastTitle = "Name this trip";
                } else if (location.lat == null) {
                    toastTitle = "Set a location for this trip";
                } else {
                    toastTitle = "Set trip dates";
                }

                Toast.makeText(getApplicationContext(), toastTitle, Toast.LENGTH_LONG).show();
            }

            return false;
        }

        btnRight.setTextColor(getResources().getColor(R.color.theme));
        return true;
    }


    private void saveTrip() {
        if (!isReadyToSave(true)) {
            return;
        } else if (isEditingTrip) {
            saveUpdates();
        } else {
            pb.setVisibility(View.VISIBLE);
            btnRight.setEnabled(false);

            ParseObject trip = new ParseObject("Trip");

            trip.put("name", etTrip.getText().toString().trim());
            trip.put("locationName", location.name);
            trip.put("lat", location.lat);
            trip.put("lng", location.lng);
            trip.put("postalCode", (location.postalCode == null) ? "" : location.postalCode);
            trip.put("owner", ParseUser.getCurrentUser());

            trip.put("startDay", dates.get(0).getDay());
            trip.put("startMonth", dates.get(0).getMonth());
            trip.put("startYear", dates.get(0).getYear());
            trip.put("endDay", dates.get(dates.size() - 1).getDay());
            trip.put("endMonth", dates.get(dates.size() - 1).getMonth());
            trip.put("endYear", dates.get(dates.size() - 1).getYear());

            trip.saveInBackground((e) -> {
                btnRight.setEnabled(true);
                if (e == null) {
                    pb.setVisibility(View.INVISIBLE);
                    Toast.makeText(getApplicationContext(), "Trip Saved!", Toast.LENGTH_LONG).show();

                    UserLocation.clearCurrentTripLocation(getApplicationContext());

                    saveTripMoves(trip);
                    saveTripFriends(trip);

                    onBackPressed();
                } else {
                    pb.setVisibility(View.INVISIBLE);
                    Toast.makeText(getApplicationContext(), "error saving trip", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, e.getMessage());
                    e.printStackTrace();
                }
            });
        }
    }



    private void saveUpdates() {
        pb.setVisibility(View.VISIBLE);
        btnRight.setEnabled(false);
        ParseObject trip = currentTrip.parseObject;

        trip.put("name", etTrip.getText().toString().trim());
        trip.put("locationName", location.name);
        trip.put("lat", location.lat);
        trip.put("lng", location.lng);
        trip.put("postalCode", location.postalCode);
        trip.put("owner", ParseUser.getCurrentUser());

        trip.put("startDay", dates.get(0).getDay());
        trip.put("startMonth", dates.get(0).getMonth());
        trip.put("startYear", dates.get(0).getYear());
        trip.put("endDay", dates.get(dates.size() - 1).getDay());
        trip.put("endMonth", dates.get(dates.size() - 1).getMonth());
        trip.put("endYear", dates.get(dates.size() - 1).getYear());

        trip.saveInBackground((e) -> {
            btnRight.setEnabled(true);
            if (e == null) {
                pb.setVisibility(View.INVISIBLE);
                UserLocation.clearCurrentTripLocation(getApplicationContext());
                saveTripMoves(trip);

                onBackPressed();
            } else {
                pb.setVisibility(View.INVISIBLE);
                Toast.makeText(getApplicationContext(), "Error updating trip!", Toast.LENGTH_SHORT).show();
                Log.e(TAG, e.getMessage());
                e.printStackTrace();
            }
        });
    }

    private void saveTripFriends(ParseObject trip) {
        if (selectedFriends.size() > 0 && !isEditingTrip) {
            for (ParseUser selectedFriend : selectedFriends) {
                saveFriendToTrip(selectedFriend, trip);
            }
        } else if (newSelectedFriends.size() > 0 && isEditingTrip) {
            for (ParseUser selectedFriend : newSelectedFriends) {
                saveFriendToTrip(selectedFriend, trip);
            }
        }
    }


    private void saveFriendToTrip(ParseUser user, ParseObject trip) {
        ParseObject friend = new ParseObject("Friend");

        friend.put("trip", trip);
        friend.put("receiver", user);

        friend.saveInBackground((e) -> {
            if (e != null) {
                Toast.makeText(getApplicationContext(), "error saving friend to trip, " + user.getUsername(), Toast.LENGTH_SHORT).show();
                Log.e(TAG, e.getMessage());
                e.printStackTrace();
            }
        });
    }


    private void getSavedTripMoves(Trip trip) {
        ParseQuery<ParseObject> moveQuery = ParseQuery.getQuery("Move");
        moveQuery.whereEqualTo("trip", trip.parseObject);
        moveQuery.orderByDescending("createdAt");
        moveQuery.findInBackground((objects, e) -> {
            didCheckSavedSelected = true;

            if (e == null) {
                serverMoves.addAll(objects);

                List<Move> moves = new ArrayList<>();

                for (int i = 0; i < objects.size(); i++) {
                    if (objects.get(i).getString("moveType").equals("food")) {
                        moves.add(Move.fromParseObject(objects.get(i)));
                    } else {
                        moves.add(Move.fromParseObject(objects.get(i)));
                    }
                }
                selectedMoves.addAll(moves);
            } else {
                Log.e(TAG, "Error getting selected moves");
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Error getting selected moves", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void saveTripMoves(ParseObject trip) {
        if (isEditingTrip && deleteFromServerMoves.size() > 0) {
            for (int i = 0; i < deleteFromServerMoves.size(); i++) {
                for (int a = 0; a < serverMoves.size(); a++) {
                    if (serverMoves.get(a).getString("placeId").equals(deleteFromServerMoves.get(i).id)) {
                        serverMoves.get(a).deleteInBackground();
                        break;
                    }
                }
            }
        }

        if (selectedMoves.size() > 0 && !isEditingTrip) {
            for (Move selectedMove : selectedMoves) {
                saveMoveToTrip(selectedMove, trip);
            }
        } else if (newSelectedMoves.size() > 0 && isEditingTrip) {
            for (Move selectedMove : newSelectedMoves) {
                saveMoveToTrip(selectedMove, trip);
            }
        }
    }

    private void saveMoveToTrip(Move selectedMove, ParseObject trip) {
        ParseObject move = new ParseObject("Move");

        move.put("name", selectedMove.name);
        move.put("placeId", selectedMove.id);
        move.put("trip", trip);
        move.put("moveType", (selectedMove.moveType));

        if (selectedMove.moveType.equals("food")){
            if (((Restaurant) selectedMove).movePhotos != null) {
                move.put("photoUrl", ((Restaurant) selectedMove).movePhotos.get(0).getPhotoURL(getApplicationContext()));
            }
        } else {
            if (selectedMove.photo != null) {
                move.put("photoUrl", selectedMove.photo);
            }
        }

        move.saveInBackground((e) -> {
            if (e != null) {
                Toast.makeText(getApplicationContext(), "Error saving move: " + selectedMove.name, Toast.LENGTH_SHORT).show();
                Log.e(TAG, e.getMessage());
                e.printStackTrace();
            }
        });
    }


    private void getNearbyRestaurants() {
        pb.setVisibility(View.VISIBLE);

        String apiUrl = API_BASE_URL + "/place/nearbysearch/json";
        float distance = MoveCategoriesHelper.milesToMeters(10);

        RequestParams params = new RequestParams();
        params.put("location",location.lat + "," + location.lng);
        params.put("radius", (distance > 50000) ? 50000 : distance);
        params.put("type","restaurant");


        String userFoodPref = MoveCategoriesHelper.getUserFoodPreferenceString(new ArrayList<>());

        if (!userFoodPref.equals("")) {
            params.put("keyword", "mexican");
        }

        params.put("key", getString(R.string.api_key));

        HomeActivity.client.get(apiUrl, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                pb.setVisibility(View.INVISIBLE);

                foodMoves.clear();
                try {
                    foodMoves = new ArrayList<>();
                    Move.arrayFromJSONArray(foodMoves, response.getJSONArray("results"), "food");
                    updateMoves(foodMoves);
                } catch (JSONException e) {
                    Log.e(TAG, e.getMessage());
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                pb.setVisibility(View.INVISIBLE);
                new StatusCodeHandler(TAG, statusCode);
                throwable.printStackTrace();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                pb.setVisibility(View.INVISIBLE);
                new StatusCodeHandler(TAG, statusCode);
                throwable.printStackTrace();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                pb.setVisibility(View.INVISIBLE);
                new StatusCodeHandler(TAG, statusCode);
                throwable.printStackTrace();
            }
        });
    }


    private void getNearbyEvents() {
        pb.setVisibility(View.VISIBLE);
        checkForPostalCode();

        String API_BASE_URL_TM = "https://app.ticketmaster.com/discovery/v2/events";
        String apiUrl = API_BASE_URL_TM + ".json";

        RequestParams params = new RequestParams();

        params.put("apikey", getString(R.string.api_key_tm));
        params.put("postalCode", location.postalCode);
        if (dates.size() != 0) {
            params.put("localStartDateTime", Trip.getAPIDateFormat(dates.get(0), dates.get(dates.size() - 1)));
        }
        params.put("sort", "date,asc");

            JSONArray currUserPrefList = ParseUser.getCurrentUser().getJSONArray("eventPrefList");
            if (currUserPrefList != null) {
                try {
                    for (int i = 0; i < currUserPrefList.length(); i++) {
                        String pref = currUserPrefList.get(i).toString();
                        params.put("keyword", pref);
                    }
                } catch (JSONException e) {
                    Log.e(TAG, e.getMessage());
                    e.printStackTrace();
                }
            }

        HomeActivity.clientTM.get(apiUrl, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                pb.setVisibility(View.INVISIBLE);

                eventMoves.clear();

                if (response.has("_embedded")) {
                    try {
                        eventMoves = new ArrayList<>();
                        Move.arrayFromJSONArray(eventMoves, response.getJSONObject("_embedded").getJSONArray("events"), "event");
                        updateMoves(eventMoves);
                    } catch (JSONException e) {
                        Log.e(TAG, "Error getting events");
                        e.printStackTrace();
                    }
                } else {
                    updateMoves(eventMoves);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                pb.setVisibility(View.INVISIBLE);
                new StatusCodeHandler(TAG, statusCode);
                Log.i(TAG, errorResponse.toString());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                pb.setVisibility(View.INVISIBLE);
                new StatusCodeHandler(TAG, statusCode);
                Log.i(TAG, errorResponse.toString());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                pb.setVisibility(View.INVISIBLE);
                new StatusCodeHandler(TAG, statusCode);
                Log.i(TAG, responseString);
            }
        });
    }


    private void checkForPostalCode() {
        if (location.postalCode.equals("")) {

            if (location.lat.equals("0.0") && location.lng.equals("0.0")) {
                Toast.makeText(getApplicationContext(), "Set a location", Toast.LENGTH_LONG).show();
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

                        UserLocation newLocation = UserLocation.addingPostalCodeFromJSON(getApplicationContext(), false, location, response);
                        location.postalCode = newLocation.postalCode;

                        if (!newLocation.equals("")) {
                            return;
                        } else {
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


    private final TextWatcher charTextWatcher= new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

        public void onTextChanged(CharSequence s, int start, int before, int count) { isReadyToSave(false); }

        @Override
        public void afterTextChanged(Editable s) { }
    };

}
