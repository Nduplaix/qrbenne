package com.example.qrbenne;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.telephony.SmsManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.IOException;

@TargetApi(28)
public class SmsActivity extends AppCompatActivity {
    private final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 1;
    private Context context;
    private Activity activity;

    public SmsActivity(Context context, Activity activity) throws IOException {
        this.context = context;
        this.activity = activity;
    }

    void sendSMS() {
        if (ContextCompat.checkSelfPermission(context,
                Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
                    Manifest.permission.SEND_SMS)) {
            } else {
                ActivityCompat.requestPermissions(activity,
                        new String[]{Manifest.permission.SEND_SMS},
                        MY_PERMISSIONS_REQUEST_READ_CONTACTS);
            }
        }
        SmsManager.getDefault().sendTextMessage(Helper.getMetaData(this, Helper.PHONE_NUMBER), null, "Une nouvelle benne vient d'etre scannée", null, null);
    }
}
