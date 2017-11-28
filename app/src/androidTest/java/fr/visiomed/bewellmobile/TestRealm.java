package fr.visiomed.bewellmobile;

import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

import fr.simona.smartlamp.common.log.model.LogValues;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by aaitzeouay on 09/08/2017.
 */

@RunWith(AndroidJUnit4.class)
public class TestRealm {
    @Test
    public void useAppContext() throws Exception {
        ArrayList<LogValues> logs = new ArrayList<>();
        Realm realm = Realm.getDefaultInstance();
        RealmResults<LogValues> results = realm
                .where(LogValues.class)
                .findAll();
        for(LogValues log : results) {
            logs.add(new LogValues(log));
        }
        realm.close();
        Log.e("TestRealm", "Size" +logs.size());
    }
}
