package fr.simona.smartlamp.common;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aaitzeouay on 31/08/2017.
 */

public abstract class BaseDeletableAdapter<T, VH extends BaseDeletableVH> extends RecyclerView.Adapter<VH> implements DeletableItemsAdapter<T> {

    protected List<T> devices;
    private List<T> selectedDevices;
    private boolean isDeleteModeEnabled;
    private DeletableItemsAdapter.Listener listener;

    public BaseDeletableAdapter(List<T> devices) {
        this.devices = devices;
    }

    public void setListener(DeletableItemsAdapter.Listener listener) {
        this.listener = listener;
    }

    @Override
    public void onBindViewHolder(final VH holder, int position) {
        if (isDeleteModeEnabled) {
            holder.cb_selected.setVisibility(View.VISIBLE);
            final T device = devices.get(position);
            holder.cb_selected.setChecked(selectedDevices.contains(device));
            ((ViewGroup) holder.cb_selected.getParent()).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    handleClickListener(device, holder);
                }
            });
            holder.cb_selected.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    handleClickListener(device, holder);
                }
            });
        } else {
            holder.cb_selected.setVisibility(View.GONE);
        }
    }

    private void handleClickListener(T device, VH holder) {
        int itemIndex = selectedDevices.indexOf(device);
        if (itemIndex >= 0) {
            selectedDevices.remove(device);
        } else {
            selectedDevices.add(device);
        }
        notifyItemChanged(holder.getAdapterPosition());
    }

    public void setDeleteMode(boolean deleteModeEnabled) {
        if (isDeleteModeEnabled != deleteModeEnabled) {
            isDeleteModeEnabled = deleteModeEnabled;
            if (isDeleteModeEnabled) {
                selectedDevices = new ArrayList<>();
            }
            notifyDataSetChanged();
        }
    }

    public List<T> getSelectedDevices() {
        return selectedDevices;
    }

    public void removeSelectedDevices() {
        for (T device : selectedDevices) {
            int index = devices.indexOf(device);
            if (index >= 0) {
                devices.remove(index);
                notifyItemRemoved(index);
            }
        }
        if (devices.isEmpty() && listener != null) {
            listener.onNoData();
        }
    }
}
