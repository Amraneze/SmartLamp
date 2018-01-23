package fr.simona.smartlamp.home;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.wooplr.spotlight.SpotlightView;

import butterknife.BindView;
import butterknife.ButterKnife;
import fr.simona.smartlamp.R;
import fr.simona.smartlamp.common.ui.CommonButton;

/**
 * Created by Amrane Ait Zeouay on 26-Nov-17.
 */

public class HomeActivity extends AppCompatActivity implements HomeView {

    private static final String INTRO_CARD = "rotation_intro";
    private static final String INTRO_SWITCH_LIGHT = "switch_light_intro";

    @BindView(R.id.btn_switch_light)
    CommonButton btnSwitchLight;

    BluetoothService bleService;
    private String statusNotPressed = "0";
    private String statusPressed = "1";
    private boolean receiverRegistered = false;
    public final static String BUTTON_VALUE = "Button Value";
    private int pressed = 0;
    public String isConnected = "0";
    private boolean isRevealEnabled = true;
    private SpotlightView spotLight;

    private HomePresenterN presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        presenter = new HomePresenterN(this);
        //creating a new intent to start a bluetooth service (blutoothService.java)
        // or can use intent.setClassName("com.tchafekar.helloworld", "com.tchafekar.helloworld.BluetoothService");
        Intent bleService = new Intent(this, BluetoothService.class);
        //This will start the service and execute the onStartCommand() fucntion.
        // You will have to stop the service (stopService()) explicitely otherwise it will keep running indefinitely

        startService(bleService);

        //Other method to start a service is bindService(). This calls onBind() function.
        //When no devices are bound to the service, the service destroys itself. No need to stop explicitely
        bindService(bleService, bleConnection, Context.BIND_AUTO_CREATE);

        registerReceiver(mMessageReceiver, new IntentFilter(BluetoothService.RX_MSG));
        registerReceiver(mMessageReceiver, new IntentFilter(BluetoothService.CONNECT_STATUS));
    }

    @Override
    public void displayIntroScreen() {
        showIntro(btnSwitchLight, INTRO_SWITCH_LIGHT);
        showIntro(btnSwitchLight, INTRO_SWITCH_LIGHT);
        Log.e("Amrane", "From here");
        spotLight.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                Log.e("Amrane", "From touch");
                return false;
            }
        });
    }

    private void showIntro(View view, String usageId) {
        spotLight = new SpotlightView.Builder(this)
                .introAnimationDuration(400)
                .enableRevealAnimation(isRevealEnabled)
                .performClick(true)
                .fadeinTextDuration(400)
                //.setTypeface(FontUtil.get(this, "RemachineScript_Personal_Use"))
                .headingTvColor(Color.parseColor("#eb273f"))
                .headingTvSize(32)
                .headingTvText("Love")
                .subHeadingTvColor(Color.parseColor("#ffffff"))
                .subHeadingTvSize(16)
                .subHeadingTvText("Like the picture?\nLet others know.")
                .maskColor(Color.parseColor("#dc000000"))
                .target(view)
                .lineAnimDuration(400)
                .lineAndArcColor(Color.parseColor("#eb273f"))
                .dismissOnTouch(true)
                .dismissOnBackPress(true)
                .enableDismissAfterShown(true)
                .usageId(usageId) //UNIQUE ID
                .show();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!receiverRegistered) {
            Intent intent = new Intent(this, BluetoothService.class);
            startService(intent);
            bindService(intent, bleConnection, Context.BIND_AUTO_CREATE);
//
            receiverRegistered = true;
            LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, new IntentFilter(BluetoothService.RX_MSG));
            LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, new IntentFilter(BluetoothService.CONNECT_STATUS));
        }
    }


    public void onPause() {
        super.onPause();
        if (receiverRegistered) {
            receiverRegistered = false;
            unbindService(bleConnection);
            LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
        }
    }


    /**
     * Defines callbacks for service binding, passed to bindService()
     */
    private ServiceConnection bleConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            // We've bound to BluetoothService, cast the IBinder return value (ie blebinder) and get BluetoothService instance
            bleService = ((BluetoothService.BluetoothBinder) service).getService();
            //after this bleService can access all the public fucntion in the BluetoothService class.
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
        }
    };


    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //int message = intent.getIntExtra(BluetoothService.RX_MSG, 0);
            isConnected = intent.getStringExtra(BluetoothService.CONNECT_STATUS);
            //Log.d("Tejas", isConnected);
            //Button btOn = (Button) findViewById(R.id.btOnButton);
            TextView btStat = (TextView) findViewById(R.id.btStatus);
            if(isConnected == "0") {
                //btOn.setText(R.string.btOff);
                btStat.setText(R.string.btStatusConnecting);
                //Log.d("Tejas", "Not Connected");

            }else if (isConnected == "1") {
                //Log.d("Tejas", "connected");
                //btOn.setText(R.string.btOn);
                btStat.setText(R.string.btStatusConnected);
                btStat.setTextColor(android.graphics.Color.GREEN);
            }
        }
    };

//    public void btOnClicked(View view) {
//        Button b = (Button) view;
//        //TextView btStat = (TextView) findViewById(R.id.btStatus);
//        if (b.getText() == getString(R.string.btOff)) {
//            b.setText(R.string.btOn);
//            //btStat.setText(R.string.btStatusConnecting);
//            //btStat.setTextColor(android.graphics.Color.WHITE);
//            //start bt service
//            Intent bleService = new Intent(this, BluetoothService.class);
//            startService(bleService);
//            bindService(bleService, bleConnection, Context.BIND_AUTO_CREATE);
//            registerReceiver(mMessageReceiver, new IntentFilter(BluetoothService.RX_MSG));
//            registerReceiver(mMessageReceiver, new IntentFilter(BluetoothService.CONNECT_STATUS));
//        } else if (b.getText() == getString(R.string.btOn)) {
//            b.setText(R.string.btOff);
//            //stop bt service
//            unbindService(bleConnection);
//            //btStat.setText(R.string.btStatusTurningOff);
//            //btStat.setTextColor(android.graphics.Color.WHITE);
//            LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
//        }
//    }


    public void onLight1SwitchClicked(View view) {
        Button b = (Button) view;
        String s = "";
        int w = 0;
        if (b.getText() == getString(R.string.lightOff)) {
            b.setText(R.string.lightOn);
            s = "1";
            w = 1;

        } else if (b.getText() == getString(R.string.lightOn)) {
            b.setText(R.string.lightOff);
            s = "0";
            w = 0;
        }
        try {
            bleService.write(w);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}