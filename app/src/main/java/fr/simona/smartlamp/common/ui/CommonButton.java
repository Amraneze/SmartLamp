package fr.simona.smartlamp.common.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.Button;
import fr.simona.smartlamp.R;

/**
 * Created by aaitzeouay on 31/08/2017.
 */

public class CommonButton extends Button {

    private int style;

    public CommonButton(Context context) {
        super(context);
        style = FontManager.Style.O_SEMI_BOLD;
        init();
    }

    public CommonButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        parseAttributes(attrs);
        init();
    }

    public CommonButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        parseAttributes(attrs);
        init();
    }

    public CommonButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        parseAttributes(attrs);
        init();
    }

    private void parseAttributes(AttributeSet attrs){
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.CommonTextView, 0, 0);
        style = a.getInt(R.styleable.CommonTextView_font_style, FontManager.Style.O_SEMI_BOLD);
        a.recycle();
    }

    private void init(){
        if(isInEditMode()){
            return;
        }
        setTypeface(FontManager.INSTANCE.getTypeFace(getContext(), style));
    }


}
