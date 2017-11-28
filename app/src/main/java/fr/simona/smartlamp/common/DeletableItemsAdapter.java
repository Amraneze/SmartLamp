package fr.simona.smartlamp.common;

import java.util.List;

/**
 * Created by aaitzeouay on 31/08/2017.
 */

public interface DeletableItemsAdapter<T> {

    void setDeleteMode(boolean deleteModeEnabled);

    void setListener(Listener listener);

    List<T> getSelectedDevices();

    void removeSelectedDevices();

    interface Listener {
        void onNoData();
    }
}
