package com.example.newcomer_io.ui.main.Onboarding;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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
 * create an instance of this fragment.
 */
public class OnboardingInformation extends Fragment {
    private OnButtonAction_Information onButtonAction_information;
    @Override
    public void onAttach(Context context) {
        if (context != null){
            onButtonAction_information = (OnButtonAction_Information) context;
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

        View inflate = inflater.inflate(R.layout.joingroup_onboarding, container, false);
        Button next = inflate.findViewById(R.id.button2);
        Button prev = inflate.findViewById(R.id.button3);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onButtonAction_information != null){
                    //Then we do a call back
                    onButtonAction_information.clickedButton_Information("NEXT");
                }
            }
        });
        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onButtonAction_information != null){
                    onButtonAction_information.clickedButton_Information("PREV");
                }
            }
        });
        return inflate;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public interface OnButtonAction_Information{
        void clickedButton_Information(String action_type);
    }
    
}