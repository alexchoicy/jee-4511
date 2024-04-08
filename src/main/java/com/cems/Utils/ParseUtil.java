package com.cems.Utils;

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
}
