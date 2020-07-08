package com.example.newcomer_io.ui.main.UserDetails;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.location.Location;
import android.net.Uri;
import android.util.Log;
import androidx.annotation.NonNull;
import com.example.newcomer_io.ui.main.LocationSettings.TrendingContent;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.*;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.functions.HttpsCallableResult;
import com.google.firebase.storage.FirebaseStorage;
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

    //Prevalent user information to their current state
    private String UserID;
    private String email;
    private String displayName;
    private String phoneNumber;
    private String photoUrl;

    private TrendingContent chosenContent;
    private EventCreate eventCreate;
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

    public void setUUID(){
        mFunctions.getHttpsCallable("getUserGUID")
                .call().continueWith(new Continuation<HttpsCallableResult, String>() {
            @Override
            public String then(@NonNull Task<HttpsCallableResult> task) throws Exception {
                String data = (String) task.getResult().getData();
                setUserID(data);
                return data;
            }
        });
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

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String userID) {
        UserID = userID;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public void setAuthenticationInformation(String name, String uuid) {

        setUserID(uuid);
        setEmail(email);
        setDisplayName(displayName);
        setPhoneNumber(phoneNumber);
    }
}
