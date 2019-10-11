package com.example.qrbenne;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ScanActivity extends AppCompatActivity {

    private Button buttonAccueil;
    private TextView textView;

    private LocationManager locationManager;
    private LocationListener locationListener;
    private String locationContent;


    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scan_activity);

        textView = (TextView) findViewById(R.id.textView3);


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


        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                locationContent = "\nlocation :" + location.getLatitude() + " " + location.getLongitude();

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);

            }
        };

        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(
                    new String[] {
                            Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.INTERNET
                    },
                    10
            );

            return;
        } else {
            locationManager.requestLocationUpdates("gps", 5000, 0, locationListener);
            scanQRCode();
        }


    }

    /**
     * On affiche la localisation quand on click sur le bouton.
     * Dans notre application le click de bouton est remplacé par le scan du QRcode.
     * TODO: remplacer le click par le scan du QRCode
     */
    private void scanQRCode() {
        textView.append("test");
    }
}
