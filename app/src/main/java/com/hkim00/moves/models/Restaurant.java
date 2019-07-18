// May table Restaurant model

package com.hkim00.moves.models;

import org.json.JSONException;
import org.json.JSONObject;

import com.parse.ParseClassName;
import com.parse.ParseObject;

@ParseClassName("Restaurant")
public class Restaurant extends ParseObject{

    public String name, id, cuisine;
    public Integer price_level;
    public Double lat, lng, rating;

    // deserialize the JSON
    public static Restaurant fromJSON(JSONObject jsonObject) throws JSONException {
        Restaurant restaurant = new Restaurant();

        restaurant.name = jsonObject.getString("name");
        restaurant.id = jsonObject.getString("id");
        restaurant.cuisine = jsonObject.getString("cuisine");

        restaurant.price_level = jsonObject.getInt("price_level");

        restaurant.lat = jsonObject.getDouble("lat");
        restaurant.lng = jsonObject.getDouble("lng");
        restaurant.rating = jsonObject.getDouble("id");

        return restaurant;
    }


}
