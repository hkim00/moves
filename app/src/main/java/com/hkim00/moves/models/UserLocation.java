package com.hkim00.moves.models;

import android.content.res.Resources;
import android.location.Location;

import com.google.android.gms.location.LocationResult;
import com.google.android.libraries.places.api.model.AddressComponent;
import com.google.android.libraries.places.api.model.Place;
import org.parceler.Parcel;

import java.util.List;

@Parcel
public class UserLocation {

    public double lat;
    public double lng;
    public String postalCode;

    public UserLocation() {}

    public static UserLocation fromPlace(Place place) {
        UserLocation location = new UserLocation();

        location.lat = place.getLatLng().latitude;
        location.lng = place.getLatLng().longitude;

        List<AddressComponent> addressComponents = place.getAddressComponents().asList();
        for (int i = 0; i < addressComponents.size(); i++) {
            AddressComponent component = addressComponents.get(i);

            if (component.getTypes().contains("postal_code")) {
                location.postalCode = component.getName();
            }
        }

        return location;
    }


    public static UserLocation fromLocationResult(LocationResult locationResult) {
        UserLocation location = new UserLocation();

        location.lat = locationResult.getLastLocation().getLatitude();
        location.lng = locationResult.getLastLocation().getLongitude();

        location.postalCode = "";

        return location;
    }
}
