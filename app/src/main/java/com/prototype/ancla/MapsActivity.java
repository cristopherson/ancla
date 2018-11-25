package com.prototype.ancla;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    public static String ANCLA_PARCELABLE_EVENTS_ID = "ANCLA_PARCELABLE_EVENTS_ID";

    private AnclaValueEventListener valueEventListener = new AnclaValueEventListener();
    private DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference messageRef = ref.child("messages");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        valueEventListener.updateEvents(
                (FloodEventContainer) getIntent().getParcelableExtra(MapsActivity.ANCLA_PARCELABLE_EVENTS_ID));

        //Get the data sent from MainActivity
        if(messageRef != null) {
            messageRef.addValueEventListener(valueEventListener);
        } else {
            Log.d(MainActivity.LOG_ANCLA_TAG, "messageRef is null");
        }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if(mapFragment != null) {
            mapFragment.getMapAsync(this);
        } else {
            Log.d(MainActivity.LOG_ANCLA_TAG, "mapFramgement is null");
        }
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
        valueEventListener.setmMap(googleMap);
    }
}
