package com.hkim00.moves.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
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

    public static String formatDate(String raw) {
        LocalDate localDate = null;
        String formattedDate = "";

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            localDate = LocalDate.parse(raw);
             formattedDate = localDate.format(DateTimeFormatter.ofPattern("MMM dd"));
        }

        return formattedDate;
    }
}
