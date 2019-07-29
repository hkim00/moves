package com.hkim00.moves.models;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.location.Location;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.LocationResult;
import com.google.android.libraries.places.api.model.AddressComponent;
import com.google.android.libraries.places.api.model.Place;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.List;

@Parcel
public class UserLocation {

    public String name;
    public String lat;
    public String lng;
    public String postalCode;

    public UserLocation() {}


    public static UserLocation getCurrentLocation(Context context) {
        UserLocation location = new UserLocation();

        SharedPreferences sharedPreferences = context.getSharedPreferences("location", 0); //0 for private mode

        String name = sharedPreferences.getString("name", "");
        String lat = sharedPreferences.getString("lat", "0.0");
        String lng = sharedPreferences.getString("lng", "0.0");
        String postalCode = sharedPreferences.getString("postalCode", "");

        location.name = name;
        location.lat = lat;
        location.lng = lng;
        location.postalCode = postalCode;

        return location;
    }


    public static UserLocation getCurrentTripLocation(Context context) {
        UserLocation location = new UserLocation();

        SharedPreferences sharedPreferences = context.getSharedPreferences("tripLocation", 0); //0 for private mode

        String name = sharedPreferences.getString("tripName", "");
        String lat = sharedPreferences.getString("tripLat", "0.0");
        String lng = sharedPreferences.getString("tripLng", "0.0");
        String postalCode = sharedPreferences.getString("tripPostalCode", "");

        location.name = name;
        location.lat = lat;
        location.lng = lng;
        location.postalCode = postalCode;

        return location;
    }


    public static UserLocation fromPlace(Context context, boolean isTrip, Place place) {
        UserLocation location = new UserLocation();

        location.name = place.getName();
        location.lat = String.valueOf(place.getLatLng().latitude);
        location.lng = String.valueOf(place.getLatLng().longitude);

        String postalCode = "";

        List<AddressComponent> addressComponents = place.getAddressComponents().asList();
        for (int i = 0; i < addressComponents.size(); i++) {
            AddressComponent component = addressComponents.get(i);
            if (component.getTypes().contains("postal_code")) {
                postalCode = component.getName();
            }
        }

        location.postalCode = postalCode;

        saveLocation(context, isTrip, location);

        return location;
    }


    public static UserLocation fromLocationResult(Context context, boolean isTrip, LocationResult locationResult) {
        UserLocation location = new UserLocation();

        location.name = "Current location";
        location.lat = String.valueOf(locationResult.getLastLocation().getLatitude());
        location.lng = String.valueOf(locationResult.getLastLocation().getLongitude());
        location.postalCode = "";

        saveLocation(context, isTrip, location);

        return location;
    }


    public static UserLocation addingPostalCodeFromJSON(Context context, boolean isTrip, UserLocation location, JSONObject response) {

        try {
            JSONArray results = response.getJSONArray("results");

            if (results.length() > 0) {
                String postalCode = "";
                boolean didFindPostal = false;

                for (int i = 0; i < results.length(); i++) {
                    if (results.getJSONObject(i).has("address_components")) {
                        JSONArray addressComponents = results.getJSONObject(i).getJSONArray("address_components");

                        for (int a = addressComponents.length() - 1; a < addressComponents.length(); a--) { //start from back because postal code is usually the last item
                            String types = addressComponents.getJSONObject(a).getJSONArray("types").toString();
                            if (types.contains("postal_code")) {
                                postalCode = addressComponents.getJSONObject(a).getString("long_name");
                                didFindPostal = true;
                                break;
                            }
                        }
                        if (didFindPostal) {
                            break;
                        }
                    }
                }

                location.postalCode = postalCode;

                saveLocation(context, isTrip, location);

                return location;
            } else {
                return location;
            }
        } catch (JSONException e) {
            Log.e(context.toString(), e.getMessage());
            e.printStackTrace();
            return location;
        }
    }


    private static void saveLocation(Context context, boolean isTrip, UserLocation location) {

        SharedPreferences sharedPreferences = context.getSharedPreferences((isTrip) ? "tripLocation" : "location", 0); //0 for private mode
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString((isTrip) ? "tripName" : "name", location.name);
        editor.putString((isTrip) ? "tripLat" : "lat", location.lat);
        editor.putString((isTrip) ? "tripLng" : "lng", location.lng);
        editor.putString((isTrip) ? "tripPostalCode" : "postalCode", location.postalCode);

        editor.commit();
    }
}
