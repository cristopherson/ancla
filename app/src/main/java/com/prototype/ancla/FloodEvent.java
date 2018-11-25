package com.prototype.ancla;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;

public class FloodEvent {
    public static String DATASNAPSHOT_CHILD_STATUS="status";
    public static String DATASNAPSHOT_CHILD_LATITUDE="latitude";
    public static String DATASNAPSHOT_CHILD_LONGITUDE="longitude";

    private String status;
    private String latitude;
    private String longitude;

    public FloodEvent(String latitude, String longitude, String status) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.status = status;
    }

    public FloodEvent() {
        this("","","");
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean parseDataSnapshot(DataSnapshot dataSnapshot){
        boolean successfulParsing = true;

        if(dataSnapshot.hasChildren()) {
            for (DataSnapshot children:dataSnapshot.getChildren()) {
                Log.d(MainActivity.LOG_ANCLA_TAG,"" + children.getValue());
                if(children.hasChild(DATASNAPSHOT_CHILD_LATITUDE)) {
                    DataSnapshot latitudeData = null;

                    latitudeData = children.child(DATASNAPSHOT_CHILD_LATITUDE);
                    Log.d(MainActivity.LOG_ANCLA_TAG,"" + latitudeData);

                    if(latitudeData != null) {
                        latitude = (String) latitudeData.getValue();
                    } else {
                        successfulParsing = false;
                    }
                } else {
                    successfulParsing = false;
                }
                if(children.hasChild(DATASNAPSHOT_CHILD_LONGITUDE)) {
                    DataSnapshot longitudeData = null;

                    longitudeData = children.child(DATASNAPSHOT_CHILD_LONGITUDE);
                    Log.d(MainActivity.LOG_ANCLA_TAG,"" + longitudeData);

                    if(longitudeData != null) {
                        longitude = (String)longitudeData.getValue();
                    } else {
                        successfulParsing = false;
                    }
                } else {
                    successfulParsing = false;
                }
                if(children.hasChild(DATASNAPSHOT_CHILD_STATUS)) {
                    DataSnapshot statusData = null;

                    statusData = children.child(DATASNAPSHOT_CHILD_STATUS);
                    Log.d(MainActivity.LOG_ANCLA_TAG,"" + statusData);

                    if(statusData != null) {
                        status = (String)statusData.getValue();
                    } else {
                        successfulParsing = false;
                    }
                } else {
                    successfulParsing = false;
                }
            }
        } else {
            successfulParsing = false;
        }

        Log.d(MainActivity.LOG_ANCLA_TAG, this.toString());
        return successfulParsing;
    }

    public String toString() {
        return status + "," + longitude + "," + latitude;
    }
}
