package com.example.newcomer_io.ui.main.UserDetails;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.location.Location;
import android.util.Log;
import androidx.annotation.NonNull;
import com.example.newcomer_io.ui.main.LocationSettings.TrendingContent;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.*;
import com.google.firebase.functions.FirebaseFunctions;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOError;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class UserData extends Application {
    private Location location;
    private ArrayList<TrendingContent> trendingContentArray;
    private int UserID;
    private TrendingContent chosenContent;
    private EventCreate eventCreate;
    OnGroupUpdate onGroupUpdate;
    private DatabaseReference mDatabase;
    private FirebaseFunctions mFunctions;

    public void setLocation(Location location){
        this.location = location;

    }

    @Override
    public void onCreate(){
        mFunctions = FirebaseFunctions.getInstance();

        super.onCreate();
    }

    @Override
    public Context getApplicationContext(){

        return super.getApplicationContext(); }

    public void setTrendingContent(ArrayList<TrendingContent> trendingContentArray) {
        this.trendingContentArray = trendingContentArray;
    }
    public ArrayList<TrendingContent> getTrendingContentArray(){
        return this.trendingContentArray;
    }

    public Location getLocation() {
        return location;
    }
    public void setChosenLocation(TrendingContent chosenContent) {
        this.chosenContent = chosenContent;
    }
    public TrendingContent getChosenContent(){
        return this.chosenContent;
    }
    public void updateEventContent(Activity context){
        //Then we try calling the data base
        onGroupUpdate = (OnGroupUpdate) context;
        final String guid = eventCreate.getGUID();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        mDatabase.child("Groups/" + guid).addValueEventListener(new ValueEventListener() {
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
                String location_name = dataSnapshot.child("Location").child("Longitude").getValue().toString();

                int groupSize = Integer.parseInt(dataSnapshot.child("Group Size").getValue().toString());

                String endTime = dataSnapshot.child("Timing").child("End Time").getValue().toString();
                String startTime = dataSnapshot.child("Timing").child("Start Time").getValue().toString();

                String[] s = endTime.split(" ");
                String endTime_Days = s[0];
                String endTime_Time = s[1];

                String[] s1 = startTime.split(" ");
                String startTime_Days = s1[0];
                String startTime_Time = s1[1];

                SimpleDateFormat simpleDateFormat_Days = new SimpleDateFormat("E, MMMM dd ");
                SimpleDateFormat simpleDateFormat_Time = new SimpleDateFormat("hh:mm a");

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
                String eventNotes = dataSnapshot.child("Event Notes").getValue().toString();
                onGroupUpdate.sendGroupUpdate(maxAge,minAge,event_name,new LatLng(lattitude,longitude), location_name,groupSize,startTime_Days_DT, startTime_Time_DT
                        ,endTime_Days_DT,endTime_Time_DT,eventNotes);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    public void setEventCreate(EventCreate eventCreate) {
        this.eventCreate = eventCreate;
    }
    public EventCreate getEventCreate(){
        return this.eventCreate;
    }
    public void pushGroupCreationUpdates() {
        //This function will push all of the updates to hte database
        mDatabase = FirebaseDatabase.getInstance().getReference();
        JSONObject eventDetails = new JSONObject();
        JSONObject userKey = new JSONObject();
        //Created these two json objects to hold ll of the values regarding the JSON data that will be inputtec into the database
        JSONObject locationObject = new JSONObject();
        JSONObject timing = new JSONObject();
        JSONObject ageRange = new JSONObject();

        try{
            userKey.put("Event Name", eventCreate.getEventName());
            userKey.put("Group Size", eventCreate.getGroupSize());
            userKey.put("Event Notes", eventCreate.getEventNotes());

            //---------------------------------Location Paramaters------------------------------------//
            locationObject.put("Lattitude",chosenContent.getLocation().latitude);
            locationObject.put("Longitude", chosenContent.getLocation().longitude);
            locationObject.put("Name", eventCreate.getLocationName());

            //---------------------------------Date Paramaters----------------------------------------//
            SimpleDateFormat simpleDateFormat_Days = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat simpleDateFormat_Time = new SimpleDateFormat( "HH:mm");
            String format_Days_StartTime = simpleDateFormat_Days.format(eventCreate.getStartTime_Day());

            String format_Time_StartTime = simpleDateFormat_Time.format(eventCreate.getStartTime());

            String format_Days_EndTime = simpleDateFormat_Days.format(eventCreate.getEndTime_Day());
            String format_Time_EndTime = simpleDateFormat_Time.format(eventCreate.getEndTime());
            timing.put("Start Time",format_Days_StartTime + " " + format_Time_StartTime);
            timing.put("End Time", format_Days_EndTime + " " + format_Time_EndTime);

            //---------------------------------Location Paramaters------------------------------------//
            ageRange.put("Min Age", eventCreate.getAgeMin());
            ageRange.put("Max Age", eventCreate.getAgeMax());
            userKey.put("Age Range", ageRange);
            userKey.put("Timing", timing);
            userKey.put("Location", locationObject);

            eventDetails.put(eventCreate.getGUID(), userKey);
        }catch(IOError | JSONException e){
            Log.d("JSONCreateGroupFailure", "Unable to create the JSON structure");
        }
        try {
            mDatabase.child("Groups").updateChildren(jsonToMap(eventDetails));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public static Map<String, Object> jsonToMap(JSONObject json) throws JSONException {
        Map<String, Object> retMap = new HashMap<String, Object>();

        if(json != JSONObject.NULL) {
            retMap = toMap(json);
        }
        return retMap;
    }

    public static Map<String, Object> toMap(JSONObject object) throws JSONException {
        Map<String, Object> map = new HashMap<String, Object>();

        Iterator<String> keysItr = object.keys();
        while(keysItr.hasNext()) {
            String key = keysItr.next();
            Object value = object.get(key);

            if(value instanceof JSONArray) {
                value = toList((JSONArray) value);
            }

            else if(value instanceof JSONObject) {
                value = toMap((JSONObject) value);
            }
            map.put(key, value);
        }
        return map;
    }

    public static List<Object> toList(JSONArray array) throws JSONException {
        List<Object> list = new ArrayList<Object>();
        for(int i = 0; i < array.length(); i++) {
            Object value = array.get(i);
            if(value instanceof JSONArray) {
                value = toList((JSONArray) value);
            }

            else if(value instanceof JSONObject) {
                value = toMap((JSONObject) value);
            }
            list.add(value);
        }
        return list;
    }
    public interface OnGroupUpdate {
        //This function represents the ongroup update that occurs after the firebase database is updated
        void sendGroupUpdate(int maxAge, int minAge, String event_name, LatLng latLng, String location_name, int groupSize, Date startTime_Days_DT, Date startTime_Time_DT, Date endTime_date, Date startTime_date, String eventNotes);
    }

}
