package com.KeepMySeed.nfc;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

public class DialogReadEncodedTagFragment extends DialogFragment {

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder adb = new AlertDialog.Builder(getActivity());
        adb.setTitle(getActivity().getResources().getString(R.string.dialog_title_read_data));
        adb.setMessage(getActivity().getResources().getString(R.string.dialog_message_read_encode_tag)+" «"+((MainActivity)getActivity()).name+"»");
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.dialog_write_password, null, false);
        final EditText etPassword = layout.findViewById(R.id.etPassword);
        adb.setView(layout);
        adb.setPositiveButton(getActivity().getResources().getString(R.string.dialog_btn_view), new OnClickListener() {
            public void onClick(DialogInterface dialog, int arg1) {    }
        });
        AlertDialog dialog = adb.create();
        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if( ((MainActivity)getActivity()).checkPassword(etPassword.getText().toString()))
                {
                    ((MainActivity)getActivity()).showDialogShowTag();
                    dismiss();
                }
                else
                ((MainActivity)getActivity()).showToast(getActivity().getResources().getString(R.string.toast_false_password));
            }
        });
        return dialog;
    }

    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
    }

    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
    }
}