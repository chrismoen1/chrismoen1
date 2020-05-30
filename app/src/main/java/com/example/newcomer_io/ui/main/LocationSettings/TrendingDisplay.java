package com.example.newcomer_io.ui.main.LocationSettings;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.newcomer_io.R;
import com.example.newcomer_io.ui.main.GroupTiming.CreateStudyGroup;
import com.example.newcomer_io.ui.main.UserDetails.UserData;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

//This is the intent that displays all information relevant to the type of bars that can be selected
public class TrendingDisplay extends AppCompatActivity {
    private String googleBrowserKEY = "AIzaSyAjGcF4XC-OEVJHKPmPefDUxGjxiSCbFK8";
    private int num_pressed;
    private ImageButton up;

    private static final int MY_PERMISSION_REQUEST_FINE_LOCATION = 101 ;
    private static final int MY_PERMISSION_REQUEST_COARSE_LOCATION= 102;

    private static final int REQUEST_CODE_ASK_PERMISSIONS = 1001 ;
    private static final int PROXIMITY_RADIUS = 25;

    private LinearLayout scrollView;
    private String pageToken;
    private FloatingActionButton chooseLocation;

    private CheckBox nightClubs;
    private CheckBox restaurants;
    private CheckBox bars;

    private ArrayList<ContentNode> viewList;
    private UserData userData;
    private ArrayList<TrendingContent> trendingContentArray_Initial;
    private PlacesClient placesClient;
    private Location currLocation;
    private FusedLocationProviderClient fusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trending_display);

        scrollView = findViewById(R.id.scrollLayout);
        userData = (UserData) getApplicationContext();

        getUserCurrentLocation();
        Places.initialize(getApplicationContext(), googleBrowserKEY);
        placesClient = Places.createClient(getApplicationContext());
        pageToken = ""; //Set the global variable for the page token response .

        final UserData userData= (UserData) getApplicationContext();
        trendingContentArray_Initial = userData.getTrendingContentArray();
        viewList = new ArrayList<ContentNode>();

        //displayTrendingContent(trendingContentArray_Initial, viewList);

        //We set the checkbox listeners so that if there is a change to one of them then we will go toward

    }
    private void getUserCurrentLocation() {
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
                        setCurrLocation(location);
                        userData.setLocation(location);

                        //userData.setLocation(currLocation);
                        //Get trending locations

                        requestTrendingLocations(getCurrLocation(),PROXIMITY_RADIUS,"night_club","");
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
    private void displayTrendingContent(final ArrayList<TrendingContent> trendingContentArray, final ArrayList<ContentNode> viewList){

        //scrollView.removeAllViews();
        if (trendingContentArray.size() != 0) {
            for (int i = 0; i < trendingContentArray.size(); i++) {
                //First get all of the data associarted with this indx
                final TrendingContent trendingContent = trendingContentArray.get(i);
                String photoReference = trendingContent.getPhotoReference();
                String placeName = trendingContent.getPlaceName();
                float rating = trendingContent.getRating();

                ContentNode contentNode = new ContentNode(rating, placeName, i);
                //We want to insert the image

                getPhotoRequest(photoReference, contentNode, trendingContent); //call the photo reference API
                View content_view = contentNode.getView();
                trendingContent.setContentView(content_view);

                //Now we want to insert additional paramaters

                scrollView.addView(content_view);
                final ConstraintLayout constraintView = contentNode.getConstraintView();
                constraintView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        //Then we will show that the indiviaul icon has a border around it
                        constraintView.setBackground(getResources().getDrawable(R.drawable.border_card));
                        //we need to get the content node based on the user's click so that we can unclick the other nodes
                        int id = constraintView.getId();
                        uncheckOtherIds(id, viewList); //Uncheck the other IDs that aren't relevant to the ONE WE JUSET CLICKECD
                        //Here is where we go tot he next place
                        //Based ont eh ID, then we have the chosen location to be used for the user's event
                        TrendingContent trendingContent1 = getTrendingContent(trendingContentArray, id);


                        userData.getEventCreate().setLocationName(trendingContent1.getPlaceName());
                        userData.getEventCreate().setPhoto(trendingContent1.getPhoto());
                        userData.getEventCreate().setEventlocation(trendingContent1.getLocation());

                        Intent intent = new Intent(TrendingDisplay.this, CreateStudyGroup.class);
                        startActivity(intent);
                        //chooseLocation.color

                    }
                });
                viewList.add(contentNode);
            }
        }
    }

    private TrendingContent getTrendingContent(ArrayList<TrendingContent> contentArrayList,int id){

        for (int i =0 ; i < contentArrayList.size();i++){
            TrendingContent trendingContent = contentArrayList.get(i);
            if (trendingContent.getContentView().getId() == id){
                return trendingContent;
            }
        }
        return null;
    }

    private void uncheckOtherIds(int id, ArrayList<ContentNode> viewList) {
        for (int i = 0; i < viewList.size();i++){
            ContentNode contentNode = viewList.get(i);
            ConstraintLayout constraintView = contentNode.getConstraintView();

            int id1 = constraintView.getId();

            if (id1 != id){
                //Set thhe
                constraintView.setBackground(null);
            }
        }
    }

    private void removeTrendingContent(ArrayList<ContentNode> viewList){
        for (int i = 0; i < viewList.size();i++){
            scrollView.removeView(viewList.get(i).getView());
        }
    }

    public void requestTrendingLocations(Location currLocation, int PROXIMITY_RADIUS,  String keyword,String skipToken) {

        //This function will get all of the list of trending locations based on the API call
        int PLACE_PICKER_REQUEST = 1;
        //we will want to send the request to the Google backend database


        StringBuilder googlePlacesUrl =
                new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        googlePlacesUrl.append("location=").append(currLocation.getLatitude()).append(",").append(currLocation.getLongitude());
        googlePlacesUrl.append("&radius=").append(PROXIMITY_RADIUS*1000);
        googlePlacesUrl.append("&types=").append(keyword);
        googlePlacesUrl.append("&key=" + googleBrowserKEY);

        //Request a string response from the URL resource
        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, googlePlacesUrl.toString(),null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Display the response string.
                        String t= ";alsdkfj";
                        ArrayList<TrendingContent> trendingContentArray  = new ArrayList<TrendingContent>();

                        try {
                            JSONArray results = response.getJSONArray("results");
                            try{
                                pageToken =  response.getString("next_page_token");
                            }
                            catch (JSONException e){
                                pageToken = "";
                            }

                            for (int i =0; i < results.length();i++) {
                                JSONObject objectInArray = results.getJSONObject(i);

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
                                    if (priceLev != -1 && rating != -1){
                                        LatLng place_location = new LatLng(lat,lon);
                                        Location userLocation = userData.getLocation();
                                        TrendingContent trendingContent = new TrendingContent(place_location, name, priceLev, rating, photo_reference, isOpen);

                                        trendingContent.setDistanceBetweenLocation(place_location,new LatLng(userLocation.getLatitude(),userLocation.getLongitude()),getApplicationContext());
                                        trendingContentArray.add(trendingContent);
                                    }

                                }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        displayTrendingContent(trendingContentArray,viewList);

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

    private void getPhotoRequest(final String photoReference, final ContentNode contentNode, final TrendingContent trendingContent) {
        //https://maps.googleapis.com/maps/api/place/photo?photoreference=PHOTO_REFERENCE&sensor=false&maxheight=MAX_HEIGHT&maxwidth=MAX_WIDTH&key=YOUR_API_KEY
        String type = "bar";
        StringBuilder googlePlacesUrl =
                new StringBuilder("https://maps.googleapis.com/maps/api/place/photo?photoreference=");
        googlePlacesUrl.append(photoReference);
        googlePlacesUrl.append("&sensor=false&maxheight=");
        googlePlacesUrl.append("1600");
        googlePlacesUrl.append("&maxwidth=");
        googlePlacesUrl.append("1600");
        googlePlacesUrl.append("&key=");
        googlePlacesUrl.append(googleBrowserKEY);

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

        ImageRequest ir = new ImageRequest(googlePlacesUrl.toString(),
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap response) {
                        //trendingContent.setPhoto(response);
                        contentNode.setImage(response);
                        trendingContent.addImage(response);
                        //We also need to set the Trending Content type as well

                        //with this response, then we will want to send the iage back in

                    }
                }, 1600, 1600, Bitmap.Config.RGB_565, new Response.ErrorListener() {
            public void onErrorResponse(VolleyError error) {
            }
        });

        requestQueue.add(ir);

    }

    public PlacesClient getPlacesClient() {
        return placesClient;
    }

    public void setPlacesClient(PlacesClient placesClient) {
        this.placesClient = placesClient;
    }
    private void setCurrLocation(Location currLocation){this.currLocation = currLocation;}
    private Location getCurrLocation(){return this.currLocation; }

    public class ContentNode{

        //This content node represents each card view that is displayed to the user and acts as the container to hold all of the data
        private View trending_content;

        private TextView title;
        private RatingBar stars;
        private ImageView image;
        private ConstraintLayout cardr;
        private int ID;

        public ContentNode(float rating, String name, int ID ){
            LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(getApplicationContext().LAYOUT_INFLATER_SERVICE);

            trending_content = inflater.inflate(R.layout.trending_content, null);
            stars = trending_content.findViewById(R.id.rating);
            this.image = trending_content.findViewById(R.id.imageView7);
            this.stars = trending_content.findViewById(R.id.rating);
            this.title = trending_content.findViewById(R.id.textView8);
            this.ID = ID;
            this.cardr = trending_content.findViewById(R.id.constraintLayout);
            cardr.setId(ID);
            trending_content.setId(ID);
            setParams(rating, name);

        }
        public ConstraintLayout getConstraintView(){
            return this.cardr;
        }

        public View getView(){
            return trending_content;
        }

        private void setParams(float rating, String name) {
            this.stars.setRating(rating);
            this.title.setText(name);
        }

        public RatingBar getStars() {
            return stars;
        }

        public void setStars(RatingBar stars) {
            this.stars = stars;
        }

        public int getID() {
            return ID;
        }

        public void setID(int ID) {
            this.ID = ID;
        }

        public ImageView getImage() {
            return image;
        }

        public void setImage(Bitmap image) {
            this.image.setImageBitmap(image);
        }
    }
}