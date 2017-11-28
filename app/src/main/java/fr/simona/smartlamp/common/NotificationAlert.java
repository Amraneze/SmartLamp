package fr.simona.smartlamp.common;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;
import android.support.v7.app.NotificationCompat;

import java.util.List;

import fr.simona.smartlamp.R;
import fr.simona.smartlamp.home.HomeActivity;

/**
 * Created by aaitzeouay on 08/08/2017.
 */

public class NotificationAlert {

    private static final int BLE_DEVICE_NOTIFICATION_ID = 1;
    private static final int MEASUREMENT_RECEIVED_NOTIFICATION_ID = 2;
    private static final int MEASUREMENT_FAILED_NOTIFICATION_ID = 3;
    private static final int BLE_DEVICE_GATT_FAILED_NOTIFICATION_ID = 4;
    private static final int SERVER_SUCCESS_NOTIFICATION_ID = 5;
    private static final int SERVER_FAILED_NOTIFICATION_ID = 6;
    private static final int BATTERY_LOW_NOTIFICATION_ID = 7;

    private NotificationManager mNotificationManager;
    private Vibrator vibrator;
    private int notificationNumber = 0;

    public NotificationAlert(Context context) {
        mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
    }

    public void displayNotification(Context context, String title, String message, List<String> contents, @NotificationType int type) {
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);
        mBuilder.setContentTitle(title);
        mBuilder.setContentText(message);
        mBuilder.setTicker("You have a new notification");
        mBuilder.setSmallIcon(R.mipmap.ic_launcher);
        mBuilder.setNumber(++notificationNumber);
        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
        inboxStyle.setBigContentTitle(title);
        inboxStyle.addLine(message);
        for (String content : contents) {
            inboxStyle.addLine(content);
        }
        mBuilder.setStyle(inboxStyle);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(HomeActivity.class);

        stackBuilder.addNextIntent(new Intent(context, HomeActivity.class));
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        mBuilder.setContentIntent(resultPendingIntent);
        vibrator.vibrate(1000);
        //ToneUtils.playServerSucces();
        //toneGenerator.playSuccessSound();
        mNotificationManager.notify(SERVER_SUCCESS_NOTIFICATION_ID, mBuilder.build());
    }
}
