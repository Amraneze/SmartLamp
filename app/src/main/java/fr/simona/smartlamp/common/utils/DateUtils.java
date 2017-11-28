package fr.simona.smartlamp.common.utils;

import android.text.TextUtils;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by aaitzeouay on 31/07/2017.
 */

public class DateUtils {

    private static final String TIMESTAMP_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";
    private static final String MEASUREMENT_DATE_FORMAT = "HH'h'mm - dd/MM/yy";
    private static final SimpleDateFormat dateServerFormat = new SimpleDateFormat(TIMESTAMP_DATE_FORMAT, Locale.getDefault());
    private static final SimpleDateFormat measurementFormat = new SimpleDateFormat(MEASUREMENT_DATE_FORMAT, Locale.getDefault());

    private DateUtils() {
        throw new IllegalStateException("DateUtils class");
    }

    public static String formatTimestamp(long timestamp) {
        return dateServerFormat.format(new Date(timestamp));
    }

    public static String getActualDateServerFormat() {
        return dateServerFormat.format(new Date());
    }

    public static String getActualDate() {
        return dateServerFormat.format(new Date());
    }

    public static String getNotificationFormatDate() {
        return measurementFormat.format(new Date());
    }

    public static String measurementFormat(long timestamp) {
        return measurementFormat.format(new Date(timestamp));
    }

    public static Date parseDate(String dateStr) {
        if (TextUtils.isEmpty(dateStr)) {
            return null;
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat(TIMESTAMP_DATE_FORMAT, Locale.getDefault());
        try {
            return dateFormat.parse(dateStr);
        } catch (Exception e) {
            Log.e("Date parse Exception", e.getMessage());
        }
        return null;
    }

    public static String parseTimestampDate(String dateStr) {
        if (TextUtils.isEmpty(dateStr)) {
            return null;
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat(TIMESTAMP_DATE_FORMAT, Locale.getDefault());
        try {
            return measurementFormat.format(dateFormat.parse(dateStr));
        } catch (Exception e) {
            Log.e("Date parse Exception", e.getMessage());
        }
        return null;
    }
}
