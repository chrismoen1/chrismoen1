package com.example.newcomer_io.ui.main.UserDetails;

import android.app.Application;
import android.content.Context;
import android.location.Location;
import com.example.newcomer_io.ui.main.LocationSettings.TrendingContent;

import java.util.ArrayList;

public class UserData extends Application {
    private Location location;
    private ArrayList<TrendingContent> trendingContentArray;
    private int UserID;
    private TrendingContent chosenContent;
    private EventCreate eventCreate;

    public void setLocation(Location location){
        this.location = location;
    }

    @Override
    public void onCreate(){
        super.onCreate();
    }

    @Override
    public Context getApplicationContext(){return super.getApplicationContext(); }

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

}
