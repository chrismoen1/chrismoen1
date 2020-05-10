package com.example.newcomer_io.ui.main.UserDetails;

import android.app.Application;
import android.content.Context;
import android.location.Location;
import androidx.annotation.NonNull;
import com.example.newcomer_io.ui.main.LocationSettings.TrendingContent;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.functions.HttpsCallableResult;

import java.util.ArrayList;
import java.util.UUID;

public class UserData extends Application {
    private Location location;
    private ArrayList<TrendingContent> trendingContentArray;
    private int UserID;
    private TrendingContent chosenContent;
    private EventCreate eventCreate;

    private FirebaseDatabase database;
    private FirebaseFunctions mFunctions;
    public void setLocation(Location location){
        this.location = location;
    }

    @Override
    public void onCreate(){
        database = FirebaseDatabase.getInstance();
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

    public void pushFireBaseUpdates() {
        //This method will push the firebase to the firebase server
        //pushTrendingContent
        //push individual user ID
        mFunctions.getHttpsCallable("getGroupGUID")
                .call().continueWith(new Continuation<HttpsCallableResult, String>() {
            @Override
            public String then(@NonNull Task<HttpsCallableResult> task) throws Exception {
                String result = (String) task.getResult().getData(); 
                return null;
            }
        });

    }

}
