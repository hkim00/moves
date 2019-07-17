package com.hkim00.moves;

import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.widget.Button;
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
    int radius;
    int priceLevel;

    TextView tvLocation;
    Button btnTime, btnPeople, btnPlace, btnPrice;

    ConstraintLayout clCategories;
    ImageView ivFood, ivActivities, ivAttractions, ivEvents;

    ConstraintLayout clPrice;
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
        btnPlace = findViewById(R.id.btnPlace);
        btnPrice = findViewById(R.id.btnPrice);

        clCategories = findViewById(R.id.clCategories);
        ivFood = findViewById(R.id.ivFood);
        ivActivities = findViewById(R.id.ivActivities);
        ivAttractions = findViewById(R.id.ivAttractions);
        ivEvents = findViewById(R.id.ivEvents);

        clPrice = findViewById(R.id.clPrice);
        btnPriceLevel1 = findViewById(R.id.btnPriceLevel1);
        btnPriceLevel2 = findViewById(R.id.btnPriceLevel2);
        btnPriceLevel3 = findViewById(R.id.btnPriceLevel3);
        btnPriceLevel4 = findViewById(R.id.btnPriceLevel4);
    }

    private void setupButtons() {
        btnPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clPrice.setVisibility( (clPrice.getVisibility() == View.INVISIBLE) ? View.VISIBLE : View.INVISIBLE);
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

    private void priceLevelSelected(int priceLevel) {
        this.priceLevel = priceLevel;

        if (priceLevel == 1) {
            btnPriceLevel1.setBackgroundColor(getResources().getColor(R.color.navy));
            btnPriceLevel1.setTextColor(getResources().getColor(R.color.white));

            btnPriceLevel2.setBackgroundColor(getResources().getColor(R.color.white));
            btnPriceLevel2.setTextColor(getResources().getColor(R.color.black));
            btnPriceLevel3.setBackgroundColor(getResources().getColor(R.color.white));
            btnPriceLevel3.setTextColor(getResources().getColor(R.color.black));
            btnPriceLevel4.setBackgroundColor(getResources().getColor(R.color.white));
            btnPriceLevel4.setTextColor(getResources().getColor(R.color.black));

        } else if (priceLevel == 2) {
            btnPriceLevel2.setBackgroundColor(getResources().getColor(R.color.navy));
            btnPriceLevel2.setTextColor(getResources().getColor(R.color.white));

            btnPriceLevel1.setBackgroundColor(getResources().getColor(R.color.white));
            btnPriceLevel1.setTextColor(getResources().getColor(R.color.black));
            btnPriceLevel3.setBackgroundColor(getResources().getColor(R.color.white));
            btnPriceLevel3.setTextColor(getResources().getColor(R.color.black));
            btnPriceLevel4.setBackgroundColor(getResources().getColor(R.color.white));
            btnPriceLevel4.setTextColor(getResources().getColor(R.color.black));

        } else if (priceLevel == 3) {
            btnPriceLevel3.setBackgroundColor(getResources().getColor(R.color.navy));
            btnPriceLevel3.setTextColor(getResources().getColor(R.color.white));

            btnPriceLevel1.setBackgroundColor(getResources().getColor(R.color.white));
            btnPriceLevel1.setTextColor(getResources().getColor(R.color.black));
            btnPriceLevel2.setBackgroundColor(getResources().getColor(R.color.white));
            btnPriceLevel2.setTextColor(getResources().getColor(R.color.black));
            btnPriceLevel4.setBackgroundColor(getResources().getColor(R.color.white));
            btnPriceLevel4.setTextColor(getResources().getColor(R.color.black));

        } else if (priceLevel == 4) {
            btnPriceLevel4.setBackgroundColor(getResources().getColor(R.color.navy));
            btnPriceLevel4.setTextColor(getResources().getColor(R.color.white));

            btnPriceLevel1.setBackgroundColor(getResources().getColor(R.color.white));
            btnPriceLevel1.setTextColor(getResources().getColor(R.color.black));
            btnPriceLevel2.setBackgroundColor(getResources().getColor(R.color.white));
            btnPriceLevel2.setTextColor(getResources().getColor(R.color.black));
            btnPriceLevel3.setBackgroundColor(getResources().getColor(R.color.white));
            btnPriceLevel3.setTextColor(getResources().getColor(R.color.black));
        }
    }


    private void setupDesign() {
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        screenWidth = size.x;

        btnTime.getLayoutParams().width= screenWidth/4;
        btnPeople.getLayoutParams().width= screenWidth/4;
        btnPlace.getLayoutParams().width= screenWidth/4;
        btnPrice.getLayoutParams().width= screenWidth/4;

        clPrice.setVisibility(View.INVISIBLE);

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


    private void getNearbyPlaces() {
        String apiUrl = API_BASE_URL + "/nearbysearch/json";

        String urlTest = "location=47.6289467,-122.3428731&type=restaurant&key=" + "AIzaSyDcrSpn40Zg3TjA732vsxZcvkIh5RCxW6Q";

        RequestParams params = new RequestParams();
        params.put("location","47.6289467,-122.3428731");
        params.put("radius", milesToMeters(1)); //1500 meters
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
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
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

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                super.onSuccess(statusCode, headers, responseString);
            }
        });
    }


    private int milesToMeters(float miles) {
        return (int) (miles/0.000621317);
    }
}
