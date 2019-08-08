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

    public String name, id, moveType, cuisine;
    public Boolean didSave, didFavorite, didComplete;

    public Double lat, lng;

    public Move() {}

    @Override
    public boolean equals(Object obj) {
        return (((Move) obj).id.equals(this.id)) ? true : false;
    }
  
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

    public static void arrayFromJSONArray(List<Move> moveList, JSONArray objects, String moveType) {
        for (int i = 0; i < objects.length(); i++) {
            try {
                Move move = new Move();
                move.fromJSON(objects.getJSONObject(i), moveType);
                moveList.add(move);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void fromJSON(JSONObject jsonObject, String moveType) throws JSONException {
        this.name = jsonObject.getString("name");
        this.moveType = moveType;
        this.id = jsonObject.getString((moveType.equals("food")) ? "place_id" : "id");
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
