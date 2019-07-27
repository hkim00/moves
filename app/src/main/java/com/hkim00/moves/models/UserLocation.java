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


    public static UserLocation fromPlace(Context context, Place place) {
        UserLocation location = new UserLocation();

        location.lat = String.valueOf(place.getLatLng().latitude);
        location.lng = String.valueOf(place.getLatLng().longitude);

        String streetNumber = "";
        String route = "";

        List<AddressComponent> addressComponents = place.getAddressComponents().asList();
        for (int i = 0; i < addressComponents.size(); i++) {
            AddressComponent component = addressComponents.get(i);

            if (component.getTypes().contains("street_number")) {
                streetNumber = component.getName();
            } else if (component.getTypes().contains("route")) {
                route = component.getName();
            } else if (component.getTypes().contains("postal_code")) {
                location.postalCode = component.getName();
            }
        }

        location.name = streetNumber + " " + route;

        saveLocation(context, location);

        return location;
    }


    public static UserLocation fromLocationResult(Context context, LocationResult locationResult) {
        UserLocation location = new UserLocation();

        location.name = "Current location";
        location.lat = String.valueOf(locationResult.getLastLocation().getLatitude());
        location.lng = String.valueOf(locationResult.getLastLocation().getLongitude());
        location.postalCode = "";

        saveLocation(context, location);

        return location;
    }

    public static UserLocation addingPostalCodeFromJSON(Context context, UserLocation location, JSONObject response) {

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

                saveLocation(context, location);

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


    private static void saveLocation(Context context, UserLocation location) {

        SharedPreferences sharedPreferences = context.getSharedPreferences("location", 0); //0 for private mode
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString("name", location.name);
        editor.putString("lat", location.lat);
        editor.putString("lng", location.lng);
        editor.putString("postalCode", location.postalCode);

        editor.commit();
    }
}
