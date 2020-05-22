package com.example.newcomer_io.ui.main.EventDetails;

import android.os.CountDownTimer;
import android.text.InputType;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.interfaces.OnSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.widgets.CrystalRangeSeekbar;
import com.crystal.crystalrangeseekbar.widgets.CrystalSeekbar;
import com.example.newcomer_io.R;

import java.util.ArrayList;

public class FilterView extends AppCompatActivity {
    //This class will incorporate the current filter for holding all of the values related to the filtering view

    private EditText future;
    final private int CUSTOM_AGE = 69; //lol

    //This part of the filter represents the min/max distance
    private CrystalRangeSeekbar ageRange;
    private TextView minAge;
    private TextView maxAge;
    private EditText customAge;
    private CrystalSeekbar  searchDistance;
    private TextView distance;

    private TextView timeFrame;
    private CrystalSeekbar timeFrameSeekbar;

    private CrystalSeekbar groupSize;
    private TextView size;
    private LinearLayout groupSizeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_view);

        groupSizeLayout = findViewById(R.id.groupSizeLayout);
        timeFrameSeekbar = findViewById(R.id.timeFrameSeekbar);

        ageRange = findViewById(R.id.rangeSeekbar5);

        minAge = findViewById(R.id.minAge);
        maxAge = findViewById(R.id.maxAge);
        groupSize = findViewById(R.id.seekBar);
        groupSize.setMinStartValue(2);
        groupSize.setMaxValue(20);
        size = findViewById(R.id.size);

        timeFrame = findViewById(R.id.timeFrame);

        ageRange.setMinValue(18);
        ageRange.setMaxValue(100);

        ageRange.setOnRangeSeekbarChangeListener(new OnRangeSeekbarChangeListener() {
            @Override
            public void valueChanged(Number minValue, Number maxValue) {
                getMinAge().setText(minValue.toString());
                getMaxAge().setText(maxValue.toString());
            }
        });

        searchDistance = (CrystalSeekbar)findViewById(R.id.seekBar1);
        distance= findViewById(R.id.distance);
        timeFrameSeekbar.setMinValue(1);
        timeFrameSeekbar.setMaxValue(14);

        searchDistance.setOnSeekbarChangeListener(new OnSeekbarChangeListener() {
            @Override
            public void valueChanged(Number minValue) {
                getDistance().setText("Up to " + minValue.toString() + " km");
            }
        });

        timeFrameSeekbar.setOnSeekbarChangeListener(new OnSeekbarChangeListener() {
            @Override
            public void valueChanged(Number value) {
                getTimeFrame().setText("Up to " + value.toString() + " days from now");
            }
        });

        groupSize.setOnSeekbarChangeListener(new OnSeekbarChangeListener() {

            @Override
            public void valueChanged(Number minValue) {
                LinearLayout groupSizeLayout = getGroupSizeLayout();
                int progress = minValue.intValue();
                if (progress != 20){
                    //
                    if (getCustomAge() != null){
                        groupSizeLayout.removeView(getCustomAge());
                        setCustomAge(null);
                        groupSizeLayout.addView(getSize());
                    }
                    getSize().setText("up to " + String.valueOf(progress) + " people");
                }
                else{
                    if (getCustomAge() == null){
                        //Then we dispaly an Edit Text that tells the user to add to their values
                        groupSizeLayout.removeView(getSize());
                        groupSizeLayout.addView(createCustomAge());

                    }

                }
            }
        });

    }
    public EditText createCustomAge(){
        customAge = new EditText(this);

        customAge.setHint("Enter a custom group size");

        new CountDownTimer(3000, 1000) {
            public void onTick(long millisUntilFinished) {
            }

            public void onFinish() {
                if (customAge != null){
                    customAge.requestFocus();
                    customAge.setHint("");
                }
            }
        }.start();

        customAge.setId(CUSTOM_AGE);
        customAge.setInputType(InputType.TYPE_CLASS_NUMBER |
                InputType.TYPE_NUMBER_FLAG_DECIMAL |
                InputType.TYPE_NUMBER_FLAG_SIGNED);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.CENTER;
        customAge.setLayoutParams(layoutParams);
        return customAge;
    }
    public EditText getCustomAge(){
        return customAge;
    }
    public void setCustomAge(EditText customAge){
        this.customAge = null;
    }

    public CrystalRangeSeekbar getSeekbar() {
        return ageRange;
    }

    public void setSeekbar(CrystalRangeSeekbar seekbar) {
        this.ageRange = seekbar;
    }

    public EditText getFuture() {
        return future;
    }

    public void setFuture(EditText future) {
        this.future = future;
    }


    public CrystalSeekbar getSearchDistance() {
        return searchDistance;
    }

    public void setSearchDistance(CrystalSeekbar searchDistance) {
        this.searchDistance = searchDistance;
    }

    public CrystalSeekbar getGroupSize() {
        return groupSize;
    }

    public void setGroupSize(CrystalSeekbar groupSize) {
        this.groupSize = groupSize;
    }

    public TextView getMinAge() {
        return minAge;
    }

    public void setMinAge(TextView minAge) {
        this.minAge = minAge;
    }

    public TextView getMaxAge() {
        return maxAge;
    }

    public void setMaxAge(TextView maxAge) {
        this.maxAge = maxAge;
    }

    public TextView getDistance() {
        return distance;
    }

    public void setDistance(TextView distance) {
        this.distance = distance;
    }

    public TextView getSize() {
        return size;
    }

    public void setSize(TextView size) {
        this.size = size;
    }

    public LinearLayout getGroupSizeLayout() {
        return groupSizeLayout;
    }

    public void setGroupSizeLayout(LinearLayout groupSizeLayout) {
        this.groupSizeLayout = groupSizeLayout;
    }

    public int getCUSTOM_AGE() {
        return CUSTOM_AGE;
    }

    public TextView getTimeFrame() {
        return timeFrame;
    }

    public void setTimeFrame(TextView timeFrame) {
        this.timeFrame = timeFrame;
    }

    public CrystalSeekbar getTimeFrameSeekbar() {
        return timeFrameSeekbar;
    }

    public void setTimeFrameSeekbar(CrystalSeekbar timeFrameSeekbar) {
        this.timeFrameSeekbar = timeFrameSeekbar;
    }
}
