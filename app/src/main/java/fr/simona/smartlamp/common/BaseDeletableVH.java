package fr.simona.smartlamp.common;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;

import butterknife.BindView;
import butterknife.ButterKnife;
import fr.simona.smartlamp.R;

/**
 * Created by aaitzeouay on 31/08/2017.
 */

public class BaseDeletableVH extends RecyclerView.ViewHolder {

    @BindView(R.id.cb_selected)
    CheckBox cb_selected;

    public BaseDeletableVH(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
