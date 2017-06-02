package net.scriptgate.softdev.android.util;

import java.util.ArrayList;
import java.util.List;

import java8.util.stream.Collectors;

import static java8.util.stream.StreamSupport.stream;

public class TimeUtil {

    public static final long MINUTE = 60;
    public static final long HOUR = 60 * MINUTE;
    public static final long DAY = 24 * HOUR;
    public static final long MONTH = 730 * HOUR;
    public static final long YEAR = 12 * MONTH;

    public static String secondsToFullTimestamp(double seconds) {
        int years = toYears(seconds);
        int months = toMonths(seconds);
        int days = toDays(seconds);
        int hours = toHours(seconds);
        return formatPlural(years, "year") + ", " +
                formatPlural(months, "month") + ", " +
                formatPlural(days, "day") + ", " +
                formatPlural(hours, "hour");
    }

    public static String secondsToTimestamp(double seconds) {
        int years = toYears(seconds);
        int months = toMonths(seconds);
        int days = toDays(seconds);
        int hours = toHours(seconds);

        List<String> timestamp = new ArrayList<>();

        if(years != 0) {
            timestamp.add(formatPlural(years, "year"));
        }
        if(months != 0) {
            timestamp.add(formatPlural(months, "month"));
        }
        if(days != 0) {
            timestamp.add(formatPlural(days, "day"));
        }
        if(hours != 0) {
            timestamp.add(formatPlural(hours,"hour"));
        }
        return timestamp.isEmpty() ? "0 hours": stream(timestamp).collect(Collectors.joining(", "));
    }

    private static String formatPlural(int x, String type) {
        return x + " " + type + (x == 1 ? "" : "s");
    }

    private static int toHours(double nanoSeconds) {
        double dayRemainder = ((nanoSeconds % YEAR) % MONTH) % DAY;
        return (int) Math.floor(dayRemainder / HOUR) % 24;
    }

    private static int toDays(double nanoSeconds) {
        double monthRemainder = (nanoSeconds % YEAR) % MONTH;
        return (int) Math.floor(monthRemainder / DAY);
    }

    private static int toMonths(double nanoSeconds) {
        double yearRemainder = nanoSeconds % YEAR;
        return (int) (yearRemainder / MONTH);
    }

    private static int toYears(double nanoSeconds) {
        return (int) (nanoSeconds / YEAR);
    }
}
