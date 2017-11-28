package fr.simona.smartlamp.device.repository;

import java.util.List;

import fr.simona.smartlamp.common.repository.InjectableFactory;
import fr.simona.smartlamp.device.model.DeviceInfo;

/**
 * Created by Amrane Ait Zeouay on 26-Nov-17.
 */

public interface DeviceRepository {

    public static final String MAC_FIELD = "mac";

    void saveDevice(DeviceInfo device);
    void unpairDevice(DeviceInfo device);
    List<DeviceInfo> getPairedDevices();

    class Factory extends InjectableFactory<DeviceRepository> {
        public static final Factory INSTANCE = new Factory();

        @Override
        protected DeviceRepository createInstance() {
            return new DeviceRepositoryImpl();
        }
    }
}
