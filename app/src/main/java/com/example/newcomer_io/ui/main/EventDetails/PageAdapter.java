package com.example.newcomer_io.ui.main.EventDetails;

import android.app.Activity;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import com.example.newcomer_io.ui.main.UserDetails.EventCreate;

import java.util.Date;

import static java.security.AccessController.getContext;

public class PageAdapter extends FragmentPagerAdapter {
    private int numTabs;
    private EventCreate eventCreate;
    private Activity mainActivityContext;
    public PageAdapter(FragmentManager fm, int tabCount,EventCreate eventCreate, Activity mainActivityContext) {
        super(fm);
        this.numTabs = 2; //equal to the number of tabs
        this.eventCreate = eventCreate;
        this.mainActivityContext = mainActivityContext;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position){
            case 0:
                return new tab1(eventCreate);
            case 1:
                return new tab2(eventCreate);
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return this.numTabs;
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }
}
