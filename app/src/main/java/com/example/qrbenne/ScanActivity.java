package com.example.qrbenne;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.zxing.Result;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.logging.Logger;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

import static android.Manifest.permission.CAMERA;


public class ScanActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    private static final int REQUEST_CAMERA = 1;
    private ZXingScannerView scannerView;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private double latitude;
    private double longitude;
    private static String URL = "https://nicolasduplaix.com/api/qrbenne/bennes";
    private SmsActivity mSmsActivity = new SmsActivity(this, this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        scannerView = new ZXingScannerView(this);

        displayMessage(
                "Veuillez attendre la détection de la localisation, un message apparaîtra",
                "Attente de localisation",
                null
        );

        setContentView(scannerView);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkPermission()) {
                Toast.makeText(ScanActivity.this, "Permission is granted", Toast.LENGTH_LONG).show();
            } else {
                requestPermissions();
            }
        }

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                latitude = location.getLatitude();
                longitude = location.getLongitude();

                displayMessage(
                        "Vous pouvez scanner",
                        "Localisation trouvée",
                        null
                );

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
        }


    }

    private boolean checkPermission()
    {
        return (ContextCompat.checkSelfPermission(ScanActivity.this, CAMERA) == PackageManager.PERMISSION_GRANTED);
    }

    private void requestPermissions()
    {
        ActivityCompat.requestPermissions(this, new String[]{CAMERA}, REQUEST_CAMERA);
    }

    public void onRequestPermissionResult(int requestCode, String permission[], int grantResults[])
    {
        switch (requestCode)
        {
            case REQUEST_CAMERA:
                if (grantResults.length > 0)
                {
                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (cameraAccepted)
                    {
                        Toast.makeText(ScanActivity.this, "Permission Granted", Toast.LENGTH_LONG).show();
                    } else
                    {
                        Toast.makeText(ScanActivity.this, "Permission Denied", Toast.LENGTH_LONG).show();
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                        {
                            if (shouldShowRequestPermissionRationale(CAMERA))
                            {
                                displayMessage("you need to allow access for both permission",
                                        "",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                requestPermissions(new String[]{CAMERA}, REQUEST_CAMERA);
                                            }
                                        });
                                return;
                            }
                        }
                    }
                }
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            if (checkPermission())
            {
                if (scannerView == null) {
                    scannerView = new ZXingScannerView(this);
                    setContentView(scannerView);
                }
                scannerView.setResultHandler(this);
                scannerView.startCamera();
            }
            else
            {

            }
        }
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        scannerView.stopCamera();
    }

    /**
     * Send the localisation
     * @param message
     * @param listener
     */
    public void displayMessage(String message, String title, DialogInterface.OnClickListener listener)
    {
        new AlertDialog.Builder(ScanActivity.this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", listener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    @Override
    public void handleResult(Result result) {
        final String scanResult = result.getText();


        RequestQueue requestQueue = Volley.newRequestQueue(this);


        JSONObject params = new JSONObject();

        try {
            params.put("latitude", latitude);
            params.put("longitude", longitude);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            String id = scanResult.substring(6);
            JsonObjectRequest objectRequest = new JsonObjectRequest(
                    Request.Method.PATCH,
                    URL + '/' + id,
                    params,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            JSONObject jsonObject = null;
                            try {
                                jsonObject = response;
                                displayMessage(
                                        "Les coordonnées ont bien été envoyés! \n" + scanResult,
                                        "Validé !",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                Intent intent = new Intent(ScanActivity.this, MainActivity.class);
                                                startActivity(intent);
                                            }
                                        }
                                );
                                mSmsActivity.sendSMS();
                            } catch (Exception e) {
                                System.out.println(response);
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            displayMessage(
                                    "Une erreure est survenu lors de la lecture du QR Code",
                                    "Erreur !",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Intent intent = new Intent(ScanActivity.this, MainActivity.class);
                                            startActivity(intent);
                                        }
                                    }
                            );
                            error.printStackTrace();
                        }
                    }
            );

            requestQueue.add(objectRequest);
        } catch (Exception e) {
            displayMessage(
                    "Une erreure est survenu lors de la lecture du QR Code",
                    "Erreur !",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(ScanActivity.this, MainActivity.class);
                            startActivity(intent);
                        }
                    }
            );
            e.printStackTrace();
        }

    }
}
