package com.example.newcomer_io.ui.main.GroupTiming;

import android.app.Activity;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import com.example.newcomer_io.R;

import java.text.SimpleDateFormat;
import java.util.Date;

public class GroupSettings{

    private View fragment_groupLogistics;

    private TextView startTime;
    private TextView endTime;
    private android.widget.Switch time_dallDay;
    private TextView display;

    private EditText eventTitle;

    private Date startDate;
    private Date endDate;

    private static String STARTTIME = "Start Time";
    private static String ENDTIME = "End Time";

    private Date startDate_Day;
    private Date startDate_Time;
    private Date endDate_Time;
    private Date endDate_Day;
    private Activity mMainActivity;

    LinearLayout startTime_Horizontal;
    LinearLayout endTime_Horizontal;

    public GroupSettings(Activity context){

        mMainActivity = context;
        LinearLayout scroll = context.findViewById(R.id.layout);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View fragment_groupLogistics = inflater.inflate(R.layout.fragment_group_logistics, scroll, true);        //Initializing mportant variables

        startTime_Horizontal = fragment_groupLogistics.findViewById(R.id.startTime_Horiziontal);
        endTime_Horizontal = fragment_groupLogistics.findViewById(R.id.endTime_Horizontal);

        eventTitle = fragment_groupLogistics.findViewById(R.id.editText);

        startTime = fragment_groupLogistics.findViewById(R.id.startTime);
        endTime = fragment_groupLogistics.findViewById(R.id.endTime);
        startTime.setText("Click to set");
        endTime.setText("Click to set");

        time_dallDay = fragment_groupLogistics.findViewById(R.id.switchf);
        time_dallDay.setChecked(true);

        display = fragment_groupLogistics.findViewById(R.id.textView);
        display.setText("All Day");

        startTimeListener();
        endTimeListener();
        setSwitchListener();
        setLayoutListeners();

    }

    private void setLayoutListeners() {
        startTime_Horizontal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                displayCalendarDialog(STARTTIME);
                setStartClick(); 

            }
        });
        endTime_Horizontal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Set the border of the click to be a different colou

                displayCalendarDialog(ENDTIME);
                setEndClick();


            }
        });
    }

    public TextView getStartTime(){
        return this.startTime;
    }
    public TextView getEndTime(){
        return this.endTime;
    }

    private void setEndClick() {
        new CountDownTimer(50, 1000) {

            public void onTick(long millisUntilFinished) {
                endTime_Horizontal.setBackgroundResource(R.drawable.border);
            }

            public void onFinish() {
                endTime_Horizontal.setBackgroundResource(0);
            }
        }.start();
    }

    private void setStartClick() {
        new CountDownTimer(50, 1000) {

            public void onTick(long millisUntilFinished) {
                startTime_Horizontal.setBackgroundResource(R.drawable.border);
            }

            public void onFinish() {
                startTime_Horizontal.setBackgroundResource(0);
            }
        }.start();
    }

    public EditText getEventTitle(){
        return eventTitle;
    }

    public void resetTimes(){
        startTime.setText("Click to set");
        endTime.setText("Click to set");

        startDate_Day = null;
        startDate_Time = null;
        endDate_Time = null;
        endDate_Day = null;

    }

    public Date getstartDate_Day() {
        return startDate_Day;
    }

    public void setstartDate_Day(Date startDate_Day) {
        this.startDate_Day = startDate_Day;
    }

    public Date getendDate_Day() {
        return endDate_Day;
    }

    public void setEndDate_Day(Date endDate_Day) {
        //Start setting the date
        //If all day is checked, then we want to ensure that this the set to all day
        this.endDate_Day = endDate_Day;
    }

    private void endTimeListener() {
        endTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setEndClick();
                displayCalendarDialog(ENDTIME);
            }
        });
    }

    private void startTimeListener() {
        startTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setStartClick();
                displayCalendarDialog(STARTTIME);
            }
        });

    }
    private void displayCalendarDialog(String TIME) {
        boolean switchf = time_dallDay.isChecked();

        DialogFragment newFragment = new CalendarDialogFragment(TIME,startDate,endDate,switchf);
        newFragment.show(((AppCompatActivity) mMainActivity).getSupportFragmentManager(), "datepicker");

    }
    public Date getEndDate_Time() {
        return endDate_Time;
    }

    public void setStartDate_Time(Date startDate_Time) {
        this.startDate_Time = startDate_Time;
    }

    public void setEndDate_Time(Date endDate_Time) {
        this.endDate_Time = endDate_Time;
    }
    public Date getStartDate_Time() {
        return startDate_Time;
    }
    public void setStartTime(String txt){
        startTime.setText(txt);
        return;
    }

    public android.widget.Switch getSwitch(){
        return time_dallDay;
    }
    public boolean getTimeSwitch_Checked(){
        return time_dallDay.isChecked();
    }
    public void setSwitchListener(){
        time_dallDay.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked == true){
                    //WE also want to check whether or not it was previously setz
                    SimpleDateFormat newFormat_Day = new SimpleDateFormat("E, MMMM dd ");
                    SimpleDateFormat newFormat_clock = new SimpleDateFormat("hh:mm a");

                    if (startDate_Day != null && startDate_Time != null){
                        //Then we can set the start date
                        String startTime_Str = newFormat_clock.format(startDate_Time);
                        String startDay_Str = newFormat_Day.format(startDate_Day);

                        startTime.setText(startDay_Str+ ": " + startTime_Str);

                    }
                    if (endDate_Day != null && startDate_Time != null){
                        String endTime_Str = newFormat_clock.format(endDate_Time);
                        String endDay_Str = newFormat_Day.format(endDate_Day);

                        endTime.setText(endDay_Str + ": " + endTime_Str);

                    }

                    display.setText("Time");
                }
                else{
                    SimpleDateFormat  newFormat_Day = new SimpleDateFormat("E, MMMM dd ");
                    if (startDate_Day != null){
                        String startDay_Str =  newFormat_Day.format(startDate_Day);
                        startTime.setText(startDay_Str);
                        endTime.setText(startDay_Str);
                        if (endDate_Day == null){
                            endDate_Day = startDate_Day;
                        }
                    }
                    if (endDate_Day != null){
                        String endDay_Str = newFormat_Day.format(endDate_Day);
                        endTime.setText(endDay_Str);
                        if (startDate_Day == null){
                            //Then we want to set that at as well
                            startDate_Day = endDate_Day;
                            startDate_Time = endDate_Time;
                            startTime.setText(endDay_Str);
                        }
                    }

                    //We also need to set the day text to be the day, Month


                    display.setText("All Day");
                }
            }
        });
    }
    public void setEndTime(String txt){
        endTime.setText(txt);
        return;
    }
}
