package fr.simona.smartlamp.common.ui;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by aaitzeouay on 31/08/2017.
 */

public class FontManager {

    public static final FontManager INSTANCE = new FontManager();
    private static final int FONT_ARRAY_SIZE = 10;

    private Typeface[] cache;

    private FontManager(){
        cache = new Typeface[FONT_ARRAY_SIZE];
    }

    public Typeface getTypeFace(Context context, @Style int type){
        switch (type){
            case Style.O_BOLD:
                return getTypefaceAndCache(type,context,"fonts/OpenSans-Bold.ttf");
            case Style.O_BOLD_ITALIC:
                return getTypefaceAndCache(type,context,"fonts/OpenSans-BoldItalic.ttf");
            case Style.O_EXTRA_BOLD:
                return getTypefaceAndCache(type,context,"fonts/OpenSans-ExtraBold.ttf");
            case Style.O_EXTRA_BOLD_ITALIC:
                return getTypefaceAndCache(type,context,"fonts/OpenSans-ExtraBoldItalic.ttf");
            case Style.O_ITALIC:
                return getTypefaceAndCache(type,context,"fonts/OpenSans-Italic.ttf");
            case Style.O_LIGHT:
                return getTypefaceAndCache(type,context,"fonts/OpenSans-Light.ttf");
            case Style.O_LIGHT_ITALIC:
                return getTypefaceAndCache(type,context,"fonts/OpenSans-LightItalic.ttf");
            case Style.O_REGULAR:
                return getTypefaceAndCache(type,context,"fonts/OpenSans-Regular.ttf");
            case Style.O_SEMI_BOLD:
                return getTypefaceAndCache(type,context,"fonts/OpenSans-Semibold.ttf");
            case Style.O_SEMI_BOLD_ITALIC:
                return getTypefaceAndCache(type,context,"fonts/OpenSans-SemiboldItalic.ttf");
            default:
                throw new IllegalArgumentException("type provided by argument = "+type+" is invalid");
        }
    }

    private Typeface getTypefaceAndCache(int type,Context context, String fontPath){
        if(cache[type] == null){
            cache[type] = Typeface.createFromAsset(context.getAssets(), fontPath);
        }
        return cache[type];
    }

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({Style.O_BOLD,Style.O_BOLD_ITALIC,Style.O_EXTRA_BOLD,
            Style.O_EXTRA_BOLD_ITALIC, Style.O_ITALIC, Style.O_LIGHT, Style.O_LIGHT_ITALIC,
            Style.O_REGULAR, Style.O_SEMI_BOLD, Style.O_SEMI_BOLD_ITALIC,Style.NONE})
    public @interface Style {
        int NONE = -1;
        int O_BOLD = 0;
        int O_BOLD_ITALIC = 1;
        int O_EXTRA_BOLD = 2;
        int O_EXTRA_BOLD_ITALIC = 3;
        int O_ITALIC = 4;
        int O_LIGHT = 5;
        int O_LIGHT_ITALIC = 6;
        int O_REGULAR = 7;
        int O_SEMI_BOLD = 8;
        int O_SEMI_BOLD_ITALIC = 9;

    }
}
