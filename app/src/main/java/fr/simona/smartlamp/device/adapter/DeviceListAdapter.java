package fr.simona.smartlamp.device.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import fr.simona.smartlamp.R;
import fr.simona.smartlamp.device.model.DeviceInfo;

/**
 * Created by Amrane Ait Zeouay on 26-Nov-17.
 */

public class DeviceListAdapter extends RecyclerView.Adapter<DeviceListAdapter.DeviceHolder> {

    private List<DeviceInfo> devices;
    private DevicePairedListener listener;

    public DeviceListAdapter(List<DeviceInfo> devices) {
        this.devices = devices;
    }

    public void setOnDeviceAddedListener(DevicePairedListener listener) {
        this.listener = listener;
    }

    @Override
    public DeviceHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.add_device_list, parent, false);
        return new DeviceHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final DeviceHolder holder, final int position) {
        final DeviceInfo device = devices.get(position);
        holder.tvDeviceName.setText(device.getDeviceName());
        //holder.ivdeviceLogo.setBackgroundResource(DeviceUtils.getGenericDeviceDrawable(device.getType()));
        holder.tvDeviceMac.setText(device.getMac());
        holder.rlPairDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onDeviceAdded(device);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return devices.size();
    }

    static class DeviceHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.rl_pair_device)
        RelativeLayout rlPairDevice;
        @BindView(R.id.bwm_logo)
        ImageView ivdeviceLogo;
        @BindView(R.id.tv_device_name)
        TextView tvDeviceName;
        @BindView(R.id.tv_device_mac)
        TextView tvDeviceMac;

        public DeviceHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface DevicePairedListener {
        void onDeviceAdded(DeviceInfo device);
    }
}
