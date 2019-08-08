package com.hkim00.moves.util;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import static com.hkim00.moves.util.DateTimeFormatters.formatDate;
import static com.hkim00.moves.util.DateTimeFormatters.formatTime;
import static java.security.AccessController.getContext;

public class JSONResponseHelper {
    public static java.lang.String TAG = getContext().toString();

    // gets and formats start time of an event
    public static String getStartTime (JSONObject response){
        String formattedStart = "N/A";
        try {
            String rawStart = response.getJSONObject("dates").getJSONObject("start").getString("localTime");
            formattedStart = formatTime(rawStart);
        } catch (JSONException e)  {
            Log.e(TAG, "Error getting event start time!" + " Error msg from JSON: " + e.getMessage());
            e.printStackTrace();
        }
        return formattedStart;
    }

    // gets and formats price range of event tickets,
    public static String getPriceRange (JSONObject response) {
        String formattedPriceRange = "N/A";
        try {
            Double priceMin = response.getJSONArray("priceRanges").getJSONObject(0).getDouble("min");
            Double priceMax = response.getJSONArray("priceRanges").getJSONObject(0).getDouble("max");
            formattedPriceRange = "$" + String.format("%.2f", priceMin) + " ~ " + "$" + String.format("%.2f", priceMax);
        } catch (JSONException e)  {
            Log.i(TAG, e.getMessage());
            return formattedPriceRange;
        }
        return formattedPriceRange;
    }

    public static String getStartDate(JSONObject response) {
        String formattedStart = "N/A";
        try {
            String rawStart = response.getJSONObject("dates").getJSONObject("start").getString("localDate");
            formattedStart = formatDate(rawStart);
        } catch (JSONException e)  {
            Log.e(TAG, "Error getting event start time!" + " Error msg from JSON: " + e.getMessage());
            e.printStackTrace();
        }
        return formattedStart;
    }
}
