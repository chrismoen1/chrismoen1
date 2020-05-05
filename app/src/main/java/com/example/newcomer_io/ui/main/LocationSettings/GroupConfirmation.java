package com.example.newcomer_io.ui.main.LocationSettings;

import android.util.EventLog;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.viewpager.widget.ViewPager;
import com.example.newcomer_io.ui.main.EventDetails.PageAdapter;
import com.example.newcomer_io.R;
import com.example.newcomer_io.ui.main.UserDetails.EventCreate;
import com.example.newcomer_io.ui.main.UserDetails.UserData;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class GroupConfirmation extends AppCompatActivity {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private TabItem tab1, tab2,tab3;

    private EventCreate eventCreate;
    private PageAdapter pageAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_confirmation);

        //EventCreate eventCreate = new EventCreate("The attack of the titans");

        UserData userData = (UserData) getApplicationContext();
        tabLayout = findViewById(R.id.tabLayout);

        tabLayout = findViewById(R.id.tabLayout);
        tab1 = (TabItem) findViewById(R.id.tab1);
        tab2 = (TabItem) findViewById(R.id.tab2);
        viewPager = findViewById(R.id.viewPager);


        Date startTime = new Date(120,11, 12);
        Date endTime = new Date(120,11, 13);

        //Thje event create holds all of the detail regarding comments, like posts, event details event Details
        eventCreate = new EventCreate(this,"Attack of the Tiitans",
                startTime,endTime,"This is the best event ever:", "Clock Tower Brewery");

        fillEventDetails();
        pageAdapter = new PageAdapter(getSupportFragmentManager(), tabLayout.getTabCount(),eventCreate);
        viewPager.setAdapter(pageAdapter);

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

    private void fillEventDetails(EventCreate eventCreate) {
        //this function will fill in all of the event details for the current page
        TextView eventTitle = findViewById(R.id.eventTitle);
        LinearLayout eventNoteDetails = findViewById(R.id.eventNoteDetails);
        TextView eventNotes = findViewById(R.id.eventNoteDetails_Txt);
        TextView eventLocationDetails = findViewById(R.id.eventLocationDetails_Txt);
        TextView eventTimeDetails_Txt = findViewById(R.id.eventTimeDetails_Txt);
        TextView timeRemaining = findViewById(R.id.timeRemaining);

        String eventName = eventCreate.getEventName();
        eventTitle.setText(eventName);
        eventNotes.setText(eventCreate.getEventNotes());

        Date currentTime = Calendar.getInstance().getTime(); //Get the current time stamp
        Date startTime = eventCreate.getStartTime();

        String timeStampe = getTimeStamp(startTime,currentTime);
        timeRemaining.setText(timeStampe);

    }

    private String getTimeStamp(Date eventStartTime, Date currentTime) {

        //SO the purpose of this function is to determine the difference in time stamps betweent he current time and the start of the event time and return a readable string
      /*
      if it is less than 24 hours (display hours + minutes)
      if it is greater than 24 hours (display days + minutes
       */
        long diff = eventStartTime.getTime() - currentTime.getTime();
        long diff_hours = diff / (60 * 60 * 1000);

        if ((int) diff_hours < 24){
            //Then we will display
            //if it is less than 24 hours (display hours + seconds)
            Date diff_hours_date = new Date(diff_hours);
            String hours = String.valueOf(diff_hours_date.getHours());
            String minutes = String.valueOf(diff_hours_date.getMinutes());

            return (hours + " and " + minutes + " until event begins"); //Return the proper value

        }
        else{

            Date diff_hours_date = new Date(diff_hours);
            String days = String.valueOf(diff_hours_date.getDay());
            String hours = String.valueOf(diff_hours_date.getHours());

            return (days + " and " + hours + " until event begins"); //Return the proper value
        }
    }
}
