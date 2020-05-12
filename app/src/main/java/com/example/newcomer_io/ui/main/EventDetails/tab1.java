package com.example.newcomer_io.ui.main.EventDetails;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.newcomer_io.R;
import com.example.newcomer_io.ui.main.LocationSettings.TrendingContent;
import com.example.newcomer_io.ui.main.UserDetails.EventCreate;

import java.util.ArrayList;
import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class tab1 extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private EventCreate eventCreate;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private TrendingContent content;

    public tab1(EventCreate eventCreate) {
        // Required empty public constructor
        //this.content = content;
        this.eventCreate = eventCreate;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View inflate = inflater.inflate(R.layout.fragment_tab1, container, false);
        LinearLayout scrollView = inflate.findViewById(R.id.scrollLayout); //This represents the scroll for all of the posts

        ArrayList<EventCreate.Posts> postsArrayList = eventCreate.getPostsArrayList();

        //This button represents the functionality to add a post to a portion of the UI.
        Button addPost = new Button(inflater.getContext());
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.CENTER;

        addPost.setLayoutParams(layoutParams);
        addPost.setText("Click to Create A Post");
        Typeface typeface = ResourcesCompat.getFont(getContext(), R.font.open_sans);
        int paddingDp = 8;
        float density = getContext().getResources().getDisplayMetrics().density;
        int paddingPixel = (int)(paddingDp * density);
        addPost.setTypeface(typeface);
        addPost.setPadding(paddingPixel,0,paddingPixel,0);
        addPost.setBackgroundResource(R.drawable.rounded_border);

        if (postsArrayList.size() != 0){
            //then it is not empty and we can add the posts in
            scrollView.addView(addPost);
        }
        else{
            //Then we display the people of have added a post in
          for (int i =0 ; i < postsArrayList.size();i++){

                View trending_content = inflater.inflate(R.layout.user_row, null);
                ConstraintLayout cardr = trending_content.findViewById(R.id.constraintLayout);
                cardr.setId(i);
                trending_content.setId(i);

                scrollView.addView(trending_content);
                //Set the paramaters of the post
                postsArrayList.get(i).setPostParams(trending_content);
        }
          scrollView.addView(addPost);

    }




        //eventCreate.addPost("John Dobalina","You mama is so fat, that one day she went to the store to go to the store", scrollView);
        //eventCreate.addPost("Joe Smoe", "Hi guys!!!!", scrollView);

        return inflate;
    }
}
