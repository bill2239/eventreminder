package com.example.karzzi.smartreminder;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;

public class Events implements Serializable {
    public final String title;
    public final String location;
    public final String date;
    public final double latitude;
    public final double longitude;
    //public final LatLng coordinate;

    public Events(String title, String location, String date, double latitude, double longitude) {
        this.title = title;
        this.location = location;
        this.date = date;
        this.latitude = latitude;
        this.longitude = longitude;
        //this.coordinate = coordinate;
    }
}
