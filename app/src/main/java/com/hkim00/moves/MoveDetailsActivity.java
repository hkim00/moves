package com.hkim00.moves;

import android.app.ActionBar;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hkim00.moves.adapters.MoveDetailsAdapter;
import com.hkim00.moves.models.Event;
import com.hkim00.moves.models.Move;
import com.hkim00.moves.models.MoveText;
import com.hkim00.moves.models.Restaurant;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

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

        setupActionBar();

        setupRecyclerView();

        getMoveDetails();
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.left_in, R.anim.right_out);
    }


    private void setupActionBar() {
        androidx.appcompat.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.action_bar_grey)));

        this.getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.action_bar);
        getSupportActionBar().setElevation(2);
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
}