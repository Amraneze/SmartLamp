package fr.simona.smartlamp.common;

import android.support.annotation.IntDef;

/**
 * Created by aaitzeouay on 08/08/2017.
 */

@IntDef({NotificationType.MEASUREMENT_RECEIVED, NotificationType.MEASUREMENT_FAILED,
        NotificationType.DEVICE_CONNECTED, NotificationType.DEVICE_DISCONNECTED, NotificationType.DEVICE_GATT_FAILED,
        NotificationType.SERVER_SUCCES, NotificationType.SERVER_FAILED, NotificationType.BATTERY_LOW})
public @interface NotificationType {

    int MEASUREMENT_RECEIVED = 1;
    int MEASUREMENT_FAILED = 2;
    int DEVICE_CONNECTED = 3;
    int DEVICE_DISCONNECTED = 4;
    int DEVICE_GATT_FAILED = 5;
    int SERVER_SUCCES = 6;
    int SERVER_FAILED = 7;
    int BATTERY_LOW = 8;

}
