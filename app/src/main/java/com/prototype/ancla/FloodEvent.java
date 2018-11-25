package com.prototype.ancla;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

public class FloodEvent implements Parcelable {
    public static String DATASNAPSHOT_CHILD_STATUS="status";
    public static String DATASNAPSHOT_CHILD_LATITUDE="latitude";
    public static String DATASNAPSHOT_CHILD_LONGITUDE="longitude";
    public static int ANCLA_EVENT_SAFE = 0;
    public static int ANCLA_EVENT_WARNING = 1;
    public static int ANCLA_EVENT_DANGER = 2;

    private String id;
    private String event;
    private int status;
    private double latitude;
    private double longitude;

    public FloodEvent(String id, String event, int status, double latitude, double longitude) {
        this.id = id;
        this.event = event;
        this.status = status;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public FloodEvent() {
        this("","",0,0.0,0.0);
    }

    protected FloodEvent(Parcel in) {
        id = in.readString();
        event = in.readString();
        status = in.readInt();
        latitude = in.readDouble();
        longitude = in.readDouble();

        Log.d(MainActivity.LOG_ANCLA_TAG, "reading: " +
                this.toString());
    }

    public static final Creator<FloodEvent> CREATOR = new Creator<FloodEvent>() {
        @Override
        public FloodEvent createFromParcel(Parcel in) {
            return new FloodEvent(in);
        }

        @Override
        public FloodEvent[] newArray(int size) {
            return new FloodEvent[size];
        }
    };

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public double getLatitude() {
        return latitude;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String toString() {
        return id + "," + event + "," + status + "," + longitude + "," + latitude;
    }

    public String getEventMessage() {
        if (status == ANCLA_EVENT_SAFE) {
            return "Zona es segura";
        }
        if (status == ANCLA_EVENT_WARNING) {
            return "Zona es insegura";
        }
        if (status == ANCLA_EVENT_DANGER) {
            return "Zona de alto riesgo";
        }
        return "Evento Desconocido";
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        Log.d(MainActivity.LOG_ANCLA_TAG, "Writing: " +
            this.toString());

        parcel.writeString(this.id);
        parcel.writeString(this.event);
        parcel.writeInt(this.status);
        parcel.writeDouble(this.latitude);
        parcel.writeDouble(this.longitude);
    }
}
