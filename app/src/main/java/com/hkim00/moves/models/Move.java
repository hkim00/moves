package com.hkim00.moves.models;

import android.content.Context;

import com.parse.ParseObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

@Parcel
public class Move {

    public String name, id, moveType;
    public Boolean didSave, didFavorite, didComplete;

    public String text;
    public Integer price_level;
    public Double lat, lng, rating;

    public Move() {}

    public static Move fromParseObject(ParseObject parseObject) {
        Move move = new Move();

        move.name = parseObject.getString("name");
        move.id = parseObject.getString("placeId");
        move.moveType = parseObject.getString("moveType");

        move.didSave = parseObject.getBoolean("didSave");
        move.didFavorite = parseObject.getBoolean("didFavorite");
        move.didComplete = parseObject.getBoolean("didComplete");

        return move;
    }

    public static List<Move> arrayFromJSONArray(JSONArray objects, String moveType) {
        List<Move> moves = new ArrayList<>();

        for (int i = 0; i < objects.length(); i++) {
            Move move;
            try {
                move = Move.fromJSON(objects.getJSONObject(i), moveType);
                moves.add(move);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return moves;
    }

    public static Move fromJSON(JSONObject jsonObject, String moveType) throws JSONException {
        Move move = new Move();
        move.name = jsonObject.getString("name");
        move.moveType = moveType;

        if (moveType.equals("food")) {
            move.id = jsonObject.getString("place_id");
            move.price_level = (jsonObject.has("price_level")) ? jsonObject.getInt("price_level") : -1;

            JSONObject location = jsonObject.getJSONObject("geometry").getJSONObject("location");

            move.lat = location.getDouble("lat");
            move.lng = location.getDouble("lng");

            move.rating = (jsonObject.has("rating")) ? jsonObject.getDouble("rating") : -1;

            return move;
        }

        else {
            move.id = jsonObject.getString("id");

            return move;
        }
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
