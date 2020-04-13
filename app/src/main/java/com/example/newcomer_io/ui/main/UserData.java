package com.example.newcomer_io.ui.main;

import android.app.Application;
import android.content.Context;
import android.location.Location;

public class UserData extends Application {
    private Location location;

    public void setLocation(Location location){
        this.location = location;
    }

    @Override
    public void onCreate(){
        super.onCreate();
    }

    @Override
    public Context getApplicationContext(){return super.getApplicationContext(); }

}
