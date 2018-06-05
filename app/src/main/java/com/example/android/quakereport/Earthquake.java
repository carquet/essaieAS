package com.example.android.quakereport;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class Earthquake {

    private double mMagnitude;
    private String mLocation;
    private long mTimeInMilliseconds;
    private String mUrl;

    public Earthquake(double magnitude, String location, Long time, String url){
        mMagnitude = magnitude;
        mLocation = location;
        mTimeInMilliseconds = time;
        mUrl = url;
    }

    public double getmMagnitude() {
        return mMagnitude;
    }

    public String getmLocation() {
        return mLocation;
    }

    public Long getmTime() {

         return mTimeInMilliseconds;
    }

    public String getmUrl() {
        return mUrl;
    }
}
