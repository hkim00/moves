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
    Button btnTime;
    Button btnPeople;
    Button btnPlace;
    Button btnPrice;

    ConstraintLayout clCategories;
    ImageView ivFood;
    ImageView ivActivities;
    ImageView ivAttractions;
    ImageView ivEvents;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Places.initialize(getApplicationContext(), getString(R.string.api_key));
        PlacesClient placesClient = Places.createClient(this);

        client = new AsyncHttpClient();

        getViewIds();

        setupDesign();

        getNearbyPlaces();
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
