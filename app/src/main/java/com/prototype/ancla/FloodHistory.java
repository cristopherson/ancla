package com.prototype.ancla;

import android.graphics.Color;
import android.os.Parcel;
import android.os.Parcelable;

public class FloodHistory implements Parcelable {
    public static String ANCLA_PARCELABLE_HISTORY_ID = "ANCLA_PARCELABLE_HISTORY_ID";

    public static String ANCLA_STRING_DANGER_EVENT = "Zona de Peligro";
    public static String ANCLA_STRING_WARNING_EVENT = "Zona de Precaucion";
    public static String ANCLA_STRING_SAFE_EVENT = "Zona es Segura";
    public static String ANCLA_STRING_UNKNOWN_EVENT = "Evento desconocido";

    private String eventType;
    private String date;
    private String location;


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public FloodHistory(String eventType, String date, String location) {
        this.eventType = eventType;
        this.date = date;
        this.location = location;
    }

    public FloodHistory() {
        this("","","");
    }

    protected FloodHistory(Parcel in) {
        this.eventType = in.readString();
        this.date = in.readString();
        this.location = in.readString();
    }

    public static final Creator<FloodHistory> CREATOR = new Creator<FloodHistory>() {
        @Override
        public FloodHistory createFromParcel(Parcel in) {
            return new FloodHistory(in);
        }

        @Override
        public FloodHistory[] newArray(int size) {
            return new FloodHistory[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.eventType);
        dest.writeString(this.date);
        dest.writeString(this.location);
    }

    public String toString() {
        return this.eventType + "," + this.location + "," + this.date;
    }

    public static int getColor(String message) {
        if(message.compareTo(FloodHistory.ANCLA_STRING_SAFE_EVENT)==0) {
            return Color.GRAY;
        }
        if(message.compareTo(FloodHistory.ANCLA_STRING_DANGER_EVENT)==0) {
            return Color.RED;
        }
        if(message.compareTo(FloodHistory.ANCLA_STRING_WARNING_EVENT)==0) {
            return Color.YELLOW;
        }

        return Color.WHITE;
    }
}
