package com.example.newcomer_io.ui.main.Onboarding;

import android.app.Activity;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;
import com.example.newcomer_io.ui.main.UserDetails.EventCreate;

import java.util.Date;

import static java.security.AccessController.getContext;

public class OnboardingPagerAdapter extends FragmentStatePagerAdapter {
    private int numTabs;
    private EventCreate eventCreate;
    private Activity mainActivityContext;

    public OnboardingPagerAdapter(FragmentManager fm) {
        super(fm);
        numTabs = 2;
    }


    @Override
    public Fragment getItem(int position) {

        switch (position){
            case 0:
                return new OnboardingWelcome();
            case 1:
                return new OnboardingInformation();
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
