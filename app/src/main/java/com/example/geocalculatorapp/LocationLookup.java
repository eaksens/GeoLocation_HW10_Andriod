package com.example.geocalculatorapp;

import org.parceler.Parcel;

@Parcel
public class LocationLookup {
    String _key;
    double origLat;
    double origLng;
    double destLat;
    double destLng;

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    String timestamp;

    public String get_key() {
        return _key;
    }

    public void set_key(String _key) {
        this._key = _key;
    }

    public double getOrigLat() {
        return origLat;
    }

    public void setOrigLat(double origLat) {
        this.origLat = origLat;
    }

    public double getOrigLng() {
        return origLng;
    }

    public void setOrigLng(double origLng) {
        this.origLng = origLng;
    }

    public double getDestLat() {
        return destLat;
    }

    public void setDestLat(double destLat) {
        this.destLat = destLat;
    }

    public double getDestLng() {
        return destLng;
    }

    public void setDestLng(double destLng) {
        this.destLng = destLng;
    }
}
