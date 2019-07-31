package com.hkim00.moves.models;

import com.parse.ParseObject;
import com.prolificinteractive.materialcalendarview.CalendarDay;

import org.parceler.Parcel;

@Parcel
public class Trip {

    public String name;
    public UserLocation location;
    public ParseObject parseObject;
    public CalendarDay startDay, endDay;

    public Trip() {}


    public static Trip fromParseObject(ParseObject parseObject) {
        Trip trip = new Trip();
        trip.location = new UserLocation();

        trip.parseObject = parseObject;

        trip.name = parseObject.getString("name");

        trip.location.name = parseObject.getString("locationName");
        trip.location.lat = parseObject.getString("lat");
        trip.location.lng = parseObject.getString("lng");
        trip.location.postalCode = parseObject.getString("postalCode");

        trip.startDay = CalendarDay.from(parseObject.getInt("startYear"), parseObject.getInt("startMonth"), parseObject.getInt("startDay"));
        trip.endDay = CalendarDay.from(parseObject.getInt("endYear"), parseObject.getInt("endMonth"), parseObject.getInt("endDay"));

        return trip;
    }
}
