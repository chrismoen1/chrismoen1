package com.example.newcomer_io.ui.main.EventDetails;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.viewpager.widget.ViewPager;
import com.example.newcomer_io.R;
import com.example.newcomer_io.ui.main.UserDetails.EventCreate;
import com.example.newcomer_io.ui.main.UserDetails.UserData;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.*;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.text.BreakIterator;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class GroupConfirmation extends AppCompatActivity implements EventCreate.OnGroupUpdate {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private TabItem tab1, tab2;

    private EventCreate eventCreate;
    private PageAdapter pageAdapter;
    private ImageView displayPhoto;
    private UserData userData;

    private String subject;
    private String eventLocation;
    private ArrayList<String> subjectTags;
    private String eventName;
    private String eventGuid;
    private String eventNotes;
    private String eventTiming;

    private boolean isJoined;

    private TextView eventTiming_Txt;
    private TextView eventNotes_Txt;
    private TextView eventLocation_Txt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_confirmation);
        //EventCreate eventCreate = new EventCreate("The attack of the titans");

        //First lets get the paramateres from the prior activity
        Intent intent = getIntent();
        //Button joinGroup
        eventGuid = intent.getStringExtra("GUID");
        eventNotes = intent.getStringExtra("Event Notes");
        eventName = intent.getStringExtra("Event Name");
        subject = intent.getStringExtra("Subject");
        eventLocation = intent.getStringExtra("Event Name");

        subjectTags = intent.getStringArrayListExtra("Subject Tags");
        eventTiming = intent.getStringExtra("Event Time");

        userData = (UserData) getApplicationContext();

        EventCreate eventCreate = userData.getEventCreate();
        if (eventCreate == null){
            this.eventCreate = new EventCreate(this);
        }else{
            setEventCreate(eventCreate);
        }
        //this.displayPhoto = findViewById(R.id.displayPhoto);

        LinearLayout tagHolder = findViewById(R.id.tagHolder);
        if (tagHolder != null){
            tagHolder.removeAllViews();
            tagHolder.addView(createGroupTagsLayout(subjectTags));

        }else{
            tagHolder.addView(createGroupTagsLayout(subjectTags));
        }

        setEventCreate(this.eventCreate);
        userData.setEventCreate(this.eventCreate);
        userData.getEventCreate().setGUID(eventGuid);
        userData.setUserID("ee493abb-5a86-4c1b-9eae-201336c3a283");

        isJoined = false;

        eventNotes_Txt = findViewById(R.id.eventNoteDetails_Txt);
        eventLocation_Txt = findViewById(R.id.eventLocationDetails_Txt);
        eventTiming_Txt = findViewById(R.id.eventTimeDetails_Txt);
        eventNotes_Txt.setText(eventNotes);
        eventLocation_Txt.setText(eventLocation);
        eventTiming_Txt.setText(eventTiming);

        tabLayout = findViewById(R.id.tabLayout);

        tabLayout = findViewById(R.id.tabLayout);
        tab1 = (TabItem) findViewById(R.id.tab1);
        tab2 = (TabItem) findViewById(R.id.tab2);
        viewPager = findViewById(R.id.viewPager);
        //updateEventContent(eventCreate);

        Date startTime = new Date(120,11, 12);
        Date endTime = new Date(120,11, 13);

        //Thje event create holds all of the detail regarding comments, like posts, event details event Details
        //String placeName = chosenContent.getPlaceName();
        //Bitmap photo = chosenContent.getPhoto();

        pageAdapter = new PageAdapter(getSupportFragmentManager(), tabLayout.getTabCount(), this.eventCreate, this);
        viewPager.setAdapter(pageAdapter);

        /*try {
            getGroupImageUpdate();
        } catch (IOException e) {
            e.printStackTrace();
        }
*/
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
    public LinearLayout createGroupTagsLayout(ArrayList<String> children) {
        LinearLayout viewHolder = new LinearLayout(this);
        viewHolder.setOrientation(LinearLayout.VERTICAL);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        for(int i =0; i < children.size();i++){

            //Then we iterate and get each tag
            String typeName = children.get(i);
            LinearLayout checkMarksLayout = createCheckMarksLayout(typeName);
            viewHolder.addView(checkMarksLayout);

        }
        return viewHolder;
    }
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
    public LinearLayout createCheckMarksLayout(String typeName){

        LinearLayout container = new LinearLayout(this);
        LinearLayout.LayoutParams container_Parms = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        container.setOrientation(LinearLayout.HORIZONTAL);
        container.setLayoutParams(container_Parms);

        int x = dip2px(this, 10);
        int y = dip2px(this, 10);

        LinearLayout.LayoutParams checkMarkBoxes_Parms = new LinearLayout.LayoutParams(x,y);
        //Create the checkbox which will go beside the text
        ImageView checkBox = new ImageView(this);
        checkBox.setBackgroundResource(R.drawable.checkicon);
        checkBox.setLayoutParams(checkMarkBoxes_Parms);

        TextView typeName_Txt = new TextView(this);
        typeName_Txt.setMaxLines(3);
        typeName_Txt.setTextSize(15f);
        LinearLayout.LayoutParams textDisplay_Parms = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        textDisplay_Parms.setMargins(dip2px(this,2),0,0,0);
        typeName_Txt.setText(typeName);
        typeName_Txt.setLayoutParams(textDisplay_Parms);

        //Add it to the views
        container.addView(checkBox);
        container.addView(typeName_Txt);

        return container;
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

    @Override
    public void onBackPressed() {


        super.onBackPressed();
    }
    
    public ImageView getDisplayPhoto() {
        return displayPhoto;
    }

    public void setDisplayPhoto(ImageView displayPhoto) {
        this.displayPhoto = displayPhoto;
    }
    public void setDisplayPhoto_Bitmap(Bitmap photo){
        this.displayPhoto.setImageBitmap(photo);
    }

    public void getGroupImageUpdate() throws IOException {

        String path = "/Group Image/" + getEventCreate().getGUID() + "/groupphoto.jpg";

            StorageReference reference = FirebaseStorage.getInstance().getReference();
            //This function will download the user's image
            final long ONE_MEGABYTE = 1024 * 1024;

            reference.child(path).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Picasso.get().load(uri).into(getDisplayPhoto());

                }
            });

            ///Group Image/ee493abb-5a86-4c1b-9eae-201336c3a283
            reference.child(path).getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {
                    Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

                    getEventCreate().setPhoto(bitmap);
                    setDisplayPhoto_Bitmap(bitmap);

                }
            });
    }

    public void setEventCreate(EventCreate eventCreate) {
        this.eventCreate = eventCreate;
    }
    public EventCreate getEventCreate(){return this.eventCreate; }

    public String getEventGuid() {
        return eventGuid;
    }

    public void setEventGuid(String eventGuid) {
        this.eventGuid = eventGuid;
    }

    public String getEventNotes() {
        return eventNotes;
    }

    public void setEventNotes(String eventNotes) {
        this.eventNotes = eventNotes;
    }

    public ArrayList<String> getSubjectTags() {
        return subjectTags;
    }

    public void setSubjectTags(ArrayList<String> subjectTags) {
        this.subjectTags = subjectTags;
    }

    public String getEventLocation() {
        return eventLocation;
    }

    public void setEventLocation(String eventLocation) {
        this.eventLocation = eventLocation;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public TextView getEventNotes_Txt() {
        return eventNotes_Txt;
    }

    public void setEventNotes_Txt(TextView eventNotes_Txt) {
        this.eventNotes_Txt = eventNotes_Txt;
    }

    public TextView getEventLocation_Txt() {
        return eventLocation_Txt;
    }

    public void setEventLoation_Txt(TextView eventLoation_Txt) {
        this.eventLocation_Txt = eventLoation_Txt;
    }

    public String getEventTiming() {
        return eventTiming;
    }

    public void setEventTiming(String eventTiming) {
        this.eventTiming = eventTiming;
    }

    public TextView getEventTiming_Txt() {
        return eventTiming_Txt;
    }

    public void setEventTiming_Txt(TextView eventTiming_Txt) {
        this.eventTiming_Txt = eventTiming_Txt;
    }

    public boolean isJoined() {
        return isJoined;
    }

    public void setJoined(boolean joined) {
        isJoined = joined;
    }
}
