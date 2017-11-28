package fr.simona.smartlamp.device.model;

import io.realm.RealmObject;

/**
 * Created by Amrane Ait Zeouay on 26-Nov-17.
 */

public class DeviceInfo extends RealmObject {

    private String mac;
    private String deviceName;

    public DeviceInfo() {}

    public DeviceInfo(String mac, String deviceName) {
        this.mac = mac;
        this.deviceName = deviceName;
    }

    public DeviceInfo(DeviceInfo model) {
        this.mac = model.getMac();
        this.deviceName = model.getDeviceName();
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }
}
