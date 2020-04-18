package com.example.newcomer_io.ui.main;

import android.app.Application;
import android.content.Context;
import android.location.Location;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class UserData extends Application {
    private Location location;
    private ArrayList<TrendingContent> trendingContentArray;

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
}
