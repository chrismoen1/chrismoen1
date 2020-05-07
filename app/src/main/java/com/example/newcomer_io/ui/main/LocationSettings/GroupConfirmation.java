package com.example.newcomer_io.ui.main.LocationSettings;

import android.graphics.Bitmap;
import android.util.EventLog;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SimpleExpandableListAdapter;
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
    private ImageView displayPhoto; 
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_confirmation);

        //EventCreate eventCreate = new EventCreate("The attack of the titans");

        UserData userData = (UserData) getApplicationContext();
        TrendingContent chosenContent = userData.getChosenContent();
        
        tabLayout = findViewById(R.id.tabLayout);

        tabLayout = findViewById(R.id.tabLayout);
        tab1 = (TabItem) findViewById(R.id.tab1);
        tab2 = (TabItem) findViewById(R.id.tab2);
        viewPager = findViewById(R.id.viewPager);


        Date startTime = new Date(120,11, 12);
        Date endTime = new Date(120,11, 13);

        //Thje event create holds all of the detail regarding comments, like posts, event details event Details
        String placeName = chosenContent.getPlaceName();
        Bitmap photo = chosenContent.getPhoto();


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


}
