package fr.simona.smartlamp.common.utils;

import android.bluetooth.BluetoothAdapter;

/**
 * Created by aaitzeouay on 08/08/2017.
 */

public class BluetoothUtils {

    private BluetoothUtils() {
        throw new IllegalStateException("BluetoothUtils class");
    }

    public static void enableBluetooth(boolean enable){
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            return;
        } else {
            if (enable && !bluetoothAdapter.isEnabled()) {
                bluetoothAdapter.enable();
            } else {
                bluetoothAdapter.disable();
            }
        }
    }

    public static boolean checkBluetoothAvailability() {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            return false;
        } else {
            if (!bluetoothAdapter.isEnabled()) {
                return false;
            }
        }
        return true;
    }
}
