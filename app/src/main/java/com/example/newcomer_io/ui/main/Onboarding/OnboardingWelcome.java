package com.example.newcomer_io.ui.main.Onboarding;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.newcomer_io.R;
import com.example.newcomer_io.ui.main.EventDetails.CommentsPage;
import com.example.newcomer_io.ui.main.LocationSettings.TrendingContent;
import com.example.newcomer_io.ui.main.UserDetails.EventCreate;
import com.google.firebase.database.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OnboardingWelcome#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OnboardingWelcome extends Fragment
{
    public OnButtonAction_Welcome onButtonAction;

    @Override
    public void onAttach(Context context) {
        if (context != null){
            onButtonAction = (OnButtonAction_Welcome) context;
        }
        super.onAttach(context);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View inflate = inflater.inflate(R.layout.welcome_onboarding, container, false);
        Button next = inflate.findViewById(R.id.button2);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onButtonAction != null){
                    //Then we do a call back
                    onButtonAction.clickedButton_Welcome("NEXT");
                }
            }
        });

        return inflate;
    }
    @Override
    public void onResume() {
        super.onResume();
    }

    public interface OnButtonAction_Welcome{
        void clickedButton_Welcome(String action_type);
    }

}