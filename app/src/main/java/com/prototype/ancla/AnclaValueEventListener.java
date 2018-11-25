package com.prototype.ancla;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class AnclaValueEventListener implements ValueEventListener {
    public static String ANCLA_USER_ID = "0000";
    private static String ANCLA_INIT_EVENT = "000001";
    public static String ANCLA_USER_EVENT = ANCLA_USER_ID + ANCLA_INIT_EVENT;

    private GoogleMap mMap;
    private volatile boolean isMapInitialized;
    private FloodEventContainer events;

    AnclaValueEventListener() {
        events =  new FloodEventContainer();
    }

    public void setmMap(GoogleMap mMap) {
        this.mMap = mMap;

        if(!isMapInitialized) {
            Log.d(MainActivity.LOG_ANCLA_TAG, "refreshing new map");
            refreshMap();
        }
    }

    public void updateEvents(FloodEventContainer eventContainer) {
        Log.d(MainActivity.LOG_ANCLA_TAG, "Updating ("
        + events.getEvents().values().size() + ") events");
        for(FloodEvent event:eventContainer.getIterable()) {
            events.addEvent(event.getId(), event);
        }

        refreshMap();
    }

    private void refreshMap() {

        if(mMap == null ){
            Log.d(MainActivity.LOG_ANCLA_TAG, "Map is not set");
            return;
        }

        if(!isMapInitialized) {
            isMapInitialized = true;
        }

        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        LatLngBounds bounds;
        CameraUpdate cu;
        int padding = 50;


        if(events.getEvents().values().size() > 0) {
            for (FloodEvent event : events.getIterable()) {
                if(event.getId().compareTo(AnclaValueEventListener.ANCLA_USER_ID) == 0
                        || event.getStatus() != FloodEvent.ANCLA_EVENT_SAFE) {
                    Log.d(MainActivity.LOG_ANCLA_TAG, "Adding marker: " + event);
                    LatLng eventMarker = new LatLng(event.getLatitude(), event.getLongitude());
                    mMap.addMarker(new MarkerOptions().position(eventMarker).title("Adding marker"));
                    builder.include(eventMarker);
                }
            }
            bounds = builder.build();
            cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);

            mMap.animateCamera(cu);
        } else {
            Log.d(MainActivity.LOG_ANCLA_TAG, "Nothing to update in the map");
        }

    }

    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        Log.d(MainActivity.LOG_ANCLA_TAG, "MapActivity events");

        if (!events.parseDataSnapshot(dataSnapshot)) {
            Log.d(MainActivity.LOG_ANCLA_TAG, "DataSnapshot parsing failed");
        } else {
            Log.d(MainActivity.LOG_ANCLA_TAG, "DataSnapshot parsing successful");
            refreshMap();
        }
    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {

    }
}
