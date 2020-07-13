package com.example.newcomer_io.ui.main.EventDetails;

import android.graphics.Typeface;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.InputType;
import android.text.Layout;
import android.text.TextWatcher;
import android.view.*;
import android.view.inputmethod.EditorInfo;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.interfaces.OnSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.widgets.CrystalRangeSeekbar;
import com.crystal.crystalrangeseekbar.widgets.CrystalSeekbar;
import com.example.newcomer_io.R;
import com.plumillonforge.android.chipview.Chip;
import com.plumillonforge.android.chipview.ChipView;
import com.plumillonforge.android.chipview.OnChipClickListener;

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
    private TextView timeFrame;
    private CrystalSeekbar timeFrameSeekbar;
    private List<Chip> chipList;

    private EditText subjectType;
    private TextView tagCompleteSuggestion;
    private ChipView chipDefault;
    private CrystalSeekbar groupSize;
    private TextView size;
    private LinearLayout groupSizeLayout;
    private EditText studyGroupType;
    private boolean added;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_view);

        groupSizeLayout = findViewById(R.id.groupSizeLayout);
        timeFrameSeekbar = findViewById(R.id.timeFrameSeekbar);
        ageRange = findViewById(R.id.rangeSeekbar5);
        other = findViewById(R.id.other);
        studyGroupType= findViewById(R.id.bySubject);
        chipList = new ArrayList<Chip>();

        //chipList.add(new Tag("Lorem"));
        //chipList.add(new Tag("Ipsum dolor"));
        //chipList.add(new Tag("Sit amet"));
        //chipList.add(new Tag("Consectetur"));
        //chipList.add(new Tag("adipiscing elit"));

        chipDefault = (ChipView) findViewById(R.id.chipview);
        chipDefault.setChipLayoutRes(R.layout.chip_close);
        chipDefault.setChipBackgroundColor(getResources().getColor(R.color.transparent));

        //chipDefault.setChipBackgroundColor(R.color.wizeblue);
        //chipDefault.setChipList(chipList);
        this.added = false;

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


        other.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                final LinearLayout studyGroupTheme = findViewById(R.id.studyGroupType);
                if (isChecked){

                    final EditText editText = new EditText(getApplicationContext());
                    editText.setImeOptions(EditorInfo.IME_ACTION_DONE);
                    editText.setSingleLine(true);
                    setStudyGroupType(editText);
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    layoutParams.gravity = Gravity.CENTER;

                    editText.setHint("Theme");
                    editText.setLayoutParams(layoutParams);

                    studyGroupTheme.addView(editText);
                    editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                        @Override
                        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                            other.setText(editText.getText().toString());
                            editText.clearFocus();

                            return false;
                        }
                    });

                }else{
                    other.setText("Other");
                    studyGroupTheme.removeView(getStudyGroupType());
                }

            }
        });

        studyGroupType.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                String s = v.getText().toString();
                if (s.endsWith(",") == true){
                    chipList.add(new Tag(s.split(",")[0]));
                    //chipDefault.set
                    v.setText("");
                }

                return false;
            }
        });
        studyGroupType.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (isAdded() == false){
                    setAdded(true);
                    final TextView textView = new TextView(getApplicationContext());
                    textView.setText("Comma Separated");
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    layoutParams.gravity = Gravity.CENTER;
                    textView.setLayoutParams(layoutParams);
                    Typeface typeface =Typeface.create("open-sans", Typeface.ITALIC);
                    textView.setTextSize(10);
                    textView.setTypeface(typeface);
                    final LinearLayout bySubjectLayout = findViewById(R.id.bySubjectLayout);
                    bySubjectLayout.addView(textView);

                    new CountDownTimer(3000, 1000) {

                        public void onTick(long millisUntilFinished) {
                        }

                        public void onFinish() {
                            bySubjectLayout.removeView(textView);
                        }
                    }.start();
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String s1 = s.toString();
                //String word = s1.split(",")[0];

                if (s1.endsWith(",") == true && s1.length() != 1){
                    String[] split = s1.split(",");
                    String s2 = split[0].toUpperCase().trim();

                    if (s2.equals("") == false){
                        getStudyGroupType().setText("");

                        chipList.add(new Tag(s2));


                        getChipDefault().setChipList(chipList);
                        getChipDefault().setOnChipClickListener(new OnChipClickListener() {
                            @Override
                            public void onChipClick(Chip chip) {
                                //Then we remove it
                                List<Chip> chipList = getChipList();
                                chipList.remove(chip);
                                getChipDefault().setChipList(chipList);
                            }
                        });
                    }else{
                        getStudyGroupType().setText("");
                    }
                }else if(s1.endsWith(",") == true && s1.length() == 1){
                    //Then we can just reset it
                    getStudyGroupType().setText("");
                }
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

    public TextView getTagCompleteSuggestion() {
        return tagCompleteSuggestion;
    }

    public void setTagCompleteSuggestion(TextView tagCompleteSuggestion) {
        this.tagCompleteSuggestion = tagCompleteSuggestion;
    }

    public boolean isAdded() {
        return added;
    }

    public void setAdded(boolean added) {
        this.added = added;
    }

    public ChipView getChipDefault() {
        return chipDefault;
    }

    public void setChipDefault(ChipView chipDefault) {
        this.chipDefault = chipDefault;
    }
    public List<Chip> getChipList(){return this.chipList; }
}
