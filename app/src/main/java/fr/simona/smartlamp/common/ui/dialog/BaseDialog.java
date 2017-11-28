package fr.simona.smartlamp.common.ui.dialog;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;
import fr.simona.smartlamp.R;

import fr.simona.smartlamp.common.ui.CommonButton;

/**
 * Created by aaitzeouay on 31/08/2017.
 */

public class BaseDialog extends DialogFragment {

    private static String TYPE_KEY = "DialogType";

    public static final int BUTTON_INDEX_0 = 0;
    public static final int BUTTON_INDEX_1 = 1;

    public static final int UNPAIR_DEVICE = 1;
    public static final int MAM_DIALOG = 2;

    private int dialogType;
    protected DialogListener dialogListener;

    public DialogListener getDialogListener() {
        return dialogListener;
    }

    public BaseDialog setDialogListener(DialogListener dialogListener) {
        this.dialogListener = dialogListener;
        return this;
    }

    public BaseDialog() {}

    @SuppressLint("ValidFragment")
    public BaseDialog(int type) {
        this.dialogType = type;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.custom_confirm_dialog, container);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        int width = (int)(getResources().getDisplayMetrics().widthPixels*0.90);
        view.setMinimumWidth(width);

        TextView tvTitle = (TextView) view.findViewById(R.id.tv_title);
        TextView tvMessage = (TextView) view.findViewById(R.id.tv_message);
        CommonButton positiveButton = (CommonButton) view.findViewById(R.id.btn_positive_answer);
        CommonButton negativeButton = (CommonButton) view.findViewById(R.id.btn_negative_answer);

        switch (dialogType) {
            case UNPAIR_DEVICE:
                tvTitle.setText(R.string.dialog_unpair_device_title);
                tvMessage.setText(R.string.dialog_unpair_device_message);
                break;
            case MAM_DIALOG:
                tvTitle.setText(R.string.bpm_dialog_mam_button);
                tvMessage.setText(R.string.dialog_mam_message);
                positiveButton.setText(R.string.bpm_dialog_mam_button);
                negativeButton.setText(R.string.bpm_dialog_single_mode_button);
                break;
            default:
                throw new IllegalArgumentException("Invalid dialog type " + dialogType);
        }
        positiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialogListener != null) {
                    dialogListener.onButtonClicked(BUTTON_INDEX_1);
                    dismiss();
                }
            }
        });
        negativeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialogListener != null) {
                    dialogListener.onButtonClicked(BUTTON_INDEX_0);
                    dismiss();
                }
            }
        });
        return view;
    }

    @Override
    public void show(FragmentManager manager, String tag) {
        super.show(manager, tag);
        manager.executePendingTransactions();
        getDialog().getWindow().getDecorView().setSystemUiVisibility(
                getActivity().getWindow().getDecorView().getSystemUiVisibility());
    }

    public interface DialogListener {
        void onButtonClicked(int which);
    }

    public static BaseDialog newInstance(int type){
        BaseDialog dialog = new BaseDialog(type);
        Bundle args = new Bundle();
        args.putInt(TYPE_KEY, type);
        dialog.setArguments(args);
        return dialog;
    }

}