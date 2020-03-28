package com.example.newcomer_io;

import android.content.pm.PackageManager;
import android.view.View;
import android.widget.TimePicker;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.example.newcomer_io.ui.main.GroupLogistics;
import com.example.newcomer_io.ui.main.TimePickerFragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class CreateGroup extends AppCompatActivity implements GroupLogistics.OnClickTimeSet, CalendarDialogFragment.OnClickDate, TimePickerFragment.OnInputListener{

    private static String STARTTIME = "Start Time";
    private static String ENDTIME = "End Time";
    private Date startDate;
    private Date endDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);
        //Now we create the group in this class
        createGroupLogisticsFrag();
    }


    private void createGroupLogisticsFrag() {
        NestedScrollView nestedScrollView = findViewById(R.id.nestedScrollView);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        Fragment groupLogistics = new GroupLogistics();
        fragmentTransaction.add(nestedScrollView.getId(),groupLogistics,"Group Logistics Fragment");
        fragmentTransaction.commit();

    }

    @Override
    public void sendButtonClick_startTime() {
        displayCalendarDialog(STARTTIME);
    }

    @Override
    public void sendButtonClick_endTime() {
        displayCalendarDialog(ENDTIME);
    }

    private void displayCalendarDialog(String STARTTIME) {

        DialogFragment newFragment = new CalendarDialogFragment(STARTTIME);
        newFragment.show(getSupportFragmentManager(), "datepicker");

    }

    @Override
    public void sendDate(int year, int month, int dayOfMonth, String type) {

        //We also want to send the date of the start and end time so that we can update the text for whether or not it is all day or partial of a day
        GroupLogistics group_logistics_fragment = (GroupLogistics) getSupportFragmentManager().findFragmentByTag("Group Logistics Fragment");

        //Display the time picker dialog fragment
        if (type.equals(STARTTIME) == true){
            //Then we set the start time\
            startDate = new Date(year,month,dayOfMonth);
            group_logistics_fragment.setstartDate_Day(startDate);

            displayTimeDialogFragment(STARTTIME);
        }
        else{

            endDate = new Date(year,month,dayOfMonth);
            group_logistics_fragment.setEndDate_Day(endDate);

            displayTimeDialogFragment(ENDTIME);
        }

        //Update the fragment


    }

    private void displayTimeDialogFragment(String type) {
        DialogFragment newFragment = new TimePickerFragment(type);
        newFragment.show(getSupportFragmentManager(), type);
    }

    @Override
    public void sendDate_clock(TimePicker view, int hourOfDay, int minute, String paramType) {
        GroupLogistics group_logistics_fragment = (GroupLogistics) getSupportFragmentManager().findFragmentByTag("Group Logistics Fragment");

        SimpleDateFormat clock_format = new SimpleDateFormat("hh:mm a");
        Date date = new Date();

        date.setHours(hourOfDay);
        date.setMinutes(minute);

        String timeText = clock_format.format(date);
        if (paramType.equals(STARTTIME) == true){
            //Then we set the start time
            SimpleDateFormat newFormat = new SimpleDateFormat("E, MMMM dd ");
            group_logistics_fragment.setStartDate_Time(date);
            String dateString = newFormat.format(startDate);
            group_logistics_fragment.setStartTime(dateString + ": " + timeText);
        }
        else{
            SimpleDateFormat newFormat = new SimpleDateFormat("E, MMMM dd ");
            group_logistics_fragment.setEndDate_Time(date);
            String dateString = newFormat.format(endDate);
            group_logistics_fragment.setEndTime(dateString + ": " + timeText);
        }
    }
}
