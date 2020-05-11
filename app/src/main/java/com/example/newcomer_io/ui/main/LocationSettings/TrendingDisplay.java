package com.example.newcomer_io.ui.main.LocationSettings;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Location;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.newcomer_io.R;
import com.example.newcomer_io.ui.main.GroupTiming.CreateGroup;
import com.example.newcomer_io.ui.main.UserDetails.UserData;
import com.google.android.gms.maps.model.LatLng;
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
    private LinearLayout scrollView;
    private FilterResults filterResults;
    private String pageToken;
    private FloatingActionButton chooseLocation;

    private CheckBox nightClubs;
    private CheckBox restaurants;
    private CheckBox bars;

    private ArrayList<ContentNode> viewList;
    private UserData userData;
    private ArrayList<TrendingContent> trendingContentArray_Initial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trending_display);

        scrollView = findViewById(R.id.scrollLayout);
        userData = (UserData) getApplicationContext();

        nightClubs = findViewById(R.id.nightclubs);
        restaurants = findViewById(R.id.restaurants);

        bars = findViewById(R.id.bars);

        pageToken = ""; //Set the global variable for the page token response .

        filterResults = new FilterResults(this);
        filterResults.setApplyChangesBackground(false);

        final UserData userData= (UserData) getApplicationContext();
        trendingContentArray_Initial = userData.getTrendingContentArray();
        viewList = new ArrayList<ContentNode>();

        LinearLayout holder = findViewById(R.id.linL);

        displayTrendingContent(trendingContentArray_Initial, viewList);

        filterResults.getRange().setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                //Then we update the locatiion paramater
                filterResults.setDistance(progress);
                filterResults.setApplyChangesBackground(true);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        nightClubs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                restaurants.setChecked(false);
                bars.setChecked(false);
                filterResults.setApplyChangesBackground(true);

            }
        });
        restaurants.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nightClubs.setChecked(false);
                bars.setChecked(false);
                filterResults.setApplyChangesBackground(true);
            }
        });
        bars.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                restaurants.setChecked(false);
                nightClubs.setChecked(false);
                filterResults.setApplyChangesBackground(true);
            }
        });

        //We set the checkbox listeners so that if there is a change to one of them then we will go toward
        filterResults.getApplyChanges().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int range = filterResults.getRange().getProgress();
                filterResults.setApplyChangesBackground(false);
                removeTrendingContent(viewList);
                requestTrendingLocations(userData.getLocation(),range, getCurrentKeyWords(),"");

                //Apply the changes by making the call to the API
            }
        });

    }


    private String getCurrentKeyWords(){
        //Now if there is a change then we will apply a google reques
        //Call the response
        boolean nightclubs = filterResults.getNightClubs().isChecked();
        boolean restaurants = filterResults.getRestaurants().isChecked();
        boolean bars = filterResults.getBars().isChecked();

        String keyWords = "";
        if (nightclubs == true){
            keyWords = "night_club";
        }
        else if (restaurants == true){
            keyWords = "restaurant";
        }
        else if (bars == true){
            keyWords = "bar";
        }
        return keyWords;
    }
    private void displayTrendingContent(final ArrayList<TrendingContent> trendingContentArray, final ArrayList<ContentNode> viewList){

        //scrollView.removeAllViews();
        if (trendingContentArray.size() != 0) {
            for (int i = 0; i < trendingContentArray.size(); i++) {
                //First get all of the data associarted with this indx
                TrendingContent trendingContent = trendingContentArray.get(i);
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

                        userData.setChosenLocation(trendingContent1);
                        Intent intent = new Intent(TrendingDisplay.this, CreateGroup.class);
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

    private void requestTrendingLocations(Location currLocation, int PROXIMITY_RADIUS,  String keyword,String skipToken) {

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
    public class FilterResults extends Activity {
        private CheckBox nightClubs;
        private CheckBox restaurants;
        private CheckBox bars;

        private Button applyChanges;
        private SeekBar range;
        private TextView distanceParam;


        public FilterResults(Activity activity){

            this.nightClubs = activity.findViewById(R.id.nightclubs);
            this.restaurants = activity.findViewById(R.id.restaurants);
            this.bars = activity.findViewById(R.id.bars);
            this.applyChanges = activity.findViewById(R.id.applyChanges);
            this.range = activity.findViewById(R.id.range);
            this.distanceParam = activity.findViewById(R.id.distance);

            range.setProgress(5);
            setDistance(5);

            //Hard set values for min/max values of the distance range
            this.range.setMin(1);
            this.range.setMax(25);
        }

        public void setApplyChangesBackground(boolean toUpdate){
            if (toUpdate == true){
                this.applyChanges.setEnabled(toUpdate);
                //this.applyChanges.setBackgroundColor(applyChanges.getContext().getResources().getColor(R.color.DeepSkyBlue));
            }
            else{
                this.applyChanges.setEnabled(toUpdate);
                //this.applyChanges.setBackgroundColor(applyChanges.getContext().getResources().getColor(R.color.LightGrey));
            }
        }
        public SeekBar getRange(){return this.range;}

        public Button getApplyChanges(){return this.applyChanges;}

        public CheckBox getNightClubs() {
            return nightClubs;
        }

        public void setNightClubs(CheckBox nightClubs) {
            this.nightClubs = nightClubs;
        }

        public CheckBox getRestaurants() {
            return restaurants;
        }

        public void setRestaurants(CheckBox restaurants) {
            this.restaurants = restaurants;
        }

        public CheckBox getBars() {
            return bars;
        }

        public void setBars(CheckBox bars) {
            this.bars = bars;
        }

        public TextView getDistanceParam() {
            return distanceParam;
        }

        public void setDistanceParam(TextView distanceParam) {
            this.distanceParam = distanceParam;
        }

        public void setDistance(int distance){
            this.distanceParam.setText(String.valueOf(distance) + " km");
        }

    }
}