package com.example.newcomer_io.ui.main.EventDetails;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
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
import com.example.newcomer_io.ui.main.UserDetails.UserData;
import com.google.firebase.database.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.zip.Inflater;


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
    private LinearLayout scrollView;
    private boolean flag_Button_Added;
    private Button addPost;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    public tab1(EventCreate eventCreate) {
        // Required empty public constructor
        //this.content = content;
        this.eventCreate = eventCreate;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void setFlag_Button_Added(boolean val){this.flag_Button_Added = val; }
    private boolean getFlag_Button_Added(){return this.flag_Button_Added; }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View inflate = inflater.inflate(R.layout.fragment_tab1, container, false);
        scrollView = inflate.findViewById(R.id.scrollLayout); //This represents the scroll for all of the posts
        setFlag_Button_Added(false);

        //This button represents the functionality to add a post to a portion of the UI.
        updatePostContent(eventCreate,inflate,inflater);

        return inflate;
    }

    private void createButton(View inflate){
        addPost = new Button(inflate.getContext());
        addPost.setId(200);
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
    }
    private Button getButtonView(){
        return addPost;
    }

    public void updateScrollPostContentView(View inflate, LayoutInflater inflater){

        ArrayList<EventCreate.Posts> postsArrayList = this.eventCreate.getPostsArrayList();
        boolean flag_button_added = getFlag_Button_Added();

        //if (this.scrollView.getChildCount() != 0) {
            this.scrollView.removeAllViews();
        //}
        if (!flag_button_added){
            createButton(inflate);
        }

        if (postsArrayList.size() == 0){
            //then it is not empty and we can add the posts in
            this.scrollView.addView(this.addPost);
            setFlag_Button_Added(true);
        }
        else{
            //Then we display the people of have added a post in
            for (int i =0 ; i < postsArrayList.size();i++){
                EventCreate.Posts posts = postsArrayList.get(i);
                if (posts.getPostParamsView() != null && this.scrollView != null){

                    this.scrollView.addView(posts.getPostParamsView());

                }

            }
            this.scrollView.addView(addPost);


        }
    }

    public void updatePostContent(EventCreate eventCreate, final View inflate, final LayoutInflater inflater){
         String guid = eventCreate.getGUID();
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

        mDatabase.child("Groups/" + guid + "/Posts").addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                sendData_Post(dataSnapshot.getChildren(),inflate,inflater);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void sendData_Post(Iterable<DataSnapshot> posts, View inflate, LayoutInflater inflater) {
        for (DataSnapshot childNode : posts) {
            //Then we will get each fo the element
            int comments = Integer.parseInt(childNode.child("Comments").getValue().toString());

            int likes = Integer.parseInt(childNode.child("Likes").getValue().toString());
            String userId = childNode.child("Id").getValue().toString();
            String message = childNode.child("Message").getValue().toString();
            String name = childNode.child("Name").getValue().toString();
            this.eventCreate.addPost(name, message, likes, comments,userId);

        }
        updateScrollPostContentView(inflate,inflater);


    }
}
