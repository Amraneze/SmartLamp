package fr.simona.smartlamp.application;

import android.app.Application;

import io.realm.Realm;

/**
 * Created by aaitzeouay on 28/07/2017.
 */

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
    }
}
