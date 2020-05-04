package com.example.newcomer_io.ui.main.EventDetails;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class PageAdapter extends FragmentPagerAdapter {
    private int numTabs;

    public PageAdapter(FragmentManager fm, int tabCount) {
        super(fm);
        this.numTabs = 2; //equal to the number of tabs
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new tab1();
            case 1:
                return new tab2();
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
