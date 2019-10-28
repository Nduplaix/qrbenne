package com.example.qrbenne;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MapActivity extends AppCompatActivity {

    private Button buttonBack;
    private static String URL = "https://nicolasduplaix.com/api/qrbenne/bennes";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_activity);

        final TextView textView = findViewById(R.id.textView2);


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

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JsonObjectRequest objectRequest = new JsonObjectRequest(
                Request.Method.GET,
                URL,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("hydra:member");
                            for (int i=0; i < jsonArray.length(); i++) {
                                JSONObject responseObject = jsonArray.getJSONObject(i);

                                String id = responseObject.getString("identifiant");

                                textView.append("\n"+id);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                }
        );

        requestQueue.add(objectRequest);
    }
}
