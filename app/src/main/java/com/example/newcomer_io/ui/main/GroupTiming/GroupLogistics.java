package com.example.newcomer_io.ui.main.GroupTiming;

import android.os.Bundle;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.newcomer_io.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
This fragment contains the necessary logistics to define the Title, start, end time or all day
 */
public class GroupLogistics extends Fragment {

    private ConstraintLayout constraintLayout;
    private OnClickTimeSet mListener;

    private EditText title;

    private Switch textDetails;

    private Date startDate_Day;
    private Date endDate_Day;

    private Date startDate_Time;
    private Date endDate_Time;

    private TextView startTime;
    private TextView endTime;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
   //private static final String ARG_PARAM1 = "param1";
    //private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
   // private String mParam1;
   // private String mParam2;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mListener = (OnClickTimeSet) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment0
        //mListener = (OnClickTimeSet) getContext();
        View inflate = inflater.inflate(R.layout.fragment_group_logistics, container, false);
        startTime = inflate.findViewById(R.id.startTime);
        endTime = inflate.findViewById(R.id.endTime);
        title = inflate.findViewById(R.id.editText);
        //Edit text paramaters
        title.setHint("Event Title");

        startTime.setText("Click to set");
        endTime.setText("Click to set");


        textDetails = new Switch(inflate);

        constraintLayout = inflate.findViewById(R.id.frameLayout);
        textDetails.setSwitchListener();

        startTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.sendButtonClick_startTime(); // Sends the button click back to the main UI
            }
        });
        endTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.sendButtonClick_endTime();
            }
        });
        return inflate;
    }

    private void creatCalendarBox(View v) {
        Date currentTime= Calendar.getInstance().getTime();
        CalendarView calendarView = new CalendarView(getContext());
        calendarView.setDate(currentTime.getTime(), false,true);

        long time = currentTime.getTime() + (1000* 3600 * 24 * 14);

        calendarView.setMaxDate(time);
        calendarView.setMinDate(currentTime.getTime());

        calendarView.setLayoutParams(new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT,ConstraintLayout.LayoutParams.MATCH_PARENT));
        calendarView.setBackgroundColor(getResources().getColor(R.color.white));

        constraintLayout.addView(calendarView);

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {

            }
        });
    }
    public Switch getSwitch(){
         return textDetails;
    }
    public void setStartTime(String txt){

        startTime.setText(txt);
        return;
    }
    public void setEndTime(String txt){
        endTime.setText(txt);
        return;
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
    public EditText getTitle(){
        return title;
    }
    public Date getStartDate_Time() {
        return startDate_Time;
    }

    public void setStartDate_Time(Date startDate_Time) {
            this.startDate_Time = startDate_Time;
    }

    public Date getEndDate_Time() {
        return endDate_Time;
    }

    public void setEndDate_Time(Date endDate_Time) {
            this.endDate_Time = endDate_Time;
    }


    public interface OnClickTimeSet{
        void sendButtonClick_startTime();
        void sendButtonClick_endTime();
    }
    public class Switch{
        private android.widget.Switch  time_dallDay;
        private TextView display;
        public Switch(View v){

            time_dallDay = v.findViewById(R.id.switchf);
            display = v.findViewById(R.id.textView);
            display.setText("Time");
            time_dallDay.setChecked(true);

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
                                endDate_Time = startDate_Time;
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

    }

}
