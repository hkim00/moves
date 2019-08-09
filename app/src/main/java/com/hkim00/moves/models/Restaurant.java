package com.hkim00.moves.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

@Parcel
public class Restaurant extends Move {

    public Integer price_level;
    public Double rating;
    public String id;
    public List<String> photoReferences;

    public Restaurant() {
        super();
        super.moveType = "food";
    }

    @Override
    public void fromJSON(JSONObject jsonObject, String moveType) throws JSONException {
        super.didCheckHTTPDetails = true;
        super.fromJSON(jsonObject, moveType);

        this.price_level = (jsonObject.has("price_level")) ? jsonObject.getInt("price_level") : -1;

        JSONObject location = jsonObject.getJSONObject("geometry").getJSONObject("location");

        this.lat = location.getDouble("lat");
        this.lng = location.getDouble("lng");

        this.rating = (jsonObject.has("rating")) ? jsonObject.getDouble("rating") : -1;

        photoReferences = new ArrayList<>();
        if (jsonObject.has("photos")) {
            JSONArray photosJSONArray = jsonObject.getJSONArray("photos");

            for (int i = 0; i < photosJSONArray.length(); i++) {
                String photoReference = photosJSONArray.getJSONObject(i).getString("photo_reference");
                photoReferences.add(photoReference);
            }
        }
    }
}
