package com.cems.Utils;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ParseUtil {
    public static int tryParseInt(String value, int defaultValue) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    public static boolean tryParseBoolean(String value, boolean defaultValue) {
        try {
            return Boolean.parseBoolean(value);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    public static Timestamp tryParseDateTime(String date, String time) {
        try {
            String dateTimeStr = date + " " + time;
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            Date parseDate = dateFormat.parse(dateTimeStr);
            return new Timestamp(parseDate.getTime());
        } catch (Exception e) {
            return null;
        }
    }
}
