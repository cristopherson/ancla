package com.prototype.ancla;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class FloodHistoryContainer implements Parcelable {

    private List<FloodHistory> historyList = new LinkedList<FloodHistory>();

    public FloodHistoryContainer(List<FloodHistory> historyList) {
        this.historyList = historyList;
    }

    public FloodHistoryContainer(){
        this(new LinkedList<FloodHistory>());
    }

    protected FloodHistoryContainer(Parcel in) {
        in.readList(historyList, FloodHistory.class.getClassLoader());
    }

    public void setHistoryList(List<FloodHistory> historyList) {
        this.historyList = historyList;
    }

    public List<FloodHistory>getHistoryList() {
        return historyList;
    }


    public void addElement(FloodHistory floodHistory) {
        historyList.add(floodHistory);
    }

    public static final Creator<FloodHistoryContainer> CREATOR = new Creator<FloodHistoryContainer>() {
        @Override
        public FloodHistoryContainer createFromParcel(Parcel in) {
            return new FloodHistoryContainer(in);
        }

        @Override
        public FloodHistoryContainer[] newArray(int size) {
            return new FloodHistoryContainer[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(historyList);
    }
}
