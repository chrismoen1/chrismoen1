package com.example.newcomer_io.ui.main;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.newcomer_io.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

public class LocationType extends AppCompatActivity {

    private static final int MY_PERMISSION_REQUEST_FINE_LOCATION = 101 ;
    private static final int MY_PERMISSION_REQUEST_COARSE_LOCATION= 102;

    private static final int REQUEST_CODE_ASK_PERMISSIONS = 1001 ;
    private static final int PROXIMITY_RADIUS = 5000;

    private Location currLocation;
    private ImageView customLocation;
    private ImageView trendingLocations;
    private PlacesClient placesClient;
    private String googleBrowserKEY = "AIzaSyAjGcF4XC-OEVJHKPmPefDUxGjxiSCbFK8";
    private UserData userData;

    private FusedLocationProviderClient fusedLocationClient;

    int AUTOCOMPLETE_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_type);

        customLocation = findViewById(R.id.imageView12);
        trendingLocations = findViewById(R.id.imageView13);
        Places.initialize(getApplicationContext(), googleBrowserKEY);
        placesClient = Places.createClient(getApplicationContext());
        //userData = (UserData) getApplicationContext();

        customLocation.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                //Set the paramaters as needed

                List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME,Place.Field.LAT_LNG);

                // autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME,Place.Field.LAT_LNG));
                // Start the autocomplete intent.

                Intent intent = new Autocomplete.IntentBuilder(
                        AutocompleteActivityMode.FULLSCREEN, fields)
                        .setTypeFilter(TypeFilter.ADDRESS)
                        .build(v.getContext());
                startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);
            }
        });

        trendingLocations.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
             getCurrentLocation();
            }
        });

    }

    private void getCurrentLocation() {
        LocationManager locationManger = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        boolean permissionAccessCoarseLocationApproved = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
        if (permissionAccessCoarseLocationApproved == true) {
            //boolean backgroundLocationPermissionApproved = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED;
                //Access the location
                fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
                fusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            //Keep the location
                            currLocation = location;
                            //userData.setLocation(currLocation);
                            //Get trending locations

                            requestTrendingLocations(currLocation);
                            //set the location
                        }
                    }
                });

            }
        else{
            ActivityCompat.requestPermissions(this,new String[]{
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_BACKGROUND_LOCATION},REQUEST_CODE_ASK_PERMISSIONS);
            }


        }

    private void requestTrendingLocations(Location currLocation) {
        //This function will get all of the list of trending locations based on the API call
        int PLACE_PICKER_REQUEST = 1;
        //we will want to send the request to the Google backend database
        String type = "bar";
        StringBuilder googlePlacesUrl =
                new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        googlePlacesUrl.append("location=").append(currLocation.getLatitude()).append(",").append(currLocation.getLongitude());
        googlePlacesUrl.append("&radius=").append(PROXIMITY_RADIUS);
        googlePlacesUrl.append("&types=").append(type);
        googlePlacesUrl.append("&key=" + googleBrowserKEY);
        //Request a string response from the URL resource
        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, googlePlacesUrl.toString(),null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Display the response string.
                        String t= ";alsdkfj";
                        try {
                             JSONArray results = response.getJSONArray("results");
                             for (int i =0; i < results.length();i++){
                                 JSONObject objectInArray = results.getJSONObject(i);

                             }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });

        //Instantiate the RequestQueue and add the request to the queue
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(objectRequest);

    }

}


