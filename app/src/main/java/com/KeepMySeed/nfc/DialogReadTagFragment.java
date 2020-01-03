package com.KeepMySeed.nfc;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;

public class DialogReadTagFragment extends DialogFragment {

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder adb = new AlertDialog.Builder(getActivity());
        adb.setTitle(getActivity().getResources().getString(R.string.dialog_title_read_data));
        adb.setMessage(getActivity().getResources().getString(R.string.dialog_message_read_tag)+" «"+((MainActivity)getActivity()).name+"»");
        adb.setPositiveButton(getActivity().getResources().getString(R.string.dialog_btn_view), new OnClickListener() {
            public void onClick(DialogInterface dialog, int arg1) {
                ((MainActivity)getActivity()).showDialogShowTag();
            }
        });
        return adb.create();
    }

    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
    }

    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
    }
}