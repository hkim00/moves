// May table Restaurant model

package com.hkim00.moves.models;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import com.parse.ParseObject;

import java.util.ArrayList;
import java.util.List;

@Parcel
public class Restaurant implements Move {

    public String name, id, text;
    public Integer price_level;
    public Double lat, lng, rating;
    public Boolean didSave, didFavorite, didComplete;

    public Restaurant() {}

    @Override
    public int getMoveType() {
        return Move.RESTAURANT;
    }

    @Override
    public String getName() { return name; }

    @Override
    public String getId() { return id; }

    @Override
    public Boolean getDidSave() {return didSave; }

    @Override
    public Boolean getDidFavorite() {return didFavorite; }

    @Override
    public Boolean getDidComplete() {return didComplete; }

    @Override
    public boolean equals(Object obj) {
        return (((Move) obj).getId().equals(this.id)) ? true : false;
    }

    public static List<Move> arrayFromParseObjects(List<ParseObject> objects) {
        List<Move> restaurants = new ArrayList<>();

        for (int i = 0; i < objects.size(); i++) {
                Restaurant restaurant = Restaurant.fromParseObject(objects.get(i));
                restaurants.add(restaurant);
        }

        return restaurants;
    }

    public static List<Restaurant> arrayFromJSONArray(JSONArray objects) {
        List<Restaurant> restaurants = new ArrayList<>();

        for (int i = 0; i < objects.length(); i++) {
            Restaurant restaurant = new Restaurant();
            try {
                restaurant = Restaurant.fromJSON(objects.getJSONObject(i));
                restaurants.add(restaurant);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return restaurants;
    }


    public static Restaurant fromJSON(JSONObject jsonObject) throws JSONException {
        Restaurant restaurant = new Restaurant();

        restaurant.name = jsonObject.getString("name");
        restaurant.id = jsonObject.getString("place_id");

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
        restaurant.id = parseObject.getString("placeId");

        restaurant.didSave = parseObject.getBoolean("didSave");
        restaurant.didFavorite = parseObject.getBoolean("didFavorite");
        restaurant.didComplete = parseObject.getBoolean("didComplete");

        restaurant.lat = null;

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
