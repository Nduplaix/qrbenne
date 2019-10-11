package com.example.qrbenne;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SmsActivity mSmsActivity = new SmsActivity(this, this);
        // TODO Envoyer le SMS quelque part
    }
}
