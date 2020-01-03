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

public class DialogWriteEncodedTagFragment extends DialogFragment {

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder adb = new AlertDialog.Builder(getActivity());
        adb.setTitle(getActivity().getResources().getString(R.string.dialog_title_write_encode_data));
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.dialog_write_encoded_tag, null, false);
        final EditText etName = layout.findViewById(R.id.etName);
        final EditText etInfo = layout.findViewById(R.id.etInfo);
        final EditText etPassword = layout.findViewById(R.id.etPassword);
        final EditText etConfirmPassword = layout.findViewById(R.id.etConfirmPassword);
        adb.setView(layout);
        adb.setPositiveButton(getActivity().getResources().getString(R.string.dialog_btn_write), new OnClickListener() {
            public void onClick(DialogInterface dialog, int arg1) {}
        });
        AlertDialog dialog = adb.create();
        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean wantToCloseDialog = false;
                if(etPassword.getText().toString().compareTo(etConfirmPassword.getText().toString())!=0)
                    ((MainActivity)getActivity()).showToast(getActivity().getResources().getString(R.string.toast_comparison_password));
                else
                    {
                        ((MainActivity)getActivity()).writeTag(etName.getText().toString(),true,etInfo.getText().toString(),etPassword.getText().toString());
                        dismiss();
                    }
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