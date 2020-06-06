package com.example.newcomer_io.ui.main.EventDetails;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.newcomer_io.R;
import com.example.newcomer_io.ui.main.UserDetails.EventCreate;
import com.google.firebase.database.*;

import java.lang.reflect.Array;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class tab2 extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private EventCreate eventCreate;
    private LinearLayout scrollView;
    public tab2(EventCreate eventCreate) {
        // Required empty public constructor
        this.eventCreate = eventCreate;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment tab2.
     */


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View inflate = inflater.inflate(R.layout.fragment_tab2, container, false);
        scrollView = inflate.findViewById(R.id.scrollLayout);
        updatePostContent(this.eventCreate,scrollView);
        //eventCreate.addUser("John", 351,"Ottawa, Ontario", 1,linearLayout);
        return inflate;
    }

    public void updateScrollPostContentView(View inflate, LayoutInflater inflater){

        //ArrayList<EventCreate.JoinedUsers> joinedUsersArrayList = this.eventCreate.getJoinedUsersArrayList();

        //if (this.scrollView.getChildCount() != 0) {
        this.scrollView.removeAllViews();

     /*   if (joinedUsersArrayList.size() != 0){
            //Then we display the people of have added a post in
            for (int i =0 ; i < joinedUsersArrayList.size();i++){
           *//*     EventCreate.JoinedUsers joinedUsers  = joinedUsersArrayList.get(i);
                View joinedParamsView = joinedUsers.getJoinedParamsView();
                if(joinedParamsView.getParent() != null){
                    ((ViewGroup) joinedParamsView.getParent()).removeView(joinedParamsView);
                    this.scrollView.addView(joinedParamsView);
                }
                else if (joinedParamsView != null && this.scrollView != null){
                    this.scrollView.addView(joinedParamsView);
                }
*//*
            }*/

        //}
    }


    public void updatePostContent(EventCreate eventCreate, final LinearLayout scrollView){
        String guid = eventCreate.getGUID();
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

        mDatabase.child("Groups/" + guid + "/Joined").addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                sendJoined_Data(dataSnapshot.getChildren(),scrollView);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void sendJoined_Data(Iterable<DataSnapshot> posts, LinearLayout scrollView) {

        int i =0;
        if (scrollView.getChildCount() != 0){
            scrollView.removeAllViews();
        }
        for (DataSnapshot childNode : posts) {
            //Then we will get each fo the element
            String groupsJoined = (childNode.child("Groups Joined").getValue().toString());
            String location = childNode.child("Location").getValue().toString();
            String id  = childNode.child("Id").getValue().toString();
            String name = childNode.child("Name").getValue().toString();
            LayoutInflater inflater1 = (LayoutInflater) getContext().getSystemService(getContext().LAYOUT_INFLATER_SERVICE);
            View user_overviewer = inflater1.inflate(R.layout.user_overview, null);

            View constraintLayout= user_overviewer.findViewById(R.id.constraintInner);
            constraintLayout.setId(i);

            TextView userName_Txt = user_overviewer.findViewById(R.id.name);
            TextView events_attended_Txt = user_overviewer.findViewById(R.id.eventsAttended);
            TextView postNumber_Txt = user_overviewer.findViewById(R.id.posts);
            TextView location_Txt = user_overviewer.findViewById(R.id.message);

            userName_Txt.setText(name);
            events_attended_Txt.setText(groupsJoined);
            location_Txt.setText(location);
            postNumber_Txt.setText(String.valueOf(i));
            scrollView.addView(user_overviewer);
            //inflate.findViewById(R.id.)
            //Add the user to the event create group containing hte list of all other userse
            //this.eventCreate.addUser(name,groupsJoined,location);
            i += 1;
        }

        //updateScrollPostContentView(inflate,inflater);
    }

}
