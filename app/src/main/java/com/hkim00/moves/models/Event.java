package com.hkim00.moves.models;

import com.parse.ParseObject;

import org.json.JSONArray;
import org.parceler.Parcel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.hkim00.moves.util.JSONResponseHelper.getPriceRange;
import static com.hkim00.moves.util.JSONResponseHelper.getStartTime;

@Parcel
public class Event extends Move {
    public int count;
    public String name, id, startTime, priceRange, genre;
    public Boolean didSave, didFavorite, didComplete;

    public Event() {
        super();
        super.moveType = "event";
    }

    public void fromJSON(JSONObject jsonObject, String moveType) throws JSONException {
        super.fromJSON(jsonObject, moveType);
        this.genre = jsonObject.getJSONArray("classifications").getJSONObject(0).getJSONObject("genre").getString("name");
    }

    public void fromDetailsJSON(JSONObject jsonObject) throws JSONException {
        super.name = jsonObject.getString("name");
        super.id = jsonObject.getString("id");
        this.startTime = getStartTime(jsonObject);
        this.priceRange = getPriceRange(jsonObject);
        this.genre = jsonObject.getJSONArray("classifications").getJSONObject(0).getJSONObject("genre").getString("name");
        this.lat = jsonObject.getJSONObject("_embedded").getJSONArray("venues").getJSONObject(0).getJSONObject("location").getDouble("latitude");
        this.lng = jsonObject.getJSONObject("_embedded").getJSONArray("venues").getJSONObject(0).getJSONObject("location").getDouble("longitude");
    }
}
