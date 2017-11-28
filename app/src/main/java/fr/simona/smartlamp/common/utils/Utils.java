package fr.simona.smartlamp.common.utils;

import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by aaitzeouay on 27/10/2017.
 */

public class Utils {

    private static final String BWCM_DATA_RESET = "BWCM_DATA_RESET";
    private static final String DATA_SENT_AND_DELETED = "MeasurementSent";

    private Utils() {
        throw new IllegalStateException("Utils class");
    }

    public static void setDataDeleted(Context context) {
        SharedPreferences.Editor editor = context.getSharedPreferences(BWCM_DATA_RESET, MODE_PRIVATE).edit();
        editor.putBoolean(DATA_SENT_AND_DELETED, true);
        editor.apply();
    }

    public static boolean isDataReseted(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(BWCM_DATA_RESET, MODE_PRIVATE);
        return prefs.getBoolean(DATA_SENT_AND_DELETED, false);
    }
}
