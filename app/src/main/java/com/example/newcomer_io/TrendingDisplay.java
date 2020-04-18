package com.example.newcomer_io;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Rating;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.core.widget.NestedScrollView;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.example.newcomer_io.R;
import com.example.newcomer_io.ui.main.TrendingContent;
import com.example.newcomer_io.ui.main.UserData;

import java.util.ArrayList;

//This is the intent that displays all information relevant to the type of bars that can be selected
public class TrendingDisplay extends AppCompatActivity {
    private String googleBrowserKEY = "AIzaSyAjGcF4XC-OEVJHKPmPefDUxGjxiSCbFK8";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trending_display);

        LinearLayout scrollView = findViewById(R.id.scrollLayout);

        UserData userData= (UserData) getApplicationContext();
        ArrayList<TrendingContent> trendingContentArray = userData.getTrendingContentArray();

        for (int i = 0; i < trendingContentArray.size();i++){
            //First get all of the data associarted with this indx
            String photoReference = trendingContentArray.get(i).getPhotoReference();
            String placeName = trendingContentArray.get(i).getPlaceName();
            float rating = trendingContentArray.get(i).getRating();

            ContentNode contentNode = new ContentNode(rating,placeName,i);

            getPhotoRequest(photoReference,contentNode);
            View content_view = contentNode.getView();

            scrollView.addView(content_view);

        }
    }

    private void getPhotoRequest(final String photoReference, final ContentNode contentNode) {
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
                        //with this response, then we will want to send the iage back in

                    }
                }, 1600, 1600, Bitmap.Config.RGB_565, new Response.ErrorListener() {
            public void onErrorResponse(VolleyError error) {
            }
        });

        requestQueue.add(ir);

    }
    public class ContentNode{

        private View trending_content;

        private TextView title;
        private RatingBar stars;
        private ImageView image;
        private int ID;

        public ContentNode(float rating, String name, int ID ){
            LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(getApplicationContext().LAYOUT_INFLATER_SERVICE);

            trending_content = inflater.inflate(R.layout.trending_content, null);
            stars = trending_content.findViewById(R.id.rating);
            this.image = trending_content.findViewById(R.id.imageView7); 
            this.stars = trending_content.findViewById(R.id.rating);
            this.title = trending_content.findViewById(R.id.textView8);
            this.ID = ID;

            trending_content.setId(ID);
            setParams(rating, name);

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
