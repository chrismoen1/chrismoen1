package com.example.newcomer_io.ui.main.Onboarding;

import android.content.Intent;
import android.net.Uri;
import android.os.Parcelable;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.viewpager.widget.ViewPager;
import com.example.newcomer_io.R;
import com.example.newcomer_io.ui.main.EventDetails.FilterView;
import com.example.newcomer_io.ui.main.EventDetails.PageAdapter;
import com.example.newcomer_io.ui.main.UserDetails.UserData;
import com.google.firebase.auth.FirebaseUser;

public class OnboardingLogistics extends AppCompatActivity implements OnboardingWelcome.OnButtonAction_Welcome, OnboardingInformation.OnButtonAction_Information   {
    private OnboardingPagerAdapter pageAdapter;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding_logistics);

        UserData userData = (UserData) getApplicationContext();

        Intent intent = getIntent();
        String photoUrl = intent.getStringExtra("PhotoUrl");
        String uuid = intent.getStringExtra("Uuid");
        String name = intent.getStringExtra("Name");

        userData.setAuthenticationInformation(name, uuid, photoUrl);

        viewPager = findViewById(R.id.viewPagerLogistics);

        pageAdapter = new OnboardingPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pageAdapter);
    }

    @Override
    public void onBackPressed() {
        if (viewPager.getCurrentItem() == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed();
        } else {
            // Otherwise, select the previous step.
            viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
        }
    }

    @Override
    public void clickedButton_Welcome(String action_type) {
        if (action_type.equals("NEXT") == true){
            //THen we advance to thext screen
            viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
        }
    }
    @Override
    public void clickedButton_Information(String action_type) {
        if (action_type.equals("NEXT") == true){
            //THen we advance to thext screen
            Intent intent = new Intent(this, ProfileInformation.class);
            startActivity(intent);
        }
        else{
            //otherwise we go back one pag e
            viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
        }
    }
}
