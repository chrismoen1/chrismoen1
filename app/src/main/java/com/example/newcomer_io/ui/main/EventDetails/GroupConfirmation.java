package com.example.newcomer_io.ui.main.EventDetails;

import android.app.Activity;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.viewpager.widget.ViewPager;
import com.example.newcomer_io.R;
import com.example.newcomer_io.ui.main.UserDetails.EventCreate;
import com.example.newcomer_io.ui.main.UserDetails.UserData;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class GroupConfirmation extends AppCompatActivity implements EventCreate.OnGroupUpdate {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private TabItem tab1, tab2;

    private EventCreate eventCreate;
    private PageAdapter pageAdapter;
    private ImageView displayPhoto; 
    private UserData userData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_confirmation);
        //EventCreate eventCreate = new EventCreate("The attack of the titans");

        EventCreate eventCreate = new EventCreate(this);
        userData = (UserData) getApplicationContext();
        userData.setEventCreate(eventCreate);
        userData.getEventCreate().setGUID("ee493abb-5a86-4c1b-9eae-201336c3a283");


        tabLayout = findViewById(R.id.tabLayout);

        tabLayout = findViewById(R.id.tabLayout);
        tab1 = (TabItem) findViewById(R.id.tab1);
        tab2 = (TabItem) findViewById(R.id.tab2);
        viewPager = findViewById(R.id.viewPager);
        updateEventContent(eventCreate);

        Date startTime = new Date(120,11, 12);
        Date endTime = new Date(120,11, 13);

        //Thje event create holds all of the detail regarding comments, like posts, event details event Details
        //String placeName = chosenContent.getPlaceName();
        //Bitmap photo = chosenContent.getPhoto();

        pageAdapter = new PageAdapter(getSupportFragmentManager(), tabLayout.getTabCount(),eventCreate, this);
        viewPager.setAdapter(pageAdapter);

        //We want to add this detail to the firebase data base
        //Add to firebase

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
    }

    public void sendGroupUpdate(int maxAge, int minAge, String event_name, LatLng latLng, String location_name, int groupSize, Date startTime_Days_DT, Date startTime_Time_DT, Date endTime_Days_DT, Date endTime_Time_DT, String startDate_Txt, String endDate_Txt, String eventNotes) {
        // Set the event create data
        EventCreate eventCreate = userData.getEventCreate();
        eventCreate.setEventName(event_name);
        eventCreate.setAgeMax(String.valueOf(maxAge));
        eventCreate.setAgeMin(String.valueOf(minAge));
        eventCreate.setLocationName(location_name);
        eventCreate.setEventlocation(latLng);
        eventCreate.setGroupSize(groupSize);
        eventCreate.setStartTime(startTime_Time_DT,startTime_Days_DT,startDate_Txt);
        eventCreate.setEndTime(endTime_Time_DT,endTime_Days_DT,endDate_Txt);
        eventCreate.setEventNotes(eventNotes);

        if (eventCreate.getEventDetails() == null){
            eventCreate.createEventDetails();

        }else{
            eventCreate.updateEventDetails();
        }
        userData.setEventCreate(eventCreate);
    }

    public void updateEventContent(EventCreate eventCreate){
        //Then we try calling the data base
        final String guid = eventCreate.getGUID();
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

        mDatabase.child("Groups/" + guid + "/Event Details/").addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //Load all of the data related to the trending content
                String fa = "";

                //Let's start with the data children
                int maxAge = Integer.parseInt(dataSnapshot.child("Age Range").child("Max Age").getValue().toString());
                int minAge= Integer.parseInt(dataSnapshot.child("Age Range").child("Min Age").getValue().toString());

                String event_name = dataSnapshot.child("Event Name").getValue().toString();

                double lattitude = Double.parseDouble(dataSnapshot.child("Location").child("Lattitude").getValue().toString());
                double longitude = Double.parseDouble(dataSnapshot.child("Location").child("Longitude").getValue().toString());
                String location_name = dataSnapshot.child("Location").child("Name").getValue().toString();

                int groupSize = Integer.parseInt(dataSnapshot.child("Group Size").getValue().toString());

                String endTime = dataSnapshot.child("Timing").child("End Time").getValue().toString();
                String startTime = dataSnapshot.child("Timing").child("Start Time").getValue().toString();

                String[] s = endTime.split(" ");
                String endTime_Days = s[0];
                String endTime_Time = s[1];

                String[] s1 = startTime.split(" ");
                String startTime_Days = s1[0];
                String startTime_Time = s1[1];

                SimpleDateFormat simpleDateFormat_Days = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat simpleDateFormat_Time = new SimpleDateFormat( "HH:mm");

                Date endTime_Days_DT = null;
                Date endTime_Time_DT = null;

                Date startTime_Days_DT = null;
                Date startTime_Time_DT = null;
                try {
                    startTime_Days_DT = simpleDateFormat_Days.parse(startTime_Days);
                    startTime_Time_DT = simpleDateFormat_Time.parse(startTime_Time);

                    endTime_Days_DT = simpleDateFormat_Days.parse(endTime_Days);
                    endTime_Time_DT = simpleDateFormat_Time.parse(endTime_Time);

                } catch (ParseException e) {
                    e.printStackTrace();
                }

                String endDate_Txt;
                String startDate_Txt;
                SimpleDateFormat newFormat_Day = new SimpleDateFormat("E, MMMM dd ");
                SimpleDateFormat newFormat_clock = new SimpleDateFormat("hh:mm a");

                if (startTime_Time_DT.equals(endTime_Time_DT) == true) {
                    ///Then we have an all day event endDate_Txt
                    startDate_Txt = newFormat_Day.format(startTime_Days_DT);
                    endDate_Txt = newFormat_Day.format(endTime_Days_DT);
                }else{
                    endDate_Txt = newFormat_Day.format(endTime_Days_DT) + " : " + newFormat_clock.format(endTime_Time_DT);
                    startDate_Txt = newFormat_Day.format(startTime_Days_DT) + " : " + newFormat_clock.format(startTime_Time_DT);
                }

                String eventNotes = dataSnapshot.child("Event Notes").getValue().toString();
                sendGroupUpdate(maxAge,minAge,event_name,new LatLng(lattitude,lattitude),location_name,
                        groupSize,startTime_Days_DT,startTime_Time_DT,endTime_Days_DT,endTime_Time_DT,startDate_Txt,endDate_Txt,eventNotes);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
