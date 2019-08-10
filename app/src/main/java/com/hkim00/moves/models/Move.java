package com.hkim00.moves.models;

import android.content.Context;

import com.parse.ParseObject;
import com.parse.ParseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

@Parcel
public class Move {

    public String name, id, moveType, cuisine, genre;
    public Boolean didSave, didFavorite, didComplete;
    public int price_level;
    public Double lat, lng;
    public ParseObject parseObject;

    public boolean didCheckHTTPDetails = false;

    public Move() {

        didComplete = false;
        didSave = false;
        didFavorite = false;
    }

    @Override
    public boolean equals(Object obj) {
        return (((Move) obj).id.equals(this.id)) ? true : false;
    }

  
    public static Move fromParseObject(ParseObject parseObject) {
        Move move = new Move();

        move.name = parseObject.getString("name");
        move.id = parseObject.getString("placeId");
        move.moveType = parseObject.getString("moveType");

        move.lat = parseObject.getDouble("lat");
        move.lng = parseObject.getDouble("lng");

        move.genre = parseObject.getString("genre");
        move.price_level = parseObject.getInt("price_level");

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

        if (moveType.equals("food")) {
            this.price_level = (jsonObject.has("price_level")) ? jsonObject.getInt("price_level") : -1;
            JSONObject location = jsonObject.getJSONObject("geometry").getJSONObject("location");
            this.lat = location.getDouble("lat");
            this.lng = location.getDouble("lng");
        } else {
            this.lat = jsonObject.getJSONObject("_embedded").getJSONArray("venues").getJSONObject(0).getJSONObject("location").getDouble("latitude");
            this.lng = jsonObject.getJSONObject("_embedded").getJSONArray("venues").getJSONObject(0).getJSONObject("location").getDouble("longitude");
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

    public void saveToParse() {
        ParseObject currObj = new ParseObject("Move");
        currObj.put("name", this.name);
        currObj.put("placeId", this.id);
        currObj.put("moveType", this.moveType);
        currObj.put("user", ParseUser.getCurrentUser());
        currObj.put("didComplete", this.didComplete);
        currObj.put("didSave", this.didSave);
        currObj.put("didFavorite", this.didFavorite);
        currObj.put("count", 0);
        currObj.put("lat", this.lat);
        currObj.put("lng", this.lng);
        if (this.moveType.equals("food")){
            currObj.put("price_level", ((Restaurant) this).price_level);
        } else {
            currObj.put("genre", ((Event) this).genre);
        }

        currObj.saveInBackground(e -> {
            if (e == null) {
                this.parseObject = currObj;
            }
        });
    }
}
