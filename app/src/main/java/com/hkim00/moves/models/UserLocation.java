package com.hkim00.moves.models;

import android.content.SharedPreferences;
import android.content.res.Resources;
import android.location.Location;
import android.preference.PreferenceManager;

import com.google.android.gms.location.LocationResult;
import com.google.android.libraries.places.api.model.AddressComponent;
import com.google.android.libraries.places.api.model.Place;
import org.parceler.Parcel;

import java.util.List;

@Parcel
public class UserLocation {

    public String name;
    public double lat;
    public double lng;
    public String postalCode;

    public UserLocation() {}

    public static UserLocation fromPlace(Place place) {
        UserLocation location = new UserLocation();

        location.lat = place.getLatLng().latitude;
        location.lng = place.getLatLng().longitude;

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

        return location;
    }


    public static UserLocation fromLocationResult(LocationResult locationResult) {
        UserLocation location = new UserLocation();

        location.name = "Current location";
        location.lat = locationResult.getLastLocation().getLatitude();
        location.lng = locationResult.getLastLocation().getLongitude();

        location.postalCode = "";

        return location;
    }
}
