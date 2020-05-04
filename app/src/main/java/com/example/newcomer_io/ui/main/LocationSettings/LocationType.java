package com.example.newcomer_io.ui.main.LocationSettings;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.newcomer_io.R;
import com.example.newcomer_io.ui.main.UserDetails.UserData;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.net.PlacesClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class LocationType extends AppCompatActivity {

    private static final int MY_PERMISSION_REQUEST_FINE_LOCATION = 101 ;
    private static final int MY_PERMISSION_REQUEST_COARSE_LOCATION= 102;

    private static final int REQUEST_CODE_ASK_PERMISSIONS = 1001 ;
    private static final int PROXIMITY_RADIUS = 5000;

    private Location currLocation;

    private LinearLayout scrollHorizontal;

    private PlacesClient placesClient;
    private String googleBrowserKEY = "AIzaSyAjGcF4XC-OEVJHKPmPefDUxGjxiSCbFK8";
    private UserData userData;



    private FusedLocationProviderClient fusedLocationClient;

    int AUTOCOMPLETE_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_type);

        scrollHorizontal = findViewById(R.id.scrollHorizontal);
        userData = (UserData) getApplicationContext();

        Places.initialize(getApplicationContext(), googleBrowserKEY);
        placesClient = Places.createClient(getApplicationContext());

        //userData = (UserData) getApplicationContext();

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        LocationNode customPlace = new LocationNode(this,1);
        LocationNode trendingPlaces = new LocationNode(this,2);

        View trending_View = trendingPlaces.getView();
        View customPlace_View = customPlace.getView();

        scrollHorizontal.addView(customPlace_View,params);
        scrollHorizontal.addView(trending_View,params);

        setClickListener_Trending(customPlace_View);

    }

    private void setClickListener_Trending(View trending) {
        ImageView pic = trending.findViewById(R.id.imageView9);
        TextView description = trending.findViewById(R.id.description);
        pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCurrentLocation();
            }
        });
        description.setOnClickListener(new View.OnClickListener() {
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
                            userData.setLocation(location);

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
        final String type = "night_club";

        StringBuilder googlePlacesUrl =
                new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        googlePlacesUrl.append("location=").append(currLocation.getLatitude()).append(",").append(currLocation.getLongitude());
        googlePlacesUrl.append("&radius=").append(PROXIMITY_RADIUS);
        googlePlacesUrl.append("&types=").append(type);
        googlePlacesUrl.append("&key=" + googleBrowserKEY);

        //Request a string response from the URL resource
        final JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, googlePlacesUrl.toString(),null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Display the response string.

                        ArrayList<TrendingContent> trendingContentArray  = new ArrayList<TrendingContent>();

                        try {
                             JSONArray results = response.getJSONArray("results");
                             for (int i =0; i < results.length();i++){
                                 JSONObject objectInArray = results.getJSONObject(i);


                                 JSONArray types1 = objectInArray.getJSONArray("types");

                                 if (existsArray(types1, type)== true) {


                                     //Now we want to populate some of the data pertaining to the

                                     double lat = objectInArray.getJSONObject("geometry").getJSONObject("location").getDouble("lat");
                                     double lon = objectInArray.getJSONObject("geometry").getJSONObject("location").getDouble("lng");

                                     String name = objectInArray.getString("name");

                                     //-----------------------------------------Accounting for errors-----------------------------------------------------//
                                     boolean isOpen;
                                     try {
                                         isOpen = objectInArray.getJSONObject("opening_hours").getBoolean("open_now");

                                     } catch (Exception e) {
                                         isOpen = false;
                                     }

                                     int priceLev;
                                     try {
                                         priceLev = objectInArray.getInt("price_level");

                                     } catch (Exception e) {
                                         priceLev = -1;
                                     }
                                     String streetName;
                                     try{
                                         streetName = objectInArray.getString("vicinity");
                                     }
                                     catch (Exception e) {
                                     streetName = "";
                                     }

                                     float rating;
                                     try {
                                         rating = (float) objectInArray.getDouble("rating");
                                     } catch (Exception e) {
                                         rating = -1; //Invalid rating
                                     }

                                     String photo_reference;
                                     try {
                                         photo_reference = objectInArray.getJSONArray("photos").getJSONObject(0).getString("photo_reference");
                                     } catch (Exception e) {
                                         photo_reference = ""; //aka specify that it is an error
                                     }

                                     if (rating != -1 && priceLev != -1)
                                     {
                                         TrendingContent trendingContent = new TrendingContent(new LatLng(lat, lon), name, priceLev, rating, photo_reference, isOpen);
                                         trendingContentArray.add(trendingContent);
                                     }
                                 }

                                //now that we have all of the data, then we can go to the next screen in order to show all of this data in kind of like filter scren showing of the data

                             }
                             Intent intent = new Intent(LocationType.this, TrendingDisplay.class);
                             //intent.putExtra("TrendingContent", trendingContentArray);

                            userData.setTrendingContent(trendingContentArray);

                            startActivity(intent);

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

    private boolean existsArray(JSONArray types1, String type) {
        for (int i =0; i < types1.length();i++){
            try {
                if (types1.getString(i).equals(type) == true){
                    return true;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;

    }


}


