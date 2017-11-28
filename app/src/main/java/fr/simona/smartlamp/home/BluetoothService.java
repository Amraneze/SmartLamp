package fr.simona.smartlamp.home;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.IBinder;
import android.os.Looper;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;

/*
CREATING A BOUND SERVICE

1) CREATE A PUBLIC CLASS WHICH EXTENDS BINDER CLASS. LET THIS CLASS BE BLUETOOTHSERVICE
2) CREATE AN INSTANCE OF THIS BINDER OF CLASS BLUETOOTHBINDER blebinder.
3) RETURN THIS INSTANCE blebinder FROM THE onBind() fucntion
4) IN MAINACTIVITY.JAVA, RECEIVE THIS BINDER VIA onServiceConnected() fucntion call

 */

public class BluetoothService extends Service implements Runnable {

    private static final String BT_NAME = "raspberrypi"; //"SAMSUNG-SGH-I747";//"GIRIVAN";
    private static final int REQUEST_ENABLE_BT = 1; //should be >0  is returned when startActivityForResult function exits
    private static final int EXIT = 0;
    private static final int CHECK_ADAPTER = 1;
    private static final int ENABLE_BLUETOOTH = 2;
    private static final int QUERY_PAIRED_DEVICES =  3;
    private static final int CONNECT_TO_DEVICE = 4;
    private static final int SCAN_DEVICES = 5;
    private static final int MANAGE_CONNECTION = 6;
    private static final UUID my_uuid= UUID.fromString("94f39d29-7d6d-437d-973b-fba39e49d4ee");

    //private static final String my_uuid = "38:2D:D1:45:70:3D";

    private final IBinder blebinder = new BluetoothBinder();
    private boolean run;
    private int state;
    private BluetoothSocket mmSocket;
    private BluetoothDevice bt_device;
    private InputStream mmInStream;
    private OutputStream mmOutStream;
    private Thread btoothThread = null;
    private Looper mServiceLooper;
    private BluetoothServerSocket mmServerSocket;
    public final static String RX_MSG = "Received Message";
    public final static String CONNECT_STATUS = "0";
    private String buttonPressed = "0";



