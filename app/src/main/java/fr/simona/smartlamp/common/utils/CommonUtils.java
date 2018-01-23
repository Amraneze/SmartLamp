package fr.simona.smartlamp.common.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.UUID;

/**
 * Created by aaitzeouay on 01/08/2017.
 */

public class CommonUtils {

    private static final String PREFERENCE_KEY_SCREEN_SEEN = "SCREENSEEN";
    private static final String PREFERENCE_NAME = "simona.smartlamp";


    private CommonUtils() {
        throw new IllegalStateException("CommonUtils class");
    }

    public static String generateUUID(){
        return UUID.randomUUID().toString();
    }

    public static String fromDotToComma(float value) {
        DecimalFormat df = new DecimalFormat("#.0");
        DecimalFormatSymbols sym = DecimalFormatSymbols.getInstance();
        sym.setDecimalSeparator(',');
        df.setDecimalFormatSymbols(sym);
        return df.format(value);
    }

    public static void setIntroScreenSeen(Context context, boolean isSeen) {
        SharedPreferences sp = context.getSharedPreferences(PREFERENCE_NAME, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(PREFERENCE_KEY_SCREEN_SEEN, isSeen);
        editor.apply();
    }

    public static boolean isIntroScreenSeen(Context context) {
        SharedPreferences sp = context.getSharedPreferences(PREFERENCE_NAME, Activity.MODE_PRIVATE);
        return sp.getBoolean(PREFERENCE_KEY_SCREEN_SEEN, false);
    }
}
