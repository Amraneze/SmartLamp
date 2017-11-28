package fr.simona.smartlamp.common.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.UUID;

/**
 * Created by aaitzeouay on 01/08/2017.
 */

public class CommonUtils {

    private CommonUtils() {
        throw new IllegalStateException("CommonUtils class");
    }

    public static String generateUUID(){
        return UUID.randomUUID().toString();
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static String fromDotToComma(float value) {
        DecimalFormat df = new DecimalFormat("#.0");
        DecimalFormatSymbols sym = DecimalFormatSymbols.getInstance();
        sym.setDecimalSeparator(',');
        df.setDecimalFormatSymbols(sym);
        return df.format(value);
    }
}
