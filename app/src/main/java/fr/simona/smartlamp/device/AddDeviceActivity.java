package fr.simona.smartlamp.device;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;


import java.util.List;
import java.util.Set;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import fr.simona.smartlamp.R;
import fr.simona.smartlamp.device.adapter.DeviceListAdapter;
import fr.simona.smartlamp.device.model.DeviceInfo;
import fr.simona.smartlamp.device.utils.ConnectThread;
import fr.simona.smartlamp.home.MainActivity;

/**
 * Created by aaitzeouay on 01/08/2017.
 */

public class AddDeviceActivity extends AppCompatActivity implements AddDeviceView {

    public static final String DEVICE_MAC_STRING = "device_mac";

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.btn_scan_devices)
    ImageButton btnScanDevices;
    @BindView(R.id.recycler_view)
    RecyclerView rvAddDevices;
    @BindView(R.id.ll_loading)
    LinearLayout llLoading;

    private AddDevicePresenter presenter;
    private DeviceListAdapter adapter;
    private List<DeviceInfo> pairedDevices;
    private BroadcastReceiver bluetoothStatusReceiver;

    private ConnectThread connectionManager;

    private BluetoothAdapter bluetoothAdapter;
    IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
    private final BroadcastReceiver bReciever = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                // Create a new device item
                DeviceInfo newDevice = new DeviceInfo(device.getAddress(), device.getName());
                // Add it to our adapter
                pairedDevices.add(newDevice);
                adapter.notifyDataSetChanged();
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayOptions(android.support.v7.app.ActionBar.DISPLAY_SHOW_CUSTOM);
            getSupportActionBar().setCustomView(R.layout.loading_actionbar);
        }
        setContentView(R.layout.add_device_layout);
        ButterKnife.bind(this);
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        presenter = new AddDevicePresenter(this);
        connectionManager = new ConnectThread();
        Set<BluetoothDevice> setPairedDevices = bluetoothAdapter.getBondedDevices();
        if (setPairedDevices.size() > 0) {
            for (BluetoothDevice device : setPairedDevices) {
                //DeviceItem newDevice= new DeviceItem(device.getName(),device.getAddress(),"false");
                Log.e("Amrane", "device "+device.getName());
            }
        }
        setUpToolbar();
        setUpRecyclerView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        startScanning();
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopScan();
    }

    private void startScanning() {
        new Handler(getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                registerReceiver(bReciever, filter);
                bluetoothAdapter.startDiscovery();
                adapter.notifyDataSetChanged();
            }
        });
    }

    private void stopScan() {
        unregisterReceiver(bReciever);
        bluetoothAdapter.cancelDiscovery();
    }

    private void setUpRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rvAddDevices.setLayoutManager(layoutManager);
        rvAddDevices.addItemDecoration(new DividerItemDecoration(this, layoutManager.getOrientation()));
        rvAddDevices.setItemAnimator(new DefaultItemAnimator());
        adapter = new DeviceListAdapter(pairedDevices);
        llLoading.setVisibility(adapter.getItemCount() != 0 ? View.GONE : View.VISIBLE);
        adapter.setOnDeviceAddedListener(new DeviceListAdapter.DevicePairedListener() {
            @Override
            public void onDeviceAdded(DeviceInfo device) {
                presenter.savePairedDevice(device);
                connectionManager.connect(bluetoothAdapter.getRemoteDevice(device.getMac()),
                        UUID.fromString("00001101-0000-1000-8000-00805f9b34fb"));
                Intent pairedDeviceIntent = new Intent(AddDeviceActivity.this, MainActivity.class);
                startActivity(pairedDeviceIntent);
                finish();
            }
        });
        rvAddDevices.setAdapter(adapter);
    }

    private void setUpToolbar() {
        setSupportActionBar(toolbar);
        btnScanDevices.setVisibility(View.GONE);
        toolbar.setNavigationIcon(R.drawable.action_bar_back_btn);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void getPairedDevices(List<DeviceInfo> devices) {
        this.pairedDevices = devices;
    }
}
