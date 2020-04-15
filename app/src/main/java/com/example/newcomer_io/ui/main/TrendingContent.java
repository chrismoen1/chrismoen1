package com.example.newcomer_io.ui.main;

import android.location.Location;
import com.google.android.gms.maps.model.LatLng;

public class TrendingContent {
    private LatLng location;
    private String placeName;
    private int priceLevel;
    private float rating;
    private String photoReference;
    private boolean isOpen;

    public TrendingContent(LatLng location, String placeName,int priceLevel, float rating,String photoReference, boolean isOpen){
            this.location = location;
            this.placeName = placeName;
            this.priceLevel = priceLevel;
            this.rating= rating;
            this.photoReference = photoReference;
            this.isOpen = isOpen;
    }


    public LatLng getLocation() {
        return location;
    }

    public void setLocation(LatLng location) {
        this.location = location;
    }

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public int getPriceLevel() {
        return priceLevel;
    }

    public void setPriceLevel(int priceLevel) {
        this.priceLevel = priceLevel;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getPhotoReference() {
        return photoReference;
    }

    public void setPhotoReference(String photoReference) {
        this.photoReference = photoReference;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }
}
