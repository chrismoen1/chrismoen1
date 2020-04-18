package com.example.newcomer_io.ui.main;

import android.app.Activity;
import android.location.Location;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.cardview.widget.CardView;
import com.example.newcomer_io.R;

public class LocationNode {
    private Activity activity;
    private int TYPE_CUSTOM = 1;
    private int TYPE_POPULAR = 2;

    private ImageView card;
    private TextView title;
    private View fragment_locationNode;

    public LocationNode(Activity activity,int ID){
        this.activity = activity;

        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(activity.LAYOUT_INFLATER_SERVICE);

        fragment_locationNode = inflater.inflate(R.layout.location_type, null);
        title = fragment_locationNode.findViewById(R.id.textView7);

        fragment_locationNode.setId(ID);
        setParams(ID);
    }
    public View getView(){
        return this.fragment_locationNode;
    }
    public void setImageSelect(){

    }

    private void setParams(int ID) {

        TextView description = fragment_locationNode.findViewById(R.id.description);
        ImageView displayImage= fragment_locationNode.findViewById(R.id.imageView9);

        if (ID == TYPE_CUSTOM){

            //Then we type add the text into the box
            description.setText("Choose from trending bars and night clubs near you to host your event");
            displayImage.setImageResource(R.drawable.nightlife);
            title.setText("Trending");

        }
        else{

            description.setText("Specifying a custom location to host your event");
            displayImage.setImageResource(R.drawable.customize);
            title.setText("Custom");
        }
    }

}
