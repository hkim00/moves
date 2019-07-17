package com.hkim00.moves;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class GoogleClient {

    public static final String API_BASE_URL = "https://maps.googleapis.com/maps/api/place";

    AsyncHttpClient client;


    public void getNearbyPlaces(AsyncHttpResponseHandler handler) {
        String apiUrl = API_BASE_URL + "nearbysearch/json?";

        String urlTest = "location=47.6289467,-122.3428731&type=restaurant&key=" + "AIzaSyDcrSpn40Zg3TjA732vsxZcvkIh5RCxW6Q";

        String url = apiUrl + urlTest;

        RequestParams params = new RequestParams();

        client.get(url, params, handler);
    }
}
