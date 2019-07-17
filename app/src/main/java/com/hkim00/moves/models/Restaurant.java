package com.hkim00.moves.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.JsonObject;

public class Restaurant {

    public String name, id;
    public Integer price_level;
    public Double lat, lng, rating;

    // deserialize the JSON
    public static Restaurant fromJSON(JSONObject jsonObject) throws JSONException {
        Restaurant restaurant = new Restaurant();

        //extract the values from JSON
        restaurant.name = jsonObject.getString("name");
        restaurant.id = jsonObject.getString("id");

        restaurant.price_level = jsonObject.getInt("price_level");

        JSONObject location = jsonObject.getJSONObject("geometry").getJSONObject("location");

        restaurant.lat = location.getDouble("lat");
        restaurant.lng = location.getDouble("lng");
        restaurant.rating = jsonObject.getDouble("rating");

        return restaurant;
    }
}
