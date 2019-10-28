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

@TargetApi(28)
public class SmsActivity extends AppCompatActivity {

    private static String phoneNumber = "5554";
    private final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 1;
    private Context context;
    private Activity activity;

    public SmsActivity(Context context, Activity activity){
        this.context = context;
        this.activity = activity;
    }

    void sendSMS(String message) {
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
        SmsManager.getDefault().sendTextMessage(phoneNumber, null, message, null, null);
    }
}
