package com.hkim00.moves.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateTimeFormatters {
    public static String formatTime (String raw) {
        String formatted = "";
        DateFormat input = new SimpleDateFormat("HH:mm:ss");
        DateFormat output = new SimpleDateFormat("hh:mm a");
        try {
            Date date = input.parse(raw);
            formatted = output.format(date);
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        return formatted;
    }
}
