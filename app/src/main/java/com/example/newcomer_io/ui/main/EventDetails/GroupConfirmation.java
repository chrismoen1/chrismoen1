package com.example.newcomer_io.ui.main.EventDetails;

import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.viewpager.widget.ViewPager;
import com.example.newcomer_io.R;
import com.example.newcomer_io.ui.main.UserDetails.EventCreate;
import com.example.newcomer_io.ui.main.UserDetails.UserData;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

import java.text.SimpleDateFormat;
import java.util.Date;

public class GroupConfirmation extends AppCompatActivity implements UserData.OnGroupUpdate {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private TabItem tab1, tab2,tab3;

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
        userData.updateEventContent(this);

        tabLayout = findViewById(R.id.tabLayout);

        tabLayout = findViewById(R.id.tabLayout);
        tab1 = (TabItem) findViewById(R.id.tab1);
        tab2 = (TabItem) findViewById(R.id.tab2);
        viewPager = findViewById(R.id.viewPager);

        Date startTime = new Date(120,11, 12);
        Date endTime = new Date(120,11, 13);

        //Thje event create holds all of the detail regarding comments, like posts, event details event Details
        //String placeName = chosenContent.getPlaceName();
        //Bitmap photo = chosenContent.getPhoto();

        pageAdapter = new PageAdapter(getSupportFragmentManager(), tabLayout.getTabCount(),eventCreate);
        viewPager.setAdapter(pageAdapter);

        //We want to add this detail to the firebase data base
        //Add to firebase

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                if (tab.getPosition() == 0){
                    pageAdapter.notifyDataSetChanged();
                }
                else{
                    pageAdapter.notifyDataSetChanged();
                }
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

    @Override
    public void sendGroupUpdate(int maxAge, int minAge, String event_name, LatLng latLng, String location_name, int groupSize, Date startTime_Days_DT, Date startTime_Time_DT,
                                Date endTime_Days_DT, Date endTime_Time_DT,String startDate_Txt,String endDate_Txt,String eventNotes) {

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
}
