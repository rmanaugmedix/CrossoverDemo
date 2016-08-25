package com.crossover.android;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.crossover.android.app.AppController;
import com.crossover.android.utils.Constants;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    public float lat;
    public float lang;
    public String name;
    public String id;
    private String accessToken;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        accessToken = getIntent().getStringExtra("accessToken");
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        makeJsonArrayRequest();

        // Add a marker in Sydney and move the camera
        LatLng tokyo = new LatLng(35.7090259, 139.7319925);
        mMap.addMarker(new MarkerOptions().position(tokyo).title("Tokyo"));

        LatLng hotel = new LatLng(35.776904, 139.7222837);
        mMap.addMarker(new MarkerOptions().position(hotel).title("Hotel Mid In Akabane Ekimae"));

    }

    /**
     * Method to make json array request where response starts with [
     * */
    private void makeJsonArrayRequest() {

        //showpDialog();


        StringRequest postRequest = new StringRequest(Request.Method.GET, Constants.URL_PLACES,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String strResponse) {
                        // response
                        try {

                            Log.e("PLACE: ",strResponse);
                            JSONArray response = new JSONArray(strResponse);

                            for (int i = 0; i < response.length(); i++) {

                                JSONObject item = (JSONObject) response.get(i);

                                JSONObject location = item.getJSONObject("location");
                                lat = location.getLong("lat");
                                lang = location.getLong("lng");

                                name = item.getString("name");
                                id = item.getString("id");
                                LatLng tokyo = new LatLng(lat, lang);
                                mMap.addMarker(new MarkerOptions().position(tokyo).title(name));

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                        Log.d("ERROR","error => "+error.toString());
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("Authorization", accessToken);

                return params;
            }
        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(postRequest);
    }
}
