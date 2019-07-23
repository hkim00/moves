package com.hkim00.moves;

import android.content.res.Resources;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class GoogleClient {

    public static final String API_BASE_URL = "https://maps.googleapis.com/maps/api";

    AsyncHttpClient client;


    public GoogleClient() {
        client = new AsyncHttpClient();
    }


    public void getNearbyPlaces(RequestParams requestParams, AsyncHttpResponseHandler handler) {
        String apiUrl = API_BASE_URL + "/place/nearbysearch/json?";

        String urlTest = "location=47.6289467,-122.3428731&type=restaurant&key=" + "AIzaSyDcrSpn40Zg3TjA732vsxZcvkIh5RCxW6Q";

        String url = apiUrl + urlTest;

        client.get(url, requestParams, handler);
    }


    public void getPostalCode(String lat, String lng, AsyncHttpResponseHandler handler) {
        String apiUrl = "https://maps.googleapis.com/maps/api/geocode/json";

        RequestParams params = new RequestParams();
        params.put("latlng", lat + "," + lng);
        params.put("apikey", Resources.getSystem().getString(R.string.api_key));

        client.get(apiUrl, params, new JsonHttpResponseHandler());
    }
}
