package com.hkim00.moves;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.hkim00.moves.models.Restaurant;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class HomeActivity extends AppCompatActivity {

    public final static String TAG = "HomeActivity";
    public static final String API_BASE_URL = "https://maps.googleapis.com/maps/api/place";
    private static int screenWidth;

    public AsyncHttpClient client;

    private String time;
    private String numberOfPeople;
    private int distance;
    private int priceLevel;
    private List<Restaurant> restaurantResults;

    private TextView tvLocation, tvDistance, tvPriceLevel;
    private ImageView ivDistance, ivPrice;
    private Button btnTime, btnPeople, btnDistance, btnPrice;

    private ConstraintLayout clCategories;
    private ImageView ivFood, ivActivities, ivAttractions, ivEvents;

    private ConstraintLayout clPrice;
    private TextView tvRightPopupTitle, tvMiles;
    private EditText etDistance;
    private Button btnPriceLevel1, btnPriceLevel2, btnPriceLevel3, btnPriceLevel4;

    private Button btnMove, btnRiskyMove;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // TODO: Francesco - the reason why gitguardian was giving us notifications is that you'd hardcoded the api key in this file as a string...
        Places.initialize(getApplicationContext(), getString(R.string.api_key));
        PlacesClient placesClient = Places.createClient(this);

        client = new AsyncHttpClient();
        restaurantResults = new ArrayList<>();

        getViewIds();

        setupDesign();

        setupButtons();

        Button btnLocation = findViewById(R.id.btnLocation);
        btnLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
    }

    private void getViewIds(){
        tvLocation = findViewById(R.id.tvLocation);
        btnTime = findViewById(R.id.btnTime);
        btnPeople = findViewById(R.id.btnPeople);
        ivDistance = findViewById(R.id.ivDistance);
        tvDistance = findViewById(R.id.tvDistance);
        btnDistance = findViewById(R.id.btnDistance);
        ivPrice = findViewById(R.id.ivPrice);
        btnPrice = findViewById(R.id.btnPrice);
        tvPriceLevel = findViewById(R.id.tvPriceLevel);

        clCategories = findViewById(R.id.clCategories);
        ivFood = findViewById(R.id.ivFood);
        ivActivities = findViewById(R.id.ivActivities);
        ivAttractions = findViewById(R.id.ivAttractions);
        ivEvents = findViewById(R.id.ivEvents);

        clPrice = findViewById(R.id.clPrice);
        tvRightPopupTitle = findViewById(R.id.tvRightPopupTitle);
        tvMiles = findViewById(R.id.tvMiles);
        etDistance = findViewById(R.id.etDistance);
        btnPriceLevel1 = findViewById(R.id.btnPriceLevel1);
        btnPriceLevel2 = findViewById(R.id.btnPriceLevel2);
        btnPriceLevel3 = findViewById(R.id.btnPriceLevel3);
        btnPriceLevel4 = findViewById(R.id.btnPriceLevel4);

        btnMove = findViewById(R.id.btnMove);
        btnRiskyMove = findViewById(R.id.btnRiskyMove);
    }

    private void setupDesign() {
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        screenWidth = size.x;

        btnTime.getLayoutParams().width= screenWidth/4;
        btnPeople.getLayoutParams().width= screenWidth/4;
        btnDistance.getLayoutParams().width= screenWidth/4;
        btnPrice.getLayoutParams().width= screenWidth/4;

        clPrice.setVisibility(View.INVISIBLE);

        etDistance.addTextChangedListener(textWatcher);
        tvDistance.setVisibility(View.INVISIBLE);
        distance = milesToMeters(1);

        tvPriceLevel.setVisibility(View.INVISIBLE);
        priceLevel = 0;

        clCategories.post(new Runnable() {
            @Override
            public void run() {
                int constraintHeight = clCategories.getLayoutParams().height;
                ivFood.getLayoutParams().height = constraintHeight/4;
                ivActivities.getLayoutParams().height = constraintHeight/4;
                ivAttractions.getLayoutParams().height = constraintHeight/4;
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


        btnMove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getNearbyRestaurants();
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


    private void priceLevelSelected(int priceLevel) {
        int selectedColor = getResources().getColor(R.color.selected_blue);

        if (this.priceLevel == priceLevel) {
            this.priceLevel = 0;

            ivPrice.setVisibility(View.VISIBLE);
            tvPriceLevel.setVisibility(View.INVISIBLE);

            btnPriceLevel1.setBackgroundColor(getResources().getColor(R.color.white));
            btnPriceLevel1.setTextColor(getResources().getColor(R.color.black));
            btnPriceLevel2.setBackgroundColor(getResources().getColor(R.color.white));
            btnPriceLevel2.setTextColor(getResources().getColor(R.color.black));
            btnPriceLevel3.setBackgroundColor(getResources().getColor(R.color.white));
            btnPriceLevel3.setTextColor(getResources().getColor(R.color.black));
            btnPriceLevel4.setBackgroundColor(getResources().getColor(R.color.white));
            btnPriceLevel4.setTextColor(getResources().getColor(R.color.black));

        } else {
            this.priceLevel = priceLevel;

            tvPriceLevel.setVisibility(View.VISIBLE);
            ivPrice.setVisibility(View.INVISIBLE);

            if (priceLevel == 1) {
                btnPriceLevel1.setBackgroundColor(selectedColor);
                btnPriceLevel1.setTextColor(getResources().getColor(R.color.white));

                btnPriceLevel2.setBackgroundColor(getResources().getColor(R.color.white));
                btnPriceLevel2.setTextColor(getResources().getColor(R.color.black));
                btnPriceLevel3.setBackgroundColor(getResources().getColor(R.color.white));
                btnPriceLevel3.setTextColor(getResources().getColor(R.color.black));
                btnPriceLevel4.setBackgroundColor(getResources().getColor(R.color.white));
                btnPriceLevel4.setTextColor(getResources().getColor(R.color.black));

                tvPriceLevel.setText("$");

            } else if (priceLevel == 2) {
                btnPriceLevel2.setBackgroundColor(selectedColor);
                btnPriceLevel2.setTextColor(getResources().getColor(R.color.white));

                btnPriceLevel1.setBackgroundColor(getResources().getColor(R.color.white));
                btnPriceLevel1.setTextColor(getResources().getColor(R.color.black));
                btnPriceLevel3.setBackgroundColor(getResources().getColor(R.color.white));
                btnPriceLevel3.setTextColor(getResources().getColor(R.color.black));
                btnPriceLevel4.setBackgroundColor(getResources().getColor(R.color.white));
                btnPriceLevel4.setTextColor(getResources().getColor(R.color.black));

                tvPriceLevel.setText("$$");

            } else if (priceLevel == 3) {
                btnPriceLevel3.setBackgroundColor(selectedColor);
                btnPriceLevel3.setTextColor(getResources().getColor(R.color.white));

                btnPriceLevel1.setBackgroundColor(getResources().getColor(R.color.white));
                btnPriceLevel1.setTextColor(getResources().getColor(R.color.black));
                btnPriceLevel2.setBackgroundColor(getResources().getColor(R.color.white));
                btnPriceLevel2.setTextColor(getResources().getColor(R.color.black));
                btnPriceLevel4.setBackgroundColor(getResources().getColor(R.color.white));
                btnPriceLevel4.setTextColor(getResources().getColor(R.color.black));

                tvPriceLevel.setText("$$$");

            } else if (priceLevel == 4) {
                btnPriceLevel4.setBackgroundColor(selectedColor);
                btnPriceLevel4.setTextColor(getResources().getColor(R.color.white));

                btnPriceLevel1.setBackgroundColor(getResources().getColor(R.color.white));
                btnPriceLevel1.setTextColor(getResources().getColor(R.color.black));
                btnPriceLevel2.setBackgroundColor(getResources().getColor(R.color.white));
                btnPriceLevel2.setTextColor(getResources().getColor(R.color.black));
                btnPriceLevel3.setBackgroundColor(getResources().getColor(R.color.white));
                btnPriceLevel3.setTextColor(getResources().getColor(R.color.black));

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


    private void getNearbyRestaurants() {
        String apiUrl = API_BASE_URL + "/nearbysearch/json";

        String distanceString = etDistance.getText().toString().trim();
        distance = (distanceString.equals("")) ? milesToMeters(1) : milesToMeters(Float.valueOf(distanceString));


        RequestParams params = new RequestParams();
        params.put("location","47.6289467,-122.3428731");
        params.put("radius", (distance > 50000) ? 50000 : distance);
        params.put("type","restaurant");

        if (priceLevel > 0) {
            params.put("maxprice", priceLevel);
        }

        params.put("key", getString(R.string.api_key));

        client.get(apiUrl, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                JSONArray results;
                try {
                     results = response.getJSONArray("results");

                    for (int i = 0; i < results.length(); i++) {
                        Restaurant restaurant = Restaurant.fromJSON(results.getJSONObject(i));
                        Log.d(TAG, "got restaurant");

                        restaurantResults.add(restaurant);
                    }

                    Intent intent = new Intent(HomeActivity.this, MovesActivity.class);
                    startActivity(intent);

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


    private int milesToMeters(float miles) {
        return (int) (miles/0.000621317);
    }
}
