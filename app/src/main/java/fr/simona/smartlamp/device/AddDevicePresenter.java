package fr.simona.smartlamp.device;

import android.content.Context;

import fr.simona.smartlamp.common.BasePresenter;
import fr.simona.smartlamp.device.model.DeviceInfo;
import fr.simona.smartlamp.device.repository.DeviceRepository;

/**
 * Created by Amrane Ait Zeouay on 26-Nov-17.
 */

public class AddDevicePresenter extends BasePresenter<AddDeviceView> {

    DeviceRepository repository;

    AddDevicePresenter(Context context) {
        super(context);
        repository = DeviceRepository.Factory.INSTANCE.getInstance();
        getPairedDevices();
    }

    public void savePairedDevice(DeviceInfo device){
        repository.saveDevice(device);
    }

    public void getPairedDevices(){
        view.getPairedDevices(repository.getPairedDevices());
    }
}
