package fr.simona.smartlamp.device.repository;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import fr.simona.smartlamp.device.model.DeviceInfo;
import io.realm.RealmResults;

/**
 * Created by Amrane Ait Zeouay on 26-Nov-17.
 */

public class DeviceRepositoryImpl implements DeviceRepository {

    @Override
    public void saveDevice(DeviceInfo device) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.copyToRealm(device);
        realm.commitTransaction();
        realm.close();
    }

    @Override
    public List<DeviceInfo> getPairedDevices() {
        ArrayList<DeviceInfo> pairedDevices = new ArrayList<>();
        Realm realm = Realm.getDefaultInstance();
        RealmResults<DeviceInfo> results = realm
                .where(DeviceInfo.class)
                .findAll();
        DeviceInfo copy;
        for(DeviceInfo measurement : results) {
            copy = new DeviceInfo(measurement);
            pairedDevices.add(copy);
        }
        realm.close();
        return pairedDevices;
    }

    @Override
    public void unpairDevice(DeviceInfo device) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        RealmResults<DeviceInfo> results = realm
                .where(DeviceInfo.class)
                .equalTo(MAC_FIELD, device.getMac())
                .findAll();
        results.deleteAllFromRealm();
        realm.commitTransaction();
        realm.close();
    }

}