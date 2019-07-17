package com.hkim00.moves;

import android.graphics.Point;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class HomeActivity extends AppCompatActivity {

    public static final String API_BASE_URL = "https://maps.googleapis.com/maps/api/place";
    private static int screenWidth;

    AsyncHttpClient client;

    String time;
    String numberOfPeople;
    int distance;
    int priceLevel;

    TextView tvLocation, tvDistance, tvPriceLevel;
    ImageView ivDistance, ivPrice;
    Button btnTime, btnPeople, btnDistance, btnPrice;

    ConstraintLayout clCategories;
    ImageView ivFood, ivActivities, ivAttractions, ivEvents;

    ConstraintLayout clPrice;
    TextView tvRightPopupTitle, tvMiles;
    EditText etDistance;
    Button btnPriceLevel1, btnPriceLevel2, btnPriceLevel3, btnPriceLevel4;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Places.initialize(getApplicationContext(), getString(R.string.api_key));
        PlacesClient placesClient = Places.createClient(this);

        client = new AsyncHttpClient();

        getViewIds();

        setupDesign();

        setupButtons();

        //getNearbyPlaces();
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
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

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
        public void afterTextChanged(Editable s) {

        }
    };


    private void getNearbyPlaces() {
        String apiUrl = API_BASE_URL + "/nearbysearch/json";

        String urlTest = "location=47.6289467,-122.3428731&type=restaurant&key=" + "AIzaSyDcrSpn40Zg3TjA732vsxZcvkIh5RCxW6Q";

        String distanceString = etDistance.getText().toString().trim();
        distance = (distanceString == "") ? milesToMeters(1) : milesToMeters(Float.valueOf(distanceString));


        RequestParams params = new RequestParams();
        params.put("location","47.6289467,-122.3428731");
        params.put("radius", distance);
        params.put("type","restaurant");

        if (priceLevel > 0) {
            params.put("maxprice", priceLevel);
        }

        params.put("key", getString(R.string.api_key));

        client.get(apiUrl, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
            }


            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });
    }


    private int milesToMeters(float miles) {
        return (int) (miles/0.000621317);
    }
}
