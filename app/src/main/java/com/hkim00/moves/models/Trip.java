package com.hkim00.moves.models;

import com.parse.ParseObject;
import com.prolificinteractive.materialcalendarview.CalendarDay;

import org.parceler.Parcel;

import java.text.DateFormatSymbols;

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

    public static String getAPIDateFormat(CalendarDay beginDate, CalendarDay endDate) {
        String beginDateString = beginDate.getYear() + "-" +
                ((beginDate.getMonth() < 10) ? "0" + beginDate.getMonth() : beginDate.getMonth())
                + "-" +
                ((beginDate.getDay() < 10) ? "0" + beginDate.getDay() : beginDate.getDay())
                + "T00:00:00,";
        String endDateString = endDate.getYear() + "-" +
                ((endDate.getMonth() < 10) ? "0" + endDate.getMonth() : endDate.getMonth())
                + "-" +
                ((endDate.getDay() < 10) ? "0" + endDate.getDay() : endDate.getDay())
                + "T00:00:00";

        return beginDateString + endDateString;
    }


    public static String getDateRangeString(CalendarDay beginDate, CalendarDay endDate) {
        String[] months = new DateFormatSymbols().getShortMonths();
        return months[beginDate.getMonth() - 1] + " " + beginDate.getDay() + " - " + months[endDate.getMonth() - 1] + " " + endDate.getDay();
    }
}
