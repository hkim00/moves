package com.hkim00.moves;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ActionBar;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
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
import com.hkim00.moves.adapters.UserAdapter;
import com.hkim00.moves.models.Move;
import com.hkim00.moves.models.UserLocation;
import com.hkim00.moves.util.ParseUtil;
import com.hkim00.moves.util.StatusCodeHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class SearchActivity extends AppCompatActivity {

    private final static String TAG = "SearchActivity";

    private TextView tvType;
    private EditText etSearch;
    private Button btnType;
    private RecyclerView rvSearchResults;
    private ProgressBar progressBar;

    private List<Move> moveResults;
    private List<ParseUser> userResults;

    private MoveAdapter moveAdapter;
    private UserAdapter userAdapter;

    private UserLocation location;
    private boolean isTimerRunning;
    private String type;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        setupActionBar();

        getViewIds();

        setupView();

        btnType.setOnClickListener(view -> toggleType());
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.left_in, R.anim.right_out);
    }


    private void getViewIds() {
        tvType = findViewById(R.id.tvType);
        etSearch = findViewById(R.id.etSearch);
        btnType = findViewById(R.id.btnType);
        rvSearchResults = findViewById(R.id.rvSearchResults);
        progressBar = findViewById(R.id.progressBar);
    }


    private void setupActionBar() {
        androidx.appcompat.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.action_bar_grey)));

        this.getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.action_bar_lt);
        getSupportActionBar().setElevation(2);

        Button btnLeft = findViewById(R.id.btnLeft);
        btnLeft.setOnClickListener(view -> onBackPressed());
    }


    private void toggleType() {
        etSearch.setText("");
        checkIfNeedToClearRecyclerView();

        if (type.equals("users")) {
            type = "food";
            tvType.setText("Food");
            etSearch.setHint("Restaurant name");
        } else if (type.equals("food")) {
            type = "events";
            tvType.setText("Event");
            etSearch.setHint("Event title");
        } else {
            type = "users";
            tvType.setText("User");
            etSearch.setHint("Username");
        }
    }

    private void setupView() {
        progressBar.setVisibility(View.INVISIBLE);

        location = UserLocation.getCurrentLocation(getApplicationContext());
        etSearch.addTextChangedListener(charTextWatcher);

        type = "users";
        isTimerRunning = false;

        moveResults = new ArrayList<>();
        userResults = new ArrayList<>();

        moveAdapter = new MoveAdapter(this, moveResults);
        userAdapter = new UserAdapter(this, userResults);

        rvSearchResults.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        rvSearchResults.setAdapter(userAdapter);
    }


    private void searchTextUpdated() {
        if (etSearch.getText().toString().toLowerCase().trim().equals("")) {
            checkIfNeedToClearRecyclerView();
            return;
        }

        if (!isTimerRunning) {
            isTimerRunning = true;
            startTimer();
        }
    }

    private void startTimer() {
        new CountDownTimer(500, 500) { //0.5 seconds

            public void onTick(long millisUntilFinished) { }

            public void onFinish() {
                progressBar.setVisibility(View.VISIBLE);
                if (type.equals("users")) {
                    findUsers();
                } else {
                    findMoves();
                }
                isTimerRunning = false;
            }
        }.start();
    }

    private void findUsers() {
        ParseQuery<ParseObject> userQuery = ParseQuery.getQuery("_User");
        userQuery.whereContains("username", etSearch.getText().toString().toLowerCase().trim());
        userQuery.orderByDescending("createdAt");

        userQuery.findInBackground((objects, e) -> {
            if (e == null) {
                progressBar.setVisibility(View.INVISIBLE);
                List<ParseUser> users = new ArrayList<>();

                for (int i = 0; i < objects.size(); i++) {
                    if (!objects.get(i).getObjectId().equals(ParseUser.getCurrentUser().getObjectId())) {
                        users.add((ParseUser) objects.get(i));
                    }
                }

                updateUsers(users);

            } else {
                progressBar.setVisibility(View.INVISIBLE);
                Log.e(TAG, "Error finding users with usernames' containing: " + etSearch.getText().toString().toLowerCase().trim());
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Error finding users", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void checkIfNeedToClearRecyclerView() {
        if (etSearch.getText().toString().toLowerCase().trim().equals("")) {
            if (type.equals("users")) {
                userResults.clear();
                userAdapter.notifyDataSetChanged();
            } else {
                moveResults.clear();
                moveAdapter.notifyDataSetChanged();
            }
        }
    }

    private void updateUsers(List<ParseUser> users) {
        userResults.clear();
        userResults.addAll(users);

        rvSearchResults.setAdapter(userAdapter);
        userAdapter.notifyDataSetChanged();

        checkIfNeedToClearRecyclerView();
    }

    private void updateMoves(List<Move> moves) {
        moveResults.clear();
        moveResults.addAll(moves);

        rvSearchResults.setAdapter(moveAdapter);
        moveAdapter.notifyDataSetChanged();

        checkIfNeedToClearRecyclerView();
    }

    private final TextWatcher charTextWatcher= new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

        public void onTextChanged(CharSequence s, int start, int before, int count) { searchTextUpdated(); }

        @Override
        public void afterTextChanged(Editable s) { }
    };


    private void findMoves() {
        if (location.lat == null || location.lng == null) {
            progressBar.setVisibility(View.INVISIBLE);
            Toast.makeText(getApplicationContext(), "Set a location", Toast.LENGTH_LONG).show();
            return;
        }


        final String API_URL = (type.equals("food")) ? "https://maps.googleapis.com/maps/api/place/textsearch/json?" : "https://app.ticketmaster.com/discovery/v2/events.json";
        RequestParams params = new RequestParams();

        if (type.equals("food")) {
            params.put("key", getString(R.string.api_key));
            params.put("query", etSearch.getText().toString().toLowerCase().trim());
            params.put("radius", 20000);
            params.put("location", location.lat + "," + location.lng);
        } else {
            params.put("apikey", getString(R.string.api_key_tm));
            params.put("postalCode", location.postalCode);
            params.put("sort", "date,asc");
            params.put("keyword", etSearch.getText().toString().toLowerCase().trim());
        }

        HomeActivity.client.get(API_URL, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                List<Move> moveResults = new ArrayList<>();
                try {
                    Move.arrayFromJSONArray(moveResults, response.getJSONArray("results"), type);
                } catch (JSONException e) {
                    Log.e(TAG, e.getMessage());
                    e.printStackTrace();
                }

                progressBar.setVisibility(View.INVISIBLE);
                updateMoves(moveResults);
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
