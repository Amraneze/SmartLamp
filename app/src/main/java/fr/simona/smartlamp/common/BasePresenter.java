package fr.simona.smartlamp.common;

import android.content.Context;

/**
 * Created by aaitzeouay on 01/08/2017.
 */

public class BasePresenter<T> {

    protected T view;

    public BasePresenter(Context context){
        view = (T)context;
    }

    public BasePresenter(Context context, T view){
        this.view = view;
    }
}
