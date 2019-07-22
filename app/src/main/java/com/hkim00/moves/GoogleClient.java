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

    public static final String API_BASE_URL = "https://maps.googleapis.com/maps/api/place";

    AsyncHttpClient client;


    public void getNearbyPlaces(AsyncHttpResponseHandler handler) {
        String apiUrl = API_BASE_URL + "nearbysearch/json?";

        String urlTest = "location=47.6289467,-122.3428731&type=restaurant&key=" + "AIzaSyDcrSpn40Zg3TjA732vsxZcvkIh5RCxW6Q";

        String url = apiUrl + urlTest;

        RequestParams params = new RequestParams();

        client.get(url, params, handler);
    }


    //save for later
//    private static String getPostalCode(String lat, String lng) {
//        String apiUrl = "https://maps.googleapis.com/maps/api/geocode/json";
//
//        RequestParams params = new RequestParams();
//        params.put("latlng", lat + "," + lng);
//        params.put("apikey", Resources.getSystem().getString(R.string.api_key));
//
//        HomeActivity.client.get(apiUrl, params, new JsonHttpResponseHandler() {
//            @Override
//            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
//                super.onSuccess(statusCode, headers, response);
//            }
//
//            @Override
//            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
//                super.onSuccess(statusCode, headers, response);
//            }
//
//            @Override
//            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
//                super.onFailure(statusCode, headers, throwable, errorResponse);
//            }
//
//            @Override
//            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
//                super.onFailure(statusCode, headers, throwable, errorResponse);
//            }
//
//            @Override
//            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
//                super.onFailure(statusCode, headers, responseString, throwable);
//            }
//
//            @Override
//            public void onSuccess(int statusCode, Header[] headers, String responseString) {
//                super.onSuccess(statusCode, headers, responseString);
//            }
//        });
//
//        return "";
//    }
}
