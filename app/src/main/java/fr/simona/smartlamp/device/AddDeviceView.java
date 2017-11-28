package fr.simona.smartlamp.device;

import java.util.List;

import fr.simona.smartlamp.device.model.DeviceInfo;

/**
 * Created by Amrane Ait Zeouay on 26-Nov-17.
 */

public interface AddDeviceView {

    void getPairedDevices(List<DeviceInfo> devices);

}
