package com.hkim00.moves.models;

import com.parse.ParseObject;

import org.parceler.Parcel;

@Parcel
public class Trip {

    public String name, dateRange;
    public UserLocation location;
    public ParseObject parseObject;

    public Trip() {}


    public static Trip fromParseObject(ParseObject parseObject) {
        Trip trip = new Trip();
        trip.location = new UserLocation();

        trip.parseObject = parseObject;

        trip.name = parseObject.getString("name");
        trip.dateRange = parseObject.getString("dateRange");

        trip.location.name = parseObject.getString("locationName");
        trip.location.lat = parseObject.getString("lat");
        trip.location.lng = parseObject.getString("lng");
        trip.location.postalCode = parseObject.getString("postalCode");

        return trip;
    }
}
