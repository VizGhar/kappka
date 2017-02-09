package xyz.kandrac.kappka.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by jan on 9.2.2017.
 */

public final class DateUtils {

    private DateUtils() {

    }

    public static final DateFormat TIME_FORMAT = SimpleDateFormat.getTimeInstance(DateFormat.SHORT, Locale.getDefault());
}
