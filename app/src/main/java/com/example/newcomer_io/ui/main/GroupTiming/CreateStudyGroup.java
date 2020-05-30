package com.example.newcomer_io.ui.main.GroupTiming;

import android.content.DialogInterface;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.*;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.fragment.app.DialogFragment;
import com.example.newcomer_io.R;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CreateStudyGroup extends AppCompatActivity implements CalendarDialogFragment.OnClickDate, TimePickerFragment.OnInputListener {

    private View GroupTiming;
    private View LocationLogistics;
    private View SubjectLogistics;

    private static String STARTTIME = "Start Time";
    private static String ENDTIME = "End Time";

    private TextView startTime_Txt;
    private TextView endTime_Txt;

    private Date startTime_Date;
    private Date endTime_Date;

    private android.widget.Switch timing_CheckBox;

    public static String getSTARTTIME() {
        return STARTTIME;
    }

    public static void setSTARTTIME(String STARTTIME) {
        CreateStudyGroup.STARTTIME = STARTTIME;
    }

    public static String getENDTIME() {
        return ENDTIME;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_study_group);

        LinearLayout scroll = findViewById(R.id.scrollView);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(this.LAYOUT_INFLATER_SERVICE);
        LayoutInflater inflater1 = (LayoutInflater) this.getSystemService(this.LAYOUT_INFLATER_SERVICE);
        LayoutInflater inflater2= (LayoutInflater) this.getSystemService(this.LAYOUT_INFLATER_SERVICE);


        GroupTiming = inflater1.inflate(R.layout.fragment_group_logistics, scroll, true);
        LocationLogistics = inflater1.inflate(R.layout.fragment_location_logistics,scroll,true);
        SubjectLogistics = inflater2.inflate(R.layout.fragment_group_subject_logistics,scroll,true);

        //First we want to set the Location & SubjectLogistics to have a border that is different
        LinearLayout groupTiming_Border = GroupTiming.findViewById(R.id.timing_Layout);
        LinearLayout locationLogistics_Border = LocationLogistics.findViewById(R.id.location_Layout);
        LinearLayout subjectLogistics_Border = SubjectLogistics.findViewById(R.id.subject_Layout);

        locationLogistics_Border.setBackgroundResource(R.drawable.rounded_border_deselected);
        subjectLogistics_Border.setBackgroundResource(R.drawable.rounded_border_deselected);

        //subjectLogistics_Border.setBackground(getResources().getDrawable(R.drawable.rounded_border_deselected));
        //locationLogistics_Border.setBackground(getResources().getDrawable(R.drawable.rounded_border_deselected));
        //locationLogistics_Border.setBackgroundDrawable(ContextCompat.getDrawable(this,R.drawable.rounded_border_deselected));
        //    layout.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.ready) );
        //subjectLogistics_Border.setBackgroundDrawable(ContextCompat.getDrawable(this,R.drawable.rounded_border_deselected));

        groupTiming_Border.setBackground(getResources().getDrawable(R.drawable.rounded_border));

        activitateGroupTiming(GroupTiming);

    }
    private void setStage_Highlight(int stageNum){
        LinearLayout groupTiming_Border = getGroupTiming().findViewById(R.id.timing_Layout);
        LinearLayout locationLogistics_Border =getLocationLogistics().findViewById(R.id.location_Layout);
        LinearLayout subjectLogistics_Border = getSubjectLogistics().findViewById(R.id.subject_Layout);

        if (stageNum == 1){
            //Aka if the user is on the first stage and they haven't pressed anything y
            groupTiming_Border.setBackgroundResource(R.drawable.rounded_border);
            locationLogistics_Border.setBackgroundResource(R.drawable.rounded_border_deselected);
            subjectLogistics_Border.setBackgroundResource(R.drawable.rounded_border_deselected);
        }else if(stageNum == 2){
            groupTiming_Border.setBackgroundResource(R.drawable.rounded_border_deselected);
            locationLogistics_Border.setBackgroundResource(R.drawable.rounded_border);
            subjectLogistics_Border.setBackgroundResource(R.drawable.rounded_border_deselected);
        }
        else{
            groupTiming_Border.setBackgroundResource(R.drawable.rounded_border_deselected);
            locationLogistics_Border.setBackgroundResource(R.drawable.rounded_border_deselected);
            subjectLogistics_Border.setBackgroundResource(R.drawable.rounded_border);
        }
    }
    private boolean getStageConfirmation(int stageNum){

        if (stageNum == 1){
            //We want to ensure that hte start date, end date and the the group name are set, if so, then we can proceed with highlighting the next stage
            View groupTiming = getGroupTiming();
            EditText studyGroupTitle = groupTiming.findViewById(R.id.groupTitle);
            TextView startTime_txt = getStartTime_Txt();
            TextView endTime_txt = getEndTime_Txt();
            if (endTime_txt.getText().toString().equals("Click To Set") == false &&
                    startTime_txt.getText().toString().equals("Click To Set") == false &&
                     studyGroupTitle.getText().toString().equals("") == false ){
                return true; //meaning that we have confirmed that this stage is okay
            }else{
                return false;
            }

        }else if(stageNum == 1){
        //Otherwise if all of hte values on stage 2 are correct then we can proceed
            View locationLogistics = getLocationLogistics();
            EditText location = locationLogistics.findViewById(R.id.location);
            EditText meetingNotes = locationLogistics.findViewById(R.id.meetingNotes);
            return true;

        }
        else{
            //stage 3
            return true;
        }
    }

    private void activitateGroupTiming(final View groupTiming) {
        LinearLayout startTime =groupTiming.findViewById(R.id.startTime_Horiziontal);
        LinearLayout endTime = groupTiming.findViewById(R.id.endTime_Horizontal);
        timing_CheckBox = groupTiming.findViewById(R.id.switchf);

        setStartTime_Txt((TextView) groupTiming.findViewById(R.id.startTime));
        setEndTime_Txt((TextView) groupTiming.findViewById(R.id.endTime));

        final TextView timing_Txt = groupTiming.findViewById(R.id.timing);

        EditText studyGroupTitle = groupTiming.findViewById(R.id.groupTitle);

        //Set the done button in the IME_ACTION_DONE
        studyGroupTitle.setImeOptions(EditorInfo.IME_ACTION_DONE);
        studyGroupTitle.setSingleLine(true);

        studyGroupTitle.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                ////Then we know that the user has navigated to the next page
                if (getStageConfirmation(1) == true){
                    setStage_Highlight(2);
                }

                return false;
            }
        });

        timing_CheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    //then we set the text of the timing
                    timing_Txt.setText("All Day");
                    if (getStartTime_Txt().equals("Click To Set") == false || getEndTime_Txt().equals("Click To Set") == false){
                        //Then we set it back
                        getStartTime_Txt().setText("Click To Set");
                        getEndTime_Txt().setText("Click To Set");
                    }
                }
                else{
                    timing_Txt.setText("Time Frame");
                    if (getStartTime_Txt().equals("Click To Set") == false || getEndTime_Txt().equals("Click To Set") == false){
                        //Then we set it back
                        getStartTime_Txt().setText("Click To Set");
                        getEndTime_Txt().setText("Click To Set");
                    }
                }
            }
        });

        startTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Then they have selected all day, then we have the day set to be the same
                DialogFragment calendar = new CalendarDialogFragment(STARTTIME);
                calendar.show(getSupportFragmentManager(), "datepicker");

            }
        });
        endTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment calendar = new CalendarDialogFragment(ENDTIME);
                calendar.show(getSupportFragmentManager(), "datepicker");
            }
        });
    }

    public View getGroupTiming() {
        return GroupTiming;
    }

    public void setGroupTiming(View groupTiming) {
        GroupTiming = groupTiming;
    }

    public View getLocationLogistics() {
        return LocationLogistics;
    }

    public void setLocationLogistics(View locationLogistics) {
        LocationLogistics = locationLogistics;
    }

    public View getSubjectLogistics() {
        return SubjectLogistics;
    }

    public void setSubjectLogistics(View subjectLogistics) {
        SubjectLogistics = subjectLogistics;
    }

    public android.widget.Switch getTiming() {
        return timing_CheckBox;
    }

    public void setTiming(android.widget.Switch timing) {
        this.timing_CheckBox = timing;
    }

    @Override
    public void sendDate(int year, int month, int dayOfMonth, String type) {
        Switch timing = getTiming();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("E, MMMM dd");
        Date date = new Date(year, month, dayOfMonth);
        if (timing.isChecked()){
            //We set both the start and end times since they both occur on the same date
            setStartTime_Date(date);
            setEndTime_Date(date);

            //And we also set the text of of the current date
            String format = simpleDateFormat.format(date);
            getStartTime_Txt().setText(format);
            getEndTime_Txt().setText(format);

        }else if(type.equals(STARTTIME)){

            //then we set the start time
            setStartTime_Date(date);
            DialogFragment newFragment = new TimePickerFragment(type);
            newFragment.show(getSupportFragmentManager(), type);

        }
        else{
            setEndTime_Date(date);
            //then we set the end time
            DialogFragment newFragment = new TimePickerFragment(type);
            newFragment.show(getSupportFragmentManager(), type);
        }

        if (getStageConfirmation(1) == true){
            setStage_Highlight(2);
        }
    }

    public TextView getEndTime_Txt() {
        return endTime_Txt;
    }

    public void setEndTime_Txt(TextView endTime_Txt) {
        this.endTime_Txt = endTime_Txt;
    }

    public TextView getStartTime_Txt() {
        return this.startTime_Txt;
    }

    public void setStartTime_Txt(TextView startTime_Txt) {
        this.startTime_Txt = startTime_Txt;
    }

    @Override
    public void sendDate_clock(TimePicker view, int hourOfDay, int minute, String paramType) {
        SimpleDateFormat clock_format_Time = new SimpleDateFormat("hh:mm a");
        SimpleDateFormat clock_format_Days = new SimpleDateFormat("E, MMMM dd");
        Date date = new Date();
        date.setHours(hourOfDay);
        date.setMinutes(minute);

        TextView startTime_txt = getStartTime_Txt();
        TextView endTime_txt = getEndTime_Txt();

        Date startTime_date = getStartTime_Date();
        Date endTime_date = getEndTime_Date();

        if (paramType.equals(STARTTIME)== true){
            startTime_date.setHours(hourOfDay);
            startTime_date.setMinutes(minute);
            setStartTime_Date(startTime_date);

            startTime_txt.setText(clock_format_Days.format(startTime_date) + " : " + clock_format_Time.format(startTime_date));
            promptStart_EndStringValidity(startTime_txt, endTime_txt, startTime_date, endTime_date);
        }
        else{

            endTime_date.setHours(hourOfDay);
            endTime_date.setMinutes(minute);
            setEndTime_Date(endTime_date);

            endTime_txt.setText(clock_format_Days.format(endTime_date) + " : " + clock_format_Time.format(endTime_date));
            promptStart_EndStringValidity(startTime_txt,endTime_txt,startTime_date,endTime_date);
        }

        if (getStageConfirmation(1) == true){
            setStage_Highlight(2);
        }

    }

    private void promptStart_EndStringValidity(TextView startTime_txt1, TextView endTime_txt1, Date startTime_date, Date endTime_date) {
        if (startTime_txt1.getText().toString().equals("Click To Set") == false && endTime_txt1.getText().toString().equals("Click To Set") == false){
            //Then we compare the dates and if they are timings that do not make sense , then we throw the error
            if (compareDates(startTime_date,endTime_date) == true){
                //Therefore we conclude that the start time is bigger than the end time
                errorDialogBox("Invalid Dates", "Please choose a start date before the end date");
                //and we reset the text on those boxes
                startTime_txt1.setText("Click To Set");
                endTime_txt1.setText("Click To Set");
            }
        }
    }

    private boolean compareDates(Date startTime_date, Date endTime_date) {
        long time = startTime_date.getTime();
        long time1 = endTime_date.getTime();
        if (time > time1){
            //Then we return tru, yes the start date is bigger than the end date
            return true;
        }else{
            return false;
        }
    }

    public void errorDialogBox(String title, String errorMessage){
        AlertDialog alertDialog = new AlertDialog.Builder(CreateStudyGroup.this).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(errorMessage);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();

    }

    public Date getEndTime_Date() {
        return endTime_Date;
    }

    public void setEndTime_Date(Date endTime_Date) {
        this.endTime_Date = endTime_Date;
    }

    public Date getStartTime_Date() {
        return startTime_Date;
    }

    public void setStartTime_Date(Date startTime_Date) {
        this.startTime_Date = startTime_Date;
    }
}
