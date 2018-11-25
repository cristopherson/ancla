package com.prototype.ancla;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private FloodEvent floodEvent = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        //Get the data sent from MainActivity
        Intent intent = getIntent();
        floodEvent = new FloodEvent(
                intent.getStringExtra(FloodEvent.DATASNAPSHOT_CHILD_LATITUDE),
                intent.getStringExtra(FloodEvent.DATASNAPSHOT_CHILD_LONGITUDE),
                intent.getStringExtra(FloodEvent.DATASNAPSHOT_CHILD_STATUS));

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
        double latitude = 0.0;
        double longitude = 0.0;

        if(floodEvent == null) {
            Log.d("LogAncla", "floodEvent object is null, initializing it with defaults");
            floodEvent = new FloodEvent("0,0", "0.0", "0");
        }

        try
        {
            latitude = Double.parseDouble(floodEvent.getLatitude());
            longitude = Double.parseDouble(floodEvent.getLongitude());
        }
        catch(Exception e)
        {
            Log.d("LogAncla", e.getMessage());
        }
        // Add a marker in Sydney and move the camera
        LatLng eventMarker = new LatLng(latitude, longitude);
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        LatLngBounds bounds = null;
        CameraUpdate cu = null;
        int padding = 0;

        mMap.addMarker(new MarkerOptions().position(eventMarker).title("Adding marker"));
        builder.include(eventMarker);
        bounds = builder.build();
        cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);

        mMap.moveCamera(cu);


    }
}
