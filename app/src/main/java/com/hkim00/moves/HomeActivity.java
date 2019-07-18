package com.hkim00.moves;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

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

    AsyncHttpClient client;

    String time;
    String numberOfPeople;
    String radius;
    String price;

    TextView tvLocation;
    Button btnTime;
    Button btnPeople;
    Button btnPlace;
    Button btnPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // TODO: Francesco - the reason why gitguardian was giving us notifications is that you'd hardcoded the api key in this file as a string...
        Places.initialize(getApplicationContext(), getString(R.string.api_key));
        PlacesClient placesClient = Places.createClient(this);

        tvLocation = findViewById(R.id.tvLocation);
        btnTime = findViewById(R.id.btnTime);
        btnPeople = findViewById(R.id.btnPeople);
        btnPlace = findViewById(R.id.btnPlace);
        btnPrice = findViewById(R.id.btnPrice);

        client = new AsyncHttpClient();

        getNearbyPlaces();
    }

    private void getNearbyPlaces() {
        String apiUrl = API_BASE_URL + "/nearbysearch/json";

        String urlTest = "location=47.6289467,-122.3428731&type=restaurant&key=" + "AIzaSyDcrSpn40Zg3TjA732vsxZcvkIh5RCxW6Q";

        RequestParams params = new RequestParams();
        params.put("location","47.6289467,-122.3428731");
        params.put("radius", "1500"); //1500 meters
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

}
