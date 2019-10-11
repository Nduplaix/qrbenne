package com.example.qrbenne;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MapActivity extends AppCompatActivity {

    private Button buttonBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_activity);


         buttonBack = (Button) findViewById(R.id.button);

         buttonBack.setOnClickListener(
             new View.OnClickListener() {
                 @Override
                 public void onClick(View view) {
                     Intent intent = new Intent(MapActivity.this, MainActivity.class);

                     startActivity(intent);
                 }
             }

         );
    }
}
