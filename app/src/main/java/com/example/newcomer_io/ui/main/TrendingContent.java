package com.example.newcomer_io.ui.main;

import android.app.Activity;
import android.graphics.Bitmap;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.model.LatLng;
import org.json.JSONObject;

public class TrendingContent {
    private LatLng location;
    private String placeName;
    private int priceLevel;
    private float rating;
    private String photoReference;
    private boolean isOpen;
    private String API_KEY = "AIzaSyAjGcF4XC-OEVJHKPmPefDUxGjxiSCbFK8";
    private Response.Listener<ImageRequest> context;

    private Bitmap photo;
    private LatLng currLocation;

    public TrendingContent(LatLng location, String placeName, int priceLevel, float rating, String photoReference, boolean isOpen){
            this.location = location;
            this.placeName = placeName;
            this.priceLevel = priceLevel;
            this.rating= rating;
            this.photoReference = photoReference;
            this.isOpen = isOpen;
            this.context = context;
            //https://maps.googleapis.com/maps/api/place/photo?photoreference=PHOTO_REFERENCE&sensor=false&maxheight=MAX_HEIGHT&maxwidth=MAX_WIDTH&key=YOUR_API_KE
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

    public Bitmap getPhoto() {
        return photo;
    }

    public void setPhoto(Bitmap photo) {
        this.photo = photo;
    }
}
