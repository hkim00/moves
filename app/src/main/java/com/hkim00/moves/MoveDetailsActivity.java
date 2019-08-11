package com.hkim00.moves;

import android.app.ActionBar;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

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
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

import static com.hkim00.moves.util.ParseUtil.getParseQuery;

public class MoveDetailsActivity extends AppCompatActivity {

    private final static String TAG = "MoveDetailsActivity";

    private ProgressBar progressBar;
    private RecyclerView rvMove;
    private MoveDetailsAdapter adapter;
    public static Move move;
    private List<Move> moves;

    private static ImageView ivRight;

    private static String subCategory;

    private boolean isTrip;
    private String eventPhoto;

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
        getSupportActionBar().setCustomView(R.layout.action_bar_lt_rt);
        getSupportActionBar().setElevation(2);

        Button btnLeft = findViewById(R.id.btnLeft);
        btnLeft.setOnClickListener(view -> onBackPressed());

        ivRight = findViewById(R.id.ivRight);
        Button btnRight = findViewById(R.id.btnRight);
        btnRight.setOnClickListener(view -> rightButtonAction());
    }

    private void rightButtonAction() {
        if (!move.didComplete) {
            saveMove();
        } else {
            favoriteMove();
        }
    }

    private void setupRecyclerView() {
        progressBar = findViewById(R.id.progressBar);
        rvMove = findViewById(R.id.rvMove);

        progressBar.setVisibility(View.INVISIBLE);

        moves = new ArrayList<>();
        move = Parcels.unwrap(getIntent().getParcelableExtra("move"));
        isTrip = getIntent().getBooleanExtra("isTrip", false);
        moves.add(move);

        rvMove.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        adapter = new MoveDetailsAdapter(MoveDetailsActivity.this, moves, isTrip);
        rvMove.setAdapter(adapter);
    }


    private void getMoveDetails() {
        subCategory = move.subCategory;
        progressBar.setVisibility(View.VISIBLE);
        if (move.moveType.equals("food")) {
            getFoodDetails();
        } else {
            getEventDetails();
        }
    }

    private void checkIfInParse() {
        if (move != null) {
            ParseQuery<ParseObject> detailsQuery = getParseQuery(ParseUser.getCurrentUser(), move);
            detailsQuery.findInBackground(((objects, e) -> {
                progressBar.setVisibility(View.INVISIBLE);
                if (e == null) {
                    for (int i = 0; i < objects.size(); i++) {
                        ParseObject moveParseObject = objects.get(0);

                        move.parseObject = moveParseObject;

                        move.didComplete = (moveParseObject.has("didComplete")) ? moveParseObject.getBoolean("didComplete") : false;
                        move.didFavorite = (moveParseObject.has("didFavorite")) ? moveParseObject.getBoolean("didFavorite") : false;
                        move.didSave = (moveParseObject.has("didSave")) ? moveParseObject.getBoolean("didSave") : false;

                        if (move.didComplete) {
                            ivRight.setImageResource((move.didFavorite) ? R.drawable.ufi_heart_active : R.drawable.ufi_heart);
                        } else {
                            ivRight.setImageResource((move.didSave) ? R.drawable.ufi_save_active : R.drawable.ufi_save);
                        }
                    }
                } else {
                    Log.e(TAG, e.getMessage());
                    e.printStackTrace();
                }
            }));
        }
    }

    private void saveMove() {
        if (move != null) {
            move.didSave = !move.didSave;
            ivRight.setImageResource((move.didSave) ? R.drawable.ufi_save_active : R.drawable.ufi_save);

            if (move.parseObject != null) {
                move.parseObject.put("didSave", move.didSave);
                move.parseObject.saveInBackground();
            } else {
                move.saveToParse();
            }
        }
    }

    private void favoriteMove() {
        if (move != null) {
            move.didFavorite = !move.didFavorite;
            ivRight.setImageResource((move.didFavorite) ? R.drawable.ufi_heart_active : R.drawable.ufi_heart);

            if (move.parseObject != null) {
                move.parseObject.put("didFavorite", move.didFavorite);
                move.parseObject.saveInBackground();
            } else {
                move.saveToParse();
            }
        }
    }


    public static void changeSaveToFav() {
        ivRight.setImageResource((move.didFavorite) ? R.drawable.ufi_heart_active : R.drawable.ufi_heart);
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
                    moveResult.subCategory = subCategory;
                    move = moveResult;

                    moves.clear();
                    moves.add(move);

                    if (move.lat != null) {
                        adapter.notifyDataSetChanged();
                        checkIfInParse();
                    }

                } catch (JSONException e) {
                    progressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(getApplicationContext(), "Error getting restaurant information", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "Error getting restaurant");
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                progressBar.setVisibility(View.INVISIBLE);
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Log.e(TAG, errorResponse.toString());
                throwable.printStackTrace();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                progressBar.setVisibility(View.INVISIBLE);
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Log.e(TAG, errorResponse.toString());
                throwable.printStackTrace();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                progressBar.setVisibility(View.INVISIBLE);
                super.onFailure(statusCode, headers, responseString, throwable);
                Log.e(TAG, responseString);
                throwable.printStackTrace();
            }
        });
    }

    private void getEventDetails() {
        if (move.photo != null) {
            eventPhoto = move.photo;
        }

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
                    move.photo = eventPhoto;

                    moves.clear();
                    moves.add(move);

                    if (move.lat != null) {
                        adapter.notifyDataSetChanged();
                        checkIfInParse();
                    }

                  } catch (JSONException e) {
                    progressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(getApplicationContext(), "Error getting event information", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "Error getting event");
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                progressBar.setVisibility(View.INVISIBLE);
                Log.e(TAG, errorResponse.toString());
                throwable.printStackTrace();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                progressBar.setVisibility(View.INVISIBLE);
                Log.e(TAG, errorResponse.toString());
                throwable.printStackTrace();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                progressBar.setVisibility(View.INVISIBLE);
                Log.e(TAG, responseString);
                throwable.printStackTrace();
            }
        });
    }
}