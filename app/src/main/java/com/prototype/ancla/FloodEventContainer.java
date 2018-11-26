package com.prototype.ancla;

import android.os.Parcel;
import android.os.Parcelable;
import android.provider.ContactsContract;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FloodEventContainer implements Parcelable{
    private HashMap<String, FloodEvent> events =  new HashMap<>();
    private String newMessageKey = "";

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

    public boolean parseDataSnapshot(DataSnapshot dataSnapshot){
        boolean successfulParsing = true;

        if(dataSnapshot.hasChildren()) {
            for (DataSnapshot children:dataSnapshot.getChildren()) {
                boolean successElement = false;
                FloodEvent floodEvent = new FloodEvent();

                floodEvent.setId(children.getKey());

                Log.d(MainActivity.LOG_ANCLA_TAG, "id = " + floodEvent.getId());
                Log.d(MainActivity.LOG_ANCLA_TAG,"content = " + children.getValue());

                if(children.hasChild(FloodEvent.DATASNAPSHOT_CHILD_EVENT)) {
                    DataSnapshot eventData = null;

                    eventData = children.child(FloodEvent.DATASNAPSHOT_CHILD_EVENT);
                    Log.d(MainActivity.LOG_ANCLA_TAG, ""+ eventData);

                    if (eventData != null) {
                        try {
                            floodEvent.setEvent((String)eventData.getValue());
                            successElement = true;
                        } catch (Exception e) {
                            Log.d(MainActivity.LOG_ANCLA_TAG, e.getMessage());
                        }
                    }

                }
                if(successElement && children.hasChild(FloodEvent.DATASNAPSHOT_CHILD_LATITUDE)) {
                    successElement = false;
                    DataSnapshot latitudeData = null;

                    latitudeData = children.child(FloodEvent.DATASNAPSHOT_CHILD_LATITUDE);
                    Log.d(MainActivity.LOG_ANCLA_TAG, "" + latitudeData);

                    if (latitudeData != null) {
                        try {
                            floodEvent.setLatitude(Double.valueOf((String) latitudeData.getValue()));
                            successElement = true;
                        } catch (Exception e) {
                            Log.d(MainActivity.LOG_ANCLA_TAG, e.getMessage());
                        }

                    }
                }
                if(successElement && children.hasChild(FloodEvent.DATASNAPSHOT_CHILD_LONGITUDE)) {
                    DataSnapshot longitudeData = null;
                    successElement = false;

                    longitudeData = children.child(FloodEvent.DATASNAPSHOT_CHILD_LONGITUDE);
                    Log.d(MainActivity.LOG_ANCLA_TAG,"" + longitudeData);

                    if(longitudeData != null) {
                        try {
                            floodEvent.setLongitude(Double.valueOf((String)longitudeData.getValue()));
                            successElement = true;
                        } catch(Exception e) {
                            Log.d(MainActivity.LOG_ANCLA_TAG, e.getMessage());
                        }
                    }
                }
                if(successElement && children.hasChild(FloodEvent.DATASNAPSHOT_CHILD_STATUS)) {
                    DataSnapshot statusData = null;
                    successElement = false;

                    statusData = children.child(FloodEvent.DATASNAPSHOT_CHILD_STATUS);
                    Log.d(MainActivity.LOG_ANCLA_TAG,"" + statusData);

                    if(statusData != null) {
                        try {
                            floodEvent.setStatus(Integer.valueOf((String)statusData.getValue()));
                            successElement = true;
                        } catch(Exception e) {
                            Log.d(MainActivity.LOG_ANCLA_TAG, e.getMessage());
                        }
                    }
                }

                if(successElement) {
                    if (events.containsKey(floodEvent.getId())) {
                        if(events.get(floodEvent.getId()).getStatus() != floodEvent.getStatus()) {
                            Log.d(MainActivity.LOG_ANCLA_TAG, "New event " + floodEvent.toString());
                            newMessageKey = floodEvent.getId();
                        }
                    }
                    events.put(floodEvent.getId(), floodEvent);
                } else {
                    successfulParsing = false;
                }

            }
        }

        return successfulParsing;
    }

    public String getNewEventMessage() {
        FloodEvent floodEvent = events.get(newMessageKey);
        if(floodEvent != null) {
            return floodEvent.getEventMessage();
        }

        return "";
    }

    public String getNewEventLocation() {
        FloodEvent floodEvent = events.get(newMessageKey);
        if(floodEvent != null) {
            return "" + floodEvent.getLatitude() + "," + floodEvent.getLongitude();
        }

        return "";
    }

}