    //declaring a broadcast receiver to get the discovered bt devices
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            Log.d("BluetoothService", "Entering scanning devices");
            String action = intent.getAction();
            // When discovery finds a device
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Get the BluetoothDevice object from the Intent
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                Log.d("Tejas", "Name :"+device.getName());
                if(device.getName().startsWith(BT_NAME)){
                    bt_device = device;
                    state = CONNECT_TO_DEVICE;
                }
            }
        }
    };

    public BluetoothService() {
    }

    public class BluetoothBinder extends Binder {
        //use the same class name which extends service
        //this getService function is called by the client (mainActivity.java) to return the bluetooth service binder
        //this call allows clients to call the public methods in the bluetoothService class
        BluetoothService getService() {
            return BluetoothService.this;
        }
    }
    //REFER http://developer.android.com/guide/components/services.html
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //This function will be executed when startService(bleService) is called in MainActivity
        Log.d("BluetoothService", "Starting Bluetooth Service");
        //LocalBroadcastManager.getInstance(this).registerReceiver(buttonDataReceiver, new IntentFilter(MainActivity.BUTTON_VALUE));
        // We want this service to continue running until it is explicitly
        // stopped, so return START_STICKY.
        return START_STICKY;
    }

    //THIS WILL EXECUTE WHEN THIS SERVICE IS STARTED
    @Override
    public void onCreate() {
        // Start up the thread running the service.  Note that we create a
        // separate thread because the service normally runs in the process's
        // main thread, which we don't want to block.  We also make it
        // background priority so CPU-intensive work will not disrupt our UI.
        Log.d("BluetoothService", "Starting new Thread");
        if (btoothThread == null) {
            btoothThread = new Thread(this);
            btoothThread.start();
        }
//        HandlerThread thread = new HandlerThread("ServiceStartArguments", Process.THREAD_PRIORITY_BACKGROUND);
//        thread.start();
        // Get the HandlerThread's Looper and use it for our Handler
        //mServiceLooper = thread.getLooper();
    }

    @Override
    public void run() {
        Log.d("BluetoothService", "Entering Run");
        BluetoothAdapter mBluetoothAdapter = null;
        state = CHECK_ADAPTER;
        run = true;
        while(run) {
            Intent connectIntent = new Intent();
            try {
                btoothThread.sleep(100);
            }catch (Exception e) {}
            if(state == CHECK_ADAPTER) {
                connectIntent.setAction(CONNECT_STATUS);
                connectIntent.putExtra(CONNECT_STATUS, "0");
                LocalBroadcastManager.getInstance(this).sendBroadcast(connectIntent);
                mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                if (mBluetoothAdapter == null) {
                    // Device does not support Bluetooth
                    Log.d("BluetoothService", "Bluetooth Adapter not available.");
                    state = EXIT;
                }else {
                    //if adapter is present, then enable bluetooth
                    state = ENABLE_BLUETOOTH;
                    Log.d("BluetoothService", "Bluetooth Adapter acquired.");
                }
            }
            else if(state ==ENABLE_BLUETOOTH) {
                connectIntent.setAction(CONNECT_STATUS);
                connectIntent.putExtra(CONNECT_STATUS, "0");
                LocalBroadcastManager.getInstance(this).sendBroadcast(connectIntent);
                if (!mBluetoothAdapter.isEnabled()) {
                    Log.d("BluetoothService", "Entering enable bt");
                    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    enableBtIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);//use this if you want to start an activity from within an service
                    startActivity(enableBtIntent);
                    continue;
                }
                state = QUERY_PAIRED_DEVICES;
            }
            else if (state ==QUERY_PAIRED_DEVICES) {
                Log.d("BluetoothService", "Querying For Paired devices");
                Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
                // If there are paired devices
                if (pairedDevices.size() > 0) {
                    // Loop through paired devices
                    for (BluetoothDevice device : pairedDevices) {
                        // Add the name and address to an array adapter to show in a ListView
                        if(device.getName().startsWith(BT_NAME)){
                            bt_device = device;
                            state=CONNECT_TO_DEVICE;
                        }
                    }
                    Log.d("BluetoothService", "Found paired device");
                    connectIntent.setAction(CONNECT_STATUS);
                    connectIntent.putExtra(CONNECT_STATUS, "0");
                    LocalBroadcastManager.getInstance(this).sendBroadcast(connectIntent);
                }else {
                    state=SCAN_DEVICES;
                }
            }
            else if(state ==SCAN_DEVICES) {
                Log.d("BluetoothService", "Scanning");
                mBluetoothAdapter.startDiscovery();
                // Register the BroadcastReceiver to get the action_found intent
                IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
                registerReceiver(mReceiver, filter); // Don't forget to unregister during onDestroy
                connectIntent.setAction(CONNECT_STATUS);
                connectIntent.putExtra(CONNECT_STATUS, "0");
                LocalBroadcastManager.getInstance(this).sendBroadcast(connectIntent);
            }
            else if(state == CONNECT_TO_DEVICE) {
                // Get a BluetoothSocket to connect with the given BluetoothDevice
                try {
                    // MY_UUID is the app's UUID string, also used by the server code
                    mmSocket = bt_device.createRfcommSocketToServiceRecord(my_uuid);
                    Log.d("BluetoothService", "Created RFCOMM Connection");
                    connectIntent.setAction(CONNECT_STATUS);
                    connectIntent.putExtra(CONNECT_STATUS, "0");
                    LocalBroadcastManager.getInstance(this).sendBroadcast(connectIntent);
                } catch (IOException e) {
                }

                mBluetoothAdapter.cancelDiscovery();
                boolean waitLonger = true;
                long currentTime = System.currentTimeMillis();
                while (waitLonger) {
                    try {
                        long time = System.currentTimeMillis();
                        if (time-currentTime > 20000) {//wait for 20 sec to connect otherwise exit
                            // Unable to connect; close the socket and get out
                            try {
                                mmSocket.close();
                                Log.d("BluetoothService", "Cannot connect");
                            } catch (IOException closeException) { }
                            state = EXIT;
                            break;
                        }
                        Thread.sleep(1000);
                        mmSocket.connect();
                        state = MANAGE_CONNECTION;
                        Log.d("BluetoothService", "Connected");
                        waitLonger = false;

                    } catch (IOException e1) {
                        Log.d("BluetoothService", "Can't find BT Device");
                        e1.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (IllegalArgumentException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }else if (state == MANAGE_CONNECTION) {
                connectIntent.setAction(CONNECT_STATUS);
                connectIntent.putExtra(CONNECT_STATUS, "1");
                LocalBroadcastManager.getInstance(this).sendBroadcast(connectIntent);
                Log.d("BluetoothService", "Managing connections");
                try {
                    mmInStream = mmSocket.getInputStream();
                    mmOutStream = mmSocket.getOutputStream();
                } catch (IOException e) { Log.d("Tejas", "manage connection error");}

                byte[] buffer = new byte[1024];  // buffer store for the stream
                int bytes; // bytes returned from read()
                try {
//                    Log.d("Tejas", "trying to read");
//                    Log.d("Tejas", Integer.toString(mmInStream.read()));
                    // Read from the InputStream
                    bytes = mmInStream.read(); //mmInstream.read(buffer) will add to the buffer
                    Intent intent = new Intent();
                    intent.setAction(RX_MSG);
                    intent.putExtra(RX_MSG, bytes);
                    LocalBroadcastManager.getInstance(this).sendBroadcast(intent);

                } catch (IOException e) {
                    Log.d("Tejas", "Read Error");
                    break;
                }
            }else if(state == EXIT) {
                Log.d("BluetoothService", "Exiting");
                try {
                    mmSocket.close();
                    stopSelf();
                    state = CHECK_ADAPTER;
                }catch (Exception e) {
                    Log.d("Tejas", "Exception");
                }
            }
        }
    }

    /* Call this from the main activity to send data to the remote device */
    public boolean write(int bytes) {
        String msg = Integer.toString(bytes);
        try {
            mmOutStream.write(msg.getBytes());
        }catch (IOException e) {
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean getButtonStatus(String b) {
        //buttonPressed = b;
        return true;
    }
    //This will execute if bindService fucntion is called.
    //You dont need to stop the service explicitely if the service is started by this fucntion call

    //REFER http://developer.android.com/guide/components/bound-services.html
    //BIND THE SERVICE BY EXTENDING THE BINDER CLASS
    @Override
    public IBinder onBind(Intent intent) {
        //If your service is used only by the local application and does not need to work across processes,
        // then you can implement your own Binder class that provides your client direct access to public methods in the service.
        return blebinder;
    }

    public void stop() {
        Log.d("Bluetooth Service","Stopping Bluetooth Service");
        run = false;
        this.stopSelf();
    }


    //Call this to stop the service
    @Override
    public void onDestroy() {
        Toast.makeText(this, "service done", Toast.LENGTH_SHORT).show();
    }
}



//STEPS BELOW ILLUSTRATE HOW TO CONNECT AS A SERVER. SERVER IS THE ONE WHICH WILL ACCEPT INCOMING CONNECTION REQS FROM OTHER DEVICES.
// IN OUR APPLICATION, THE BT ON THE STM32 BOARD WILL BE THE SERVER. THE CELL PHS WILL BE CLIENTS WHICH WILL CONNECT WITH THE SERVER

//                    Log.d("BluetoothService", "Connected to Device");
//                    BluetoothServerSocket tmp = null;
//                    //CONNECTING AS A SERVER : 1) gET A BLUETOOTH SERVER SOCKET 2) START LISTENING TO CONNECTIONS BY CALLING ACCEPT(). 3) AFTER CONNECTION IS MADE, CALL CLOSE() TO CLOSE OTHER SOCKETS
//                    try {
//                        mmServerSocket = mBluetoothAdapter.listenUsingRfcommWithServiceRecord(BT_NAME, my_uuid);
//                    }catch (IOException e) { }
//                    BluetoothSocket socket = null;
//                    // Keep listening until exception occurs or a socket is returned
//                    while (true) {
//                        try {
//                            socket = mmServerSocket.accept();
//                        } catch (IOException e) {
//                            break;
//                        }
//                        // If a connection was accepted
//                        if (socket != null) {
//                            state = MANAGE_CONNECTION;
//                            // Do work to manage the connection (in a separate thread)
//                        }else {
//                            state = EXIT;
//                        }
//                    }

