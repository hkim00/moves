package com.hkim00.moves;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
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

import com.hkim00.moves.adapters.MoveAdapter;
import com.hkim00.moves.models.Event;
import com.hkim00.moves.models.Move;
import com.hkim00.moves.models.Restaurant;
import com.hkim00.moves.models.Trip;
import com.hkim00.moves.models.UserLocation;
import com.hkim00.moves.util.MoveCategoriesHelper;
import com.hkim00.moves.util.StatusCodeHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.prolificinteractive.materialcalendarview.CalendarDay;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class TripActivity extends AppCompatActivity {

    private final static String TAG = "TripActivity";
    public static final String API_BASE_URL = "https://maps.googleapis.com/maps/api";
    private static final int LOCATION_REQUEST_CODE = 20;
    public static final int CALENDAR_REQUEST_CODE = 30;


    private EditText etTrip;
    private TextView tvLocation, tvCalendar, tvFood, tvEvents, tvSelected;
    private Button btnLocation, btnCalendar, btnFood, btnEvents, btnSelected, btnSave;
    private View vFoodView, vEventsView, vSelectedView;

    private ProgressBar pb;
    private RecyclerView rvMoves;
    private MoveAdapter movesAdapter;

    private Trip currentTrip;
    private UserLocation location;
    public static List<CalendarDay> dates;
    private List<Move> foodMoves, eventMoves, moves;
    public static List<Move> selectedMoves, newSelectedMoves, deleteFromServerMoves;

    private List<ParseObject> serverMoves;

    public static boolean isEditingTrip;
    private boolean didCheckSavedSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip);

        getViewIds();

        setupRecyclerView();

        setupButtons();

        setupView();
    }


    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.left_in, R.anim.right_out);
    }


    private void getViewIds() {
        etTrip = findViewById(R.id.etTripName);
        tvLocation = findViewById(R.id.tvLocation);
        btnLocation = findViewById(R.id.btnLocation);
        tvCalendar = findViewById(R.id.tvCalendar);
        btnCalendar = findViewById(R.id.btnCalendar);

        tvFood = findViewById(R.id.tvFood);
        tvEvents = findViewById(R.id.tvEvents);
        tvSelected = findViewById(R.id.tvSelected);

        btnFood = findViewById(R.id.btnFood);
        btnEvents = findViewById(R.id.btnEvents);
        btnSelected = findViewById(R.id.btnSelected);

        vFoodView = findViewById(R.id.vFoodView);
        vEventsView = findViewById(R.id.vEventsView);
        vSelectedView = findViewById(R.id.vSelectedView);

        pb = findViewById(R.id.pb);
        rvMoves = findViewById(R.id.rvMoves);
        btnSave = findViewById(R.id.btnSave);
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

        serverMoves = new ArrayList<>();

        movesAdapter = new MoveAdapter(TripActivity.this, moves);
        rvMoves.setAdapter(movesAdapter);
    }


    private void setupButtons() {
        btnLocation.setOnClickListener(view -> goToLocationActivity());

        btnCalendar.setOnClickListener(view -> goToCalendarActivity());

        btnFood.setOnClickListener(view -> toggleSection("food"));
        btnEvents.setOnClickListener(view -> toggleSection("events"));
        btnSelected.setOnClickListener(view -> toggleSection("selected"));

        btnSave.setOnClickListener(view -> saveTrip());
    }

    private void toggleSection(String type) {
        vFoodView.setVisibility((type.equals("food")) ? View.VISIBLE : View.INVISIBLE);
        vEventsView.setVisibility((type.equals("events")) ? View.VISIBLE : View.INVISIBLE);
        vSelectedView.setVisibility((type.equals("selected")) ? View.VISIBLE : View.INVISIBLE);

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

        didCheckSavedSelected = false;

        if (getIntent().hasExtra("trip")) {
            currentTrip = Parcels.unwrap(getIntent().getParcelableExtra("trip"));

            location = currentTrip.location;
            etTrip.setText(currentTrip.name);

            dates.add(currentTrip.startDay);
            dates.add(currentTrip.endDay);

            tvLocation.setText(currentTrip.location.name);
            tvCalendar.setText(getDateRangeString());

            tvLocation.setTextColor(getResources().getColor(R.color.black));
            tvCalendar.setTextColor(getResources().getColor(R.color.black));

            isEditingTrip = true;

            isReadyToSave(false);

            getNearbyRestaurants();
            getSavedTripMoves(currentTrip);
        } else {
            location = UserLocation.clearCurrentTripLocation(this);

            tvLocation.setTextColor(getResources().getColor(R.color.selected_blue));
            tvCalendar.setTextColor(getResources().getColor(R.color.selected_blue));

            isEditingTrip = false;

            btnSave.setBackgroundColor(getResources().getColor(R.color.light_grey));

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
                tvCalendar.setText(getDateRangeString());
            } else {
                tvCalendar.setText("When?");
                tvCalendar.setTextColor(getResources().getColor(R.color.selected_blue));
            }

            isReadyToSave(false);
        }
    }

    private String getDateRangeString() {
        String[] months = new DateFormatSymbols().getShortMonths();
        return months[dates.get(0).getMonth() - 1] + " " + dates.get(0).getDay() + " - " + months[dates.get(dates.size() - 1).getMonth() - 1] + " " + dates.get(dates.size() - 1).getDay();
    }


    private void checkForCurrentLocation() {
        location = UserLocation.getCurrentTripLocation(this);

        isReadyToSave(false);

        if (location.lat == null) {
            tvLocation.setText("Where?");
            tvLocation.setTextColor(getResources().getColor(R.color.selected_blue));
        } else {
            tvLocation.setText(location.name);
            tvLocation.setTextColor(getResources().getColor(R.color.black));

            toggleMovesView(true);
            getNearbyRestaurants();
        }
    }


    private void goToLocationActivity() {
        Intent intent = new Intent(getApplicationContext(), LocationActivity.class);
        intent.putExtra("isTrip", true);
        startActivityForResult(intent, LOCATION_REQUEST_CODE);
        overridePendingTransition(R.anim.right_in, R.anim.left_out);
    }

    private void goToCalendarActivity() {
        Intent intent = new Intent(getApplicationContext(), CalendarActivity.class);
        startActivityForResult(intent, CALENDAR_REQUEST_CODE);
        overridePendingTransition(R.anim.right_in, R.anim.left_out);
    }


    private void updateMoves(List<Move> replacementArray) {
        moves.clear();
        moves.addAll(replacementArray);

        movesAdapter.notifyDataSetChanged();
    }


    private boolean isReadyToSave(boolean needsToast) {
        if (etTrip.getText().toString().trim().equals("")) {
            if (needsToast) {
                Toast.makeText(getApplicationContext(), "Name this trip", Toast.LENGTH_LONG).show();
            }
            btnSave.setBackgroundColor(getResources().getColor(R.color.light_grey));
            return false;
        }

        if (location.lat == null) {
            if (needsToast) {
                Toast.makeText(getApplicationContext(), "Set a location for this trip", Toast.LENGTH_LONG).show();
            }
            btnSave.setBackgroundColor(getResources().getColor(R.color.light_grey));
            return false;
        }

        if (dates.size() == 0) {
            if (needsToast) {
                Toast.makeText(getApplicationContext(), "Set trip dates", Toast.LENGTH_LONG).show();
            }

            btnSave.setBackgroundColor(getResources().getColor(R.color.light_grey));
            return false;
        }

        btnSave.setBackgroundColor(getResources().getColor(R.color.selected_blue));
        return true;
    }

    private void saveTrip() {
        if (!isReadyToSave(true)) {
            return;
        } else if (isEditingTrip) {
            saveUpdates();
        } else {
            btnSave.setEnabled(false);

            ParseObject trip = new ParseObject("Trip");

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

            trip.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    btnSave.setEnabled(true);
                    if (e == null) {
                        Toast.makeText(getApplicationContext(), "trip saved", Toast.LENGTH_LONG).show();

                        UserLocation.clearCurrentTripLocation(getApplicationContext());

                        saveTripMoves(trip);

                        onBackPressed();
                    } else {
                        Toast.makeText(getApplicationContext(), "error saving trip", Toast.LENGTH_SHORT).show();
                        Log.e(TAG, e.getMessage());
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    private void saveUpdates() {
        btnSave.setEnabled(false);
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

        trip.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                btnSave.setEnabled(true);
                if (e == null) {
                    UserLocation.clearCurrentTripLocation(getApplicationContext());
                    saveTripMoves(trip);

                    onBackPressed();
                } else {
                    Toast.makeText(getApplicationContext(), "error updating trip", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, e.getMessage());
                    e.printStackTrace();
                }
            }
        });

    }

    private void getSavedTripMoves(Trip trip) {
        ParseQuery<ParseObject> moveQuery = ParseQuery.getQuery("Move");
        moveQuery.whereEqualTo("trip", trip.parseObject);
        moveQuery.orderByDescending("createdAt");
        moveQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                didCheckSavedSelected = true;

                if (e == null) {
                    serverMoves.addAll(objects);

                    List<Move> moves = new ArrayList<>();

                    for (int i = 0; i < objects.size(); i++) {
                        if (objects.get(i).getString("moveType").equals("food")) {
                            moves.add(Restaurant.fromParseObject(objects.get(i)));
                        } else {
                            moves.add(Event.fromParseObject(objects.get(i)));
                        }
                    }
                    selectedMoves.addAll(moves);
                } else {
                    Log.e(TAG, "Error getting selected moves");
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Error getting selected moves", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void saveTripMoves(ParseObject trip) {
        if (isEditingTrip && deleteFromServerMoves.size() > 0) {
            for (int i = 0; i < deleteFromServerMoves.size(); i++) {
                for (int a = 0; a < serverMoves.size(); a++) {
                    if (serverMoves.get(a).getString("placeId").equals(deleteFromServerMoves.get(i).getId())) {
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

        move.put("name", selectedMove.getName());
        move.put("placeId", selectedMove.getId());
        move.put("trip", trip);
        move.put("moveType", (selectedMove.getMoveType() == Restaurant.RESTAURANT) ? "food" : "event");

        move.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    Toast.makeText(getApplicationContext(), "error saving move " + selectedMove.getName(), Toast.LENGTH_SHORT).show();
                    Log.e(TAG, e.getMessage());
                    e.printStackTrace();
                }
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
                    foodMoves.addAll(Restaurant.arrayFromJSONArray(response.getJSONArray("results")));
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


    private String getDateFormat() {
        if (dates.size() == 0) {
            return "";
        }

        CalendarDay beginDate = dates.get(0);
        CalendarDay endDate = dates.get(dates.size() - 1);

        String beginDateString = beginDate.getYear() + "-" +
                ((beginDate.getMonth() < 10) ? "0" + beginDate.getMonth() : beginDate.getMonth())
                + "-" +
                ((beginDate.getDay() < 10) ? "0" + beginDate.getDay() : beginDate.getDay())
                + "T00:00:00,";
        String endDateString = endDate.getYear() + "-" +
                ((endDate.getMonth() < 10) ? "0" + endDate.getMonth() : endDate.getMonth())
                + "-" +
                ((endDate.getDay() < 10) ? "0" + endDate.getDay() : endDate.getDay())
                + "T00:00:00";

        return beginDateString + endDateString;
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
            params.put("localStartDateTime", getDateFormat());
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

                JSONArray events;
                if (response.has("_embedded")) {
                    try {
                        eventMoves = Event.arrayFromJSONArray((response.getJSONObject("_embedded")).getJSONArray("events"));
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
