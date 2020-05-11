package com.example.newcomer_io.ui.main.LocationSettings;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.model.LatLng;
import org.json.JSONException;
import org.json.JSONObject;

public class TrendingContent {
    private LatLng location;
    private String placeName;
    private int priceLevel;
    private float rating;
    private String photoReference;
    private View contentView;
    private boolean isOpen;
    private String API_KEY = "AIzaSyAjGcF4XC-OEVJHKPmPefDUxGjxiSCbFK8";
    private Response.Listener<ImageRequest> context;
    private String pageToken;
    private Bitmap photo;

    public TrendingContent(LatLng location, String placeName, int priceLevel, float rating, String photoReference, boolean isOpen) {
        this.location = location;
        this.placeName = placeName;
        this.priceLevel = priceLevel;
        this.rating = rating;
        this.photoReference = photoReference;
        this.isOpen = isOpen;
        this.context = context;
        this.pageToken = "";
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

    public void setDistanceBetweenLocation(LatLng place_location, LatLng latLng, Context applicationContext) {

        //origins=41.43206,-81.38992|-33.86748,151.20699
        StringBuilder googlePlacesUrl =
                new StringBuilder("https://maps.googleapis.com/maps/api/distancematrix/json?");
        googlePlacesUrl.append("origins=").append(String.valueOf(place_location.latitude) + "," + String.valueOf(place_location.longitude)).append("&destinations=").append(String.valueOf(latLng.latitude) + "," + String.valueOf(latLng.longitude));
        googlePlacesUrl.append("&key=").append(API_KEY);

        //Request a string response from the URL resource
        final JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, googlePlacesUrl.toString(), null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Display the response string.
                        String responsee = "";
                        try {
                            JSONObject destination_addresses = response.getJSONObject("rows");

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String ergror = "";
            }
        });

        //Instantiate the RequestQueue and add the request to the queue
        RequestQueue queue = Volley.newRequestQueue(applicationContext);
        queue.add(objectRequest);
    }
    public void setPageToken(String pageToken){
        this.pageToken = pageToken;
    }
    public void setContentView(View content){
        this.contentView = content;
    }

    public View getContentView() {
        return contentView;
    }

    public void addImage(Bitmap response) {
        this.photo = response;
    }

}
