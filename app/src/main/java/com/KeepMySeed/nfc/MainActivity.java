package com.KeepMySeed.nfc;

import android.app.PendingIntent;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.FormatException;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.scottyab.aescrypt.AESCrypt;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.security.GeneralSecurityException;

public class MainActivity extends AppCompatActivity {
    String name = "";
    boolean encoded = false;
    String message = "";
    RelativeLayout rlActivityMain;
    TextView tvInfo;
    DialogFragment dialogEmptyTagFragment,dialogWriteTagFragment,dialogWriteEncodedTagFragment,dialogReadTagFragment,dialogReadEncodedTagFragment,dialogShowTagFragment,dialogMethodWriteTagFragment,dialogSelectActionFragment;
    boolean isWrite = false;
    NfcAdapter mNfcAdapter;
    String txt = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rlActivityMain = findViewById(R.id.activity_main);
        tvInfo = findViewById(R.id.tvInfo);
        dialogEmptyTagFragment = new DialogEmptyTagFragment();
        dialogWriteTagFragment = new DialogWriteTagFragment();
        dialogWriteEncodedTagFragment = new DialogWriteEncodedTagFragment();
        dialogReadTagFragment = new DialogReadTagFragment();
        dialogReadEncodedTagFragment = new DialogReadEncodedTagFragment();
        dialogShowTagFragment = new DialogShowTagFragment();
        dialogMethodWriteTagFragment = new DialogMethodWriteTagFragment();
        dialogSelectActionFragment = new DialogSelectActionFragment();
        initNFC();
    }
    //инициализация NFC
    private void initNFC() {
        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter tagDetected = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);
        IntentFilter ndefDetected = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
        IntentFilter techDetected = new IntentFilter(NfcAdapter.ACTION_TECH_DISCOVERED);
        IntentFilter[] nfcIntentFilter = new IntentFilter[]{techDetected, tagDetected, ndefDetected};
        PendingIntent pendingIntent = PendingIntent.getActivity(
                this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        if (mNfcAdapter != null)
            mNfcAdapter.enableForegroundDispatch(this, pendingIntent, nfcIntentFilter, null);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mNfcAdapter != null)
            mNfcAdapter.disableForegroundDispatch(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        if (tag != null) {
            Ndef ndef = Ndef.get(tag);
            if (isWrite) {
                writeToNfc(ndef, message);
            } else {
                readFromNFC(ndef);
            }
        }
    }

//разбор строки
    public boolean getInfo(String response) {
        try {
            JSONObject userJson = new JSONObject(response);
            name = userJson.getString("name");
            encoded = userJson.getBoolean("encoded");
            message = userJson.getString("message");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
//чтение метки
    private void readFromNFC(Ndef ndef) {
        try {
            ndef.connect();
            NdefMessage ndefMessage = ndef.getNdefMessage();
            txt = new String(ndefMessage.getRecords()[0].getPayload());
            if (!getInfo(txt)) {
                dialogEmptyTagFragment.show(getFragmentManager(), "dlgEmptyTagFragment");
            } else if (encoded)
                dialogReadEncodedTagFragment.show(getFragmentManager(), "dlgReadEncodedTagFragment");
            else
                dialogReadTagFragment.show(getFragmentManager(), "dlgReadTagFragment");
            ndef.close();
        } catch (Exception e) {
            e.printStackTrace();
            dialogEmptyTagFragment.show(getFragmentManager(), "dlgEmptyTagFragment");
        }
    }
//вызов диалога записи некодированной метки
    void showDialogWriteTagDialog() { dialogWriteTagFragment.show(getFragmentManager(), "dlgWriteTagFragment");}
    //вызов диалога записи закодированной метки
    void showDialogWriteEncodedTagDialog() { dialogWriteEncodedTagFragment.show(getFragmentManager(), "dlgWriteEncodedTagFragment");}
    //вызов диалога просмотра метки
    void showDialogShowTag() { dialogShowTagFragment.show(getFragmentManager(), "dlgShowTagFragment");}
    //вызов диалога выбора способа записи метки
    void showDialogMethodWriteTag() { dialogMethodWriteTagFragment.show(getFragmentManager(), "dlgMethodWriteTagFragment");}
    //вывод сообщения
    void showToast(String str) { Toast.makeText(getBaseContext(), str, Toast.LENGTH_LONG).show(); }
//копирование текста в буфер
    void copyTextToClipboard() {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("message", message);
        clipboard.setPrimaryClip(clip);
    }



//включение режима записи метки
    void writeTag(String name, boolean encoded, String message1, String password) {
        if (encoded) {
            //кодирование строки
            try {
                message = AESCrypt.encrypt(password, message1);
            } catch (GeneralSecurityException e) {
                e.printStackTrace();
            }
        }
        else
            message=message1;
        try {
            JSONObject manJson = new JSONObject();
            manJson.put("name", name);
            manJson.put("encoded", encoded);
            manJson.put("message", message);
            message = manJson.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        isWrite = true;
        tvInfo.setText(getResources().getString(R.string.message_write_tag));
    }

    //включение режима чтения метки
    void readTag() {
        isWrite = false;
        tvInfo.setText(getResources().getString(R.string.message_read_tag));
    }

    //запись информации на метку
    private void writeToNfc(Ndef ndef, String message) {
        if (ndef != null) {
            try {
                ndef.connect();
                NdefRecord mimeRecord = NdefRecord.createMime("text/plain", message.getBytes("UTF-8"));
                ndef.writeNdefMessage(new NdefMessage(mimeRecord));
                ndef.close();
                Toast.makeText(this, "Данные успешно записаны", Toast.LENGTH_SHORT).show();
                dialogSelectActionFragment.show(getFragmentManager(), "dlgSelectActionFragment");
            } catch (IOException | FormatException e) {
                e.printStackTrace();
            }

        }
    }

    //проверка пароля
    boolean checkPassword(String password) {
        try {
            message = AESCrypt.decrypt(password, message);
            return true;
        } catch (GeneralSecurityException e) {
            return false;
        }
    }

}