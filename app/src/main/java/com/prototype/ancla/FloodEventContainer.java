package com.prototype.ancla;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class FloodEventContainer implements Parcelable{
    private HashMap<String, FloodEvent> events =  new HashMap<>();

    public FloodEventContainer(HashMap<String, FloodEvent> events) {
        if(events == null) {
            throw new NullPointerException();
        }
        this.events = events;
    }

    public FloodEventContainer() {
        this(new HashMap<String, FloodEvent>());
    }

    protected FloodEventContainer(Parcel in) {
        List<FloodEvent> eventList = new ArrayList<FloodEvent>();

        in.readList(eventList, FloodEvent.class.getClassLoader());

        Log.d(MainActivity.LOG_ANCLA_TAG, "Reading ("
                + eventList.size() + ") events");

        for(FloodEvent event:eventList) {
            events.put(event.getId(), event);
        }
    }

    public void setEvents(HashMap<String, FloodEvent> events) {
        if(events == null) {
            throw new NullPointerException();
        }
        this.events = events;
    }

    public HashMap<String, FloodEvent> getEvents() {
        return events;
    }

    public FloodEvent getEventByKey(String key) {
        return events.get(key);
    }

    public void addEvent(String key, FloodEvent floodEvent) {
        if(floodEvent == null || key == null) {
            throw new NullPointerException();
        }
        events.put(key, floodEvent);
    }

    public boolean containsKey(String key) {
        return events.containsKey(key);
    }

    public Iterable<FloodEvent> getIterable() {
        return events.values();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        List<FloodEvent> list = new ArrayList<FloodEvent>();

        Log.d(MainActivity.LOG_ANCLA_TAG, "Writing ("
            + events.values().size() + ") events");

        for (FloodEvent event:events.values()) {
            list.add(event);
        }
        parcel.writeList(list);
    }

    public static final Creator<FloodEventContainer> CREATOR = new Creator<FloodEventContainer>() {
        @Override
        public FloodEventContainer createFromParcel(Parcel in) {
            return new FloodEventContainer(in);
        }

        @Override
        public FloodEventContainer[] newArray(int size) {
            return new FloodEventContainer[size];
        }
    };
}
