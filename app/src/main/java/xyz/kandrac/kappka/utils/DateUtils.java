package xyz.kandrac.kappka.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by jan on 9.2.2017.
 */

public final class DateUtils {

    private DateUtils() {

    }

    public static final DateFormat TIME_FORMAT = SimpleDateFormat.getTimeInstance(DateFormat.SHORT, Locale.getDefault());
    public static final DateFormat DATE_FORMAT = SimpleDateFormat.getDateInstance(DateFormat.DATE_FIELD, Locale.getDefault());

    public static String getDateFormatted(long dateMilis) {
        return DATE_FORMAT.format(new Date(dateMilis));
    }

    public static String getCurrentDateFormatted() {
        return DATE_FORMAT.format(new Date(getCurrentDateMilis()));
    }

    public static long getCurrentDateMilis() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTimeInMillis();
    }

    public static long decrementDate(long time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        calendar.add(Calendar.DAY_OF_YEAR, -1);
        return calendar.getTimeInMillis();
    }

    public static long incrementDate(long time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        return calendar.getTimeInMillis();
    }
}
