package com.KeepMySeed.nfc;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;

public class DialogShowTagFragment extends DialogFragment {

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder adb = new AlertDialog.Builder(getActivity());
        adb.setTitle(getActivity().getResources().getString(R.string.dialog_title_tag)+" «"+((MainActivity)getActivity()).name+"»");
        adb.setMessage(((MainActivity)getActivity()).message);
        adb.setPositiveButton(getActivity().getResources().getString(R.string.dialog_btn_copy_tag), new OnClickListener() {
            public void onClick(DialogInterface dialog, int arg1) {
                ((MainActivity)getActivity()).copyTextToClipboard();
            }
        });
        adb.setNegativeButton(getActivity().getResources().getString(R.string.dialog_btn_rewrite_tag), new OnClickListener() {
            public void onClick(DialogInterface dialog, int arg1) {
                ((MainActivity)getActivity()).showDialogMethodWriteTag();
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