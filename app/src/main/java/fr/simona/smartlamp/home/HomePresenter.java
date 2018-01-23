package fr.simona.smartlamp.home;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;
import fr.simona.smartlamp.common.BasePresenter;
import fr.simona.smartlamp.common.utils.BluetoothUtils;
import fr.simona.smartlamp.common.utils.CommonUtils;

/**
 * Created by Amrane Ait Zeouay on 26-Nov-17.
 */

public class HomePresenter extends BasePresenter<HomeView> {

    private BluetoothSocket mmSocket;
    private BluetoothDevice mmDevice = null;

    private static final byte delimiter = 33;
    private int readBufferPosition = 0;

    private Handler mHandler;

    /**
     * Bluetooth Variables
     */
    BluetoothAdapter mBluetoothAdapter;

    public HomePresenter(Context context) {
        super(context);
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        mHandler = new Handler(Looper.getMainLooper());
        if (!CommonUtils.isIntroScreenSeen(context)) {
            Log.e("Amrane", "From here");
            view.displayIntroScreen();
        }
    }

    public void sendBtMsg(String msg2send){
        //UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb"); //Standard SerialPortService ID
        UUID uuid = UUID.fromString("94f39d29-7d6d-437d-973b-fba39e49d4ee"); //Standard SerialPortService ID
        try {
            mmSocket = mmDevice.createRfcommSocketToServiceRecord(uuid);
            if (!mmSocket.isConnected()) {
                mmSocket.connect();
            }
            String msg = msg2send;
            //msg += "\n";
            OutputStream mmOutputStream = mmSocket.getOutputStream();
            mmOutputStream.write(msg.getBytes());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void searchBluetoothDevices() {
        if (!BluetoothUtils.checkBluetoothAvailability()) {
            BluetoothUtils.enableBluetooth(true);
        }
        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
        if (pairedDevices.size() > 0) {
            for (BluetoothDevice device : pairedDevices) {
                //Note, you will need to change this to match the name of your device
                Log.e("Aquarium", "device name "+device.getName());
                if(device.getName().equals("raspberrypi-0")) {
                    Log.e("Aquarium","device name "+ device.getName());
                    mmDevice = device;
                    break;
                }
            }
        }
    }

    public void setLight(boolean isLightOn) {
        new Thread(new workerThread(isLightOn ? "lightOn" : "lightOff")).start();
    }

    public void startMotor(boolean isRunning) {
        new Thread(new workerThread(isRunning ? "motorOn" : "motorOff")).start();
    }

    final class workerThread implements Runnable {

        private String btMsg;

        workerThread(String msg) {
            btMsg = msg;
        }

        public void run() {
            sendBtMsg(btMsg);
            while (!Thread.currentThread().isInterrupted()) {
                int bytesAvailable;
                boolean workDone = false;
                try {
                    final InputStream mmInputStream;
                    mmInputStream = mmSocket.getInputStream();
                    bytesAvailable = mmInputStream.available();
                    if (bytesAvailable > 0) {
                        byte[] packetBytes = new byte[bytesAvailable];
                        Log.e("Aquarium recv bt","bytes available");
                        byte[] readBuffer = new byte[1024];
                        mmInputStream.read(packetBytes);
                        for (int i=0; i < bytesAvailable; i++) {
                            byte b = packetBytes[i];
                            if (b == delimiter) {
                                byte[] encodedBytes = new byte[readBufferPosition];
                                System.arraycopy(readBuffer, 0, encodedBytes, 0, encodedBytes.length);
                                final String data = new String(encodedBytes, "US-ASCII");
                                readBufferPosition = 0;
                                //The variable data now contains our full command
                                mHandler.post(new Runnable() {
                                    public void run() {
                                        //myLabel.setText(data);
                                    }
                                });
                                workDone = true;
                                break;
                            } else {
                                readBuffer[readBufferPosition++] = b;
                            }
                        }
                        if (workDone) {
                            mmSocket.close();
                            break;
                        }
                    }
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }
        }
    }

}
