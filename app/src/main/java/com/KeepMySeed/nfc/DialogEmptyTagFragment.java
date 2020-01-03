package com.KeepMySeed.nfc;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

public class DialogEmptyTagFragment extends DialogFragment {
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder adb = new AlertDialog.Builder(getActivity());
        adb.setTitle(getActivity().getResources().getString(R.string.dialog_title_empty_tag));
        adb.setMessage(getActivity().getResources().getString(R.string.dialog_message_empty_tag));
        adb.setPositiveButton(getActivity().getResources().getString(R.string.dialog_btn_yes), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int arg1) {
                ((MainActivity)getActivity()).showDialogMethodWriteTag();
            }
        });
        adb.setNegativeButton(getActivity().getResources().getString(R.string.dialog_btn_cancel), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int arg1) {}
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