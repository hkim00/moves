// May table Restaurant model

package com.hkim00.moves.models;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import com.parse.ParseObject;

@Parcel
public class Restaurant extends Move {

    public String name, id;
    public Integer price_level;
    public Double lat, lng, rating;

    public Restaurant() {}

    public static Restaurant fromJSON(JSONObject jsonObject) throws JSONException {
        Restaurant restaurant = new Restaurant();

        restaurant.name = jsonObject.getString("name");
        restaurant.id = jsonObject.getString("id");

        restaurant.price_level = (jsonObject.has("price_level")) ? jsonObject.getInt("price_level") : -1;

        JSONObject location = jsonObject.getJSONObject("geometry").getJSONObject("location");

        restaurant.lat = location.getDouble("lat");
        restaurant.lng = location.getDouble("lng");

        restaurant.rating = (jsonObject.has("rating")) ? jsonObject.getDouble("rating") : -1;

        return restaurant;
    }

    public static Restaurant fromParseObject(ParseObject parseObject) {
        Restaurant restaurant = new Restaurant();

        restaurant.name = parseObject.getString("name");
        restaurant.id = parseObject.getString("id");

        restaurant.price_level = (Integer) parseObject.getNumber("price_level");

        restaurant.lat = (Double) parseObject.getNumber("lat");
        restaurant.lng = (Double) parseObject.getNumber("lng");
        restaurant.rating = (Double) parseObject.getNumber("rating");

        return restaurant;
    }

    public String distanceFromLocation(Context context) {
        UserLocation location = UserLocation.getCurrentLocation(context);

        double theta = Double.valueOf(location.lng) - lng;
        double dist = Math.sin(Math.toRadians(Double.valueOf(location.lat))) * Math.sin(Math.toRadians(lat)) + Math.cos(Math.toRadians(Double.valueOf(location.lat))) * Math.cos(Math.toRadians(lat)) * Math.cos(Math.toRadians(theta));
        dist = Math.acos(dist);
        dist = Math.toDegrees(dist);
        dist = dist * 60 * 1.1515;

        dist = Math.round(dist * 10) / 10.0;

        return String.valueOf(dist);
    }
}
