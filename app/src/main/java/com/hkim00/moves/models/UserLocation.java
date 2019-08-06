package com.hkim00.moves.models;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.android.gms.location.LocationResult;
import com.google.android.libraries.places.api.model.AddressComponent;
import com.google.android.libraries.places.api.model.Place;
import com.hkim00.moves.TripActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.List;

@Parcel
public class UserLocation {

    private static final String LOCATION = "location";
    private static final String TRIP_LOCATION = "tripLocation";
    private static final String NAME = "name";
    private static final String TRIP_NAME = "tripName";
    private static final String LAT = "lat";
    private static final String TRIP_LAT = "tripLat";
    private static final String LNG = "lng";
    private static final String TRIP_LNG = "tripLng";
    private static final String POSTAL_CODE = "postalCode";
    private static final String TRIP_POSTAL_CODE = "tripPostalCode";

    public String name, lat, lng, postalCode;

    public UserLocation() {}


    public static UserLocation getCurrentLocation(Context context) {
        boolean isTrip = (context instanceof TripActivity ) ? true : false;

        UserLocation location = new UserLocation();

        SharedPreferences sharedPreferences = context.getSharedPreferences((isTrip) ? TRIP_LOCATION : LOCATION, 0); //0 for private mode

        location.name = sharedPreferences.getString((isTrip) ? TRIP_NAME : NAME, null);
        location.lat = sharedPreferences.getString((isTrip) ? TRIP_LAT : LAT, null);
        location.lng = sharedPreferences.getString((isTrip) ? TRIP_LNG : LNG, null);
        location.postalCode = sharedPreferences.getString((isTrip) ? TRIP_POSTAL_CODE : POSTAL_CODE, null);

        return location;
    }


    public static UserLocation clearCurrentTripLocation(Context context) {
        UserLocation location = new UserLocation();

        SharedPreferences sharedPreferences = context.getSharedPreferences(TRIP_LOCATION, 0); //0 for private mode
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.remove(TRIP_NAME);
        editor.remove(TRIP_LAT);
        editor.remove(TRIP_LNG);
        editor.remove(TRIP_POSTAL_CODE);

        location.name = null;
        location.lat = null;
        location.lng = null;
        location.postalCode = null;

        editor.commit();

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

        SharedPreferences sharedPreferences = context.getSharedPreferences((isTrip) ? TRIP_LOCATION : LOCATION, 0); //0 for private mode
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString((isTrip) ? TRIP_NAME : NAME, location.name);
        editor.putString((isTrip) ? TRIP_LAT : LAT, location.lat);
        editor.putString((isTrip) ? TRIP_LNG : LNG, location.lng);
        editor.putString((isTrip) ? TRIP_POSTAL_CODE : POSTAL_CODE, location.postalCode);

        editor.commit();
    }
}
