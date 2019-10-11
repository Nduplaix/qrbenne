package com.example.qrbenne;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ScanActivity extends AppCompatActivity {

    private Button buttonAccueil;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scan_activity);


        buttonAccueil = (Button) findViewById(R.id.button);

        buttonAccueil.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(ScanActivity.this, MainActivity.class);

                        startActivity(intent);

                    }
                }

        );


    }
}
