package com.example.newcomer_io.ui.main.EventDetails;

import android.os.CountDownTimer;
import android.text.InputType;
import android.view.*;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.interfaces.OnSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.widgets.CrystalRangeSeekbar;
import com.crystal.crystalrangeseekbar.widgets.CrystalSeekbar;
import com.example.newcomer_io.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.plumillonforge.android.chipview.Chip;
import com.plumillonforge.android.chipview.ChipView;

import java.util.ArrayList;
import java.util.List;

public class FilterView extends AppCompatActivity {
    //This class will incorporate the current filter for holding all of the values related to the filtering view

    private EditText future;
    final private int CUSTOM_AGE = 69; //lol
    private CheckBox other;
    //Variables that we will pass back to the prior page that we called from
    private int timeFrameVal;
    private int minAgeVal;
    private int maxAgeVal;
    private int searchDistanceVal;
    private int groupSizeVal;

    //This part of the filter represents the min/max distance
    private CrystalRangeSeekbar ageRange;
    private CrystalSeekbar  searchDistance;
    private TextView distance;
    private FloatingActionButton floatingActionButton;
    private TextView timeFrame;
    private CrystalSeekbar timeFrameSeekbar;

    private EditText subjectType;

    private CrystalSeekbar groupSize;
    private TextView size;
    private LinearLayout groupSizeLayout;
    private EditText studyGroupType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_view);

        groupSizeLayout = findViewById(R.id.groupSizeLayout);
        timeFrameSeekbar = findViewById(R.id.timeFrameSeekbar);
        floatingActionButton = findViewById(R.id.backGutton2);
        ageRange = findViewById(R.id.rangeSeekbar5);
        other = findViewById(R.id.other);
        studyGroupType= findViewById(R.id.bySubject);

        List<Chip> chipList = new ArrayList<>();
        chipList.add(new Tag("Lorem"));
        chipList.add(new Tag("Ipsum dolor"));
        chipList.add(new Tag("Sit amet"));
        chipList.add(new Tag("Consectetur"));
        chipList.add(new Tag("adipiscing elit"));
        ChipView chipDefault = (ChipView) findViewById(R.id.chipview);

        chipDefault.setChipList(chipList);


        groupSize = findViewById(R.id.seekBar);
        groupSize.setMinStartValue(2);
        groupSize.setMaxValue(15);

        size = findViewById(R.id.size);

        timeFrame = findViewById(R.id.timeFrame);

        timeFrameSeekbar.setMinValue(1);
        timeFrameSeekbar.setMaxValue(14);
        groupSize.setMinStartValue(6);

        timeFrameSeekbar.setOnSeekbarChangeListener(new OnSeekbarChangeListener() {
            @Override
            public void valueChanged(Number value) {
                setTimeFrameVal(value.intValue());
                getTimeFrame().setText("Up to " + value.toString() + " days from now");
            }
        });

        groupSize.setOnSeekbarChangeListener(new OnSeekbarChangeListener() {

            @Override
            public void valueChanged(Number minValue) {
                LinearLayout groupSizeLayout = getGroupSizeLayout();
                int progress = minValue.intValue();

                getSize().setText("up to " + String.valueOf(progress) + " people");
                setGroupSizeVal(progress);

            }
        });

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Then we go back to the previous page

            }
        });

        other.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                LinearLayout studyGroupTheme = findViewById(R.id.studyGroupType);
                if (isChecked){

                    EditText editText = new EditText(getApplicationContext());
                    setStudyGroupType(editText);
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    layoutParams.gravity = Gravity.CENTER;

                    editText.setHint("Theme");
                    editText.setLayoutParams(layoutParams);

                    studyGroupTheme.addView(editText);
                }else{
                    studyGroupTheme.removeView(getStudyGroupType());
                }

            }
        });

        subjectType.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                String s = v.getText().toString();
                if (s.endsWith(",") == true){
                    //then we add that string to the chip view
                    m
                }

                return false;
            }
        });

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

    public FloatingActionButton getFloatingActionButton() {
        return floatingActionButton;
    }

    public void setFloatingActionButton(FloatingActionButton floatingActionButton) {
        this.floatingActionButton = floatingActionButton;
    }

    public int getTimeFrameVal() {
        return timeFrameVal;
    }

    public void setTimeFrameVal(int timeFrameVal) {
        this.timeFrameVal = timeFrameVal;
    }

    public int getMinAgeVal() {
        return minAgeVal;
    }

    public void setMinAgeVal(int minAgeVal) {
        this.minAgeVal = minAgeVal;
    }

    public int getSearchDistanceVal() {
        return searchDistanceVal;
    }

    public void setSearchDistanceVal(int searchDistanceVal) {
        this.searchDistanceVal = searchDistanceVal;
    }

    public int getGroupSizeVal() {
        return groupSizeVal;
    }

    public void setGroupSizeVal(int groupSizeVal) {
        this.groupSizeVal = groupSizeVal;
    }

    public int getMaxAgeVal() {
        return maxAgeVal;
    }

    public void setMaxAgeVal(int maxAgeVal) {
        this.maxAgeVal = maxAgeVal;
    }

    public CheckBox getOther() {
        return other;
    }

    public void setOther(CheckBox other) {
        this.other = other;
    }

    public EditText getStudyGroupType() {
        return studyGroupType;
    }

    public void setStudyGroupType(EditText studyGroupType) {
        this.studyGroupType = studyGroupType;
    }

    public EditText getSubjectType() {
        return subjectType;
    }

    public void setSubjectType(EditText subjectType) {
        this.subjectType = subjectType;
    }
}
