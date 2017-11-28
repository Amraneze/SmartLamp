package fr.simona.smartlamp.common.ui;

import android.support.annotation.ColorRes;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.TextView;
import fr.simona.smartlamp.R;

/**
 * Created by aaitzeouay on 31/08/2017.
 */

public class CustomSnackBar {

    @ColorRes
    public static final int SUCCESS_COLOR = R.color.snack_success_color;
    @ColorRes
    public static final int ERROR_COLOR = R.color.snack_error_color;
    @ColorRes
    public static final int DEFAULT_TEXT_COLOR = R.color.white;

    public static final int SNACKBAR_DURATION_SHORT = 2000;
    public static final int SNACKBAR_DURATION_LONG = 4000;

    private CharSequence message;

    @ColorRes
    private int backgroundColor;
    @ColorRes
    private int textColor;
    @ColorRes
    private int actionTextColor;
    private String action;
    private SnackBarActionListener actionListener;
    private int duration;

    public CustomSnackBar(CharSequence message) {
        this.message = message;
        backgroundColor = SUCCESS_COLOR;
        duration = SNACKBAR_DURATION_SHORT;
        textColor = DEFAULT_TEXT_COLOR;
        actionTextColor = DEFAULT_TEXT_COLOR;
    }

    public CustomSnackBar setBackgroundColor(@ColorRes int backgroundColor) {
        this.backgroundColor = backgroundColor;
        return this;
    }

    public CustomSnackBar setActionListener(SnackBarActionListener actionListener) {
        this.actionListener = actionListener;
        return this;
    }

    public int getActionTextColor() {
        return actionTextColor;
    }

    public CustomSnackBar setActionTextColor(int actionTextColor) {
        this.actionTextColor = actionTextColor;
        return this;
    }

    public String getAction() {
        return action;
    }

    public CustomSnackBar setAction(String action) {
        this.action = action;
        return this;
    }

    public CustomSnackBar setDuration(int duration) {
        this.duration = duration;
        return this;
    }

    public CharSequence getMessage() {
        return message;
    }

    @ColorRes
    public int getBackgroundColor() {
        return backgroundColor;
    }

    public SnackBarActionListener getActionListener() {
        return actionListener;
    }

    public int getDuration() {
        return duration;
    }

    @ColorRes
    public int getTextColor() {
        return textColor;
    }

    public CustomSnackBar setTextColor(@ColorRes int textColor) {
        this.textColor = textColor;
        return this;
    }

    public static CustomSnackBar newMessage(CharSequence message) {
        return new CustomSnackBar(message).setBackgroundColor(SUCCESS_COLOR);
    }

    public static CustomSnackBar newError(CharSequence error) {
        return new CustomSnackBar(error).setBackgroundColor(ERROR_COLOR);
    }

    public interface SnackBarActionListener {
        void onSnackBarActionPressed();
    }

    public static void showSnackBar(View v, final CustomSnackBar options) {
        final Snackbar snackbar = Snackbar.make(v, options.getMessage(), options.getDuration());
        snackbar.getView().setBackgroundColor(ContextCompat.getColor(v.getContext(), options.getBackgroundColor()));
        TextView txt = (TextView) snackbar.getView().findViewById(android.support.design.R.id.snackbar_text);
        txt.setTextSize(20);
        txt.setTypeface(FontManager.INSTANCE.getTypeFace(v.getContext(), FontManager.Style.O_BOLD));
        txt.setTextColor(ContextCompat.getColor(v.getContext(), options.getTextColor()));
        snackbar.show();
    }
}
