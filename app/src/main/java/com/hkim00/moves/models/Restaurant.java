package com.hkim00.moves.models;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

@Parcel
public class Restaurant extends Move {

    public Integer price_level;
    public Double rating;
    public String id;

    public Restaurant() {
        super();
        super.moveType = "food";
    }

    @Override
    public void fromJSON(JSONObject jsonObject, String moveType) throws JSONException {
        super.fromJSON(jsonObject, moveType);

        this.price_level = (jsonObject.has("price_level")) ? jsonObject.getInt("price_level") : -1;

        JSONObject location = jsonObject.getJSONObject("geometry").getJSONObject("location");

        this.lat = location.getDouble("lat");
        this.lng = location.getDouble("lng");

        this.rating = (jsonObject.has("rating")) ? jsonObject.getDouble("rating") : -1;
    }
}
