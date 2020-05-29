package com.example.newcomer_io.ui.main.GroupTiming;

import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.view.MotionEvent;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.fragment.app.DialogFragment;
import com.example.newcomer_io.*;
import com.example.newcomer_io.ui.main.EventDetails.GroupConfirmation;
import com.example.newcomer_io.ui.main.LocationSettings.LocationType;
import com.example.newcomer_io.ui.main.LocationSettings.TrendingContent;
import com.example.newcomer_io.ui.main.LocationSettings.TrendingDisplay;
import com.example.newcomer_io.ui.main.UserDetails.EventCreate;
import com.example.newcomer_io.ui.main.UserDetails.UserData;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

public class CreateGroup extends AppCompatActivity implements GroupLogistics.OnClickTimeSet, CalendarDialogFragment.OnClickDate, TimePickerFragment.OnInputListener{

    private static final int AUTOCOMPLETE_REQUEST_CODE = 1;
    private static String STARTTIME = "Start Time";
    private static String ENDTIME = "End Time";
    private Date startDate;
    private Date endDate;
    private FloatingActionButton createTheGroup;
    private LocationLogistics locationLogistics;
    private GroupSettings groupLogistics;
    private UserData userData;
    private EventCreate eventCreate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);

        userData = (UserData) getApplicationContext();

        groupLogistics = new GroupSettings(this);
        locationLogistics = new LocationLogistics(this);

        if (userData.getEventCreate() == null){

            eventCreate = new EventCreate(this);
            userData.setEventCreate(eventCreate);

        }else{
            eventCreate = userData.getEventCreate();
            fillEventDetails(eventCreate,userData.getChosenContent());

        }
        createTheGroup = locationLogistics.getFragment_groupLocation_View().findViewById(R.id.floatingActionButton);

        createTheGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean b = checkValidity();//we need to check the validity of the paramaters that the user has entered before going to the next
                if (!b){
                    Intent intent = new Intent(CreateGroup.this, GroupConfirmation.class);
                    setValues();
                    userData.pushGroupCreationUpdates();
                    startActivity(intent);
                }
            }
        });

        locationLogisticsListeners(locationLogistics);

    }

    private void locationLogisticsListeners(LocationLogistics locationLogistics) {

        EditText locationVal = locationLogistics.getLocationVal();
        locationVal.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    //We also want to fill all of the values
                    setValues();
                    Intent intent = new Intent(v.getContext(), TrendingDisplay.class);
                    startActivity(intent);

                }
                return false;
            }
        });


        final Spinner groupSpinner = locationLogistics.getGroupSpinner();
        final int MAXSPINNERSIZE = locationLogistics.getMaxSpinnerSize();
        final int[] arr_GroupSize = locationLogistics.getArr_groupSize();
        final EditText customAge = locationLogistics.getAgeCustom();
        groupSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != MAXSPINNERSIZE){//aka the last value in the spinner
                    //Then we display the edit text
                    int sizeoMadeo = arr_GroupSize[position];

                    eventCreate.setGroupSize(sizeoMadeo);
                    customAge.setVisibility(View.INVISIBLE);
                }
                else{
                    customAge.setVisibility(View.VISIBLE);

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        customAge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eventCreate.setGroupSize(Integer.parseInt(customAge.getText().toString()));
            }
        });

    }

    private void setValues() {

        EditText customAge = locationLogistics.getAgeCustom();
        Spinner groupSpinner = locationLogistics.getGroupSpinner();
        int MAXSPINNERSIZE = locationLogistics.getMaxSpinnerSize();
        EditText locationVal = locationLogistics.getLocationVal();
        EditText eventNotes = locationLogistics.getEventNotes();
        EditText locationName = locationLogistics.locationName();
        EditText eventTitle = groupLogistics.getEventTitle();
        Switch aSwitch = groupLogistics.getSwitch();

        if (eventNotes.length() != 0){
            eventCreate.setEventNotes(eventNotes.getText().toString());
        }
        if (eventNotes.length() != 0){
            eventCreate.setEventNotes(eventNotes.getText().toString());
        }
        if (groupSpinner.getId() == MAXSPINNERSIZE){
            eventCreate.setGroupSize(Integer.parseInt(customAge.getText().toString()));
        }
        if (eventTitle.length() != 0) {
            eventCreate.setEventName(eventTitle.getText().toString());
        }
        if (locationName.length() != 0){
            eventCreate.setLocationName(locationName.getText().toString());
        }

        eventCreate.setSwitchParam(aSwitch.isChecked());
    }

    private void fillEventDetails(EventCreate eventCreate, TrendingContent chosenContent) {
        String EventNotes = eventCreate.getEventNotes();
        String eventName = eventCreate.getEventName();
        String endTime_txt = eventCreate.getEndTime_txt();
        String startTime_txt = eventCreate.getStartTime_txt();
        int groupSize1 = eventCreate.getGroupSize();
        String ageMin1 = eventCreate.getAgeMin();

        String ageMax1 = eventCreate.getAgeMax();
        boolean switchParam = eventCreate.getSwitchParam();

        Date endTime1 = eventCreate.getEndTime();
        Date endTime_day = eventCreate.getEndTime_Day();
        endDate = endTime_day;
        groupLogistics.setEndDate_Day(endTime_day);
        groupLogistics.setEndDate_Time(endTime1);

        Date startTime_day = eventCreate.getStartTime_Day();
        Date startTime1 = eventCreate.getStartTime();
        startDate = startTime_day;
        groupLogistics.setstartDate_Day(startTime_day);
        groupLogistics.setStartDate_Time(startTime1);

        ///-------------------------Location Logistic Paramaters ---------------------------------------///

        EditText eventNotes = locationLogistics.getEventNotes();
        EditText locationName = locationLogistics.locationName();
        EditText ageCustom = locationLogistics.getAgeCustom();
        ArrayAdapter<String> arrayAdapter = locationLogistics.getArrayAdapter();

        EditText eventTitle = groupLogistics.getEventTitle();
        TextView endTime = groupLogistics.getEndTime();
        TextView startTime = groupLogistics.getStartTime();
        Switch aSwitch = groupLogistics.getSwitch();

        Spinner spinner = locationLogistics.getGroupSpinner();
        int spinnerId = locationLogistics.getSpinnerId(groupSize1);
        int MAXSIZE = locationLogistics.getMaxSpinnerSize();

        if (chosenContent != null){
            String eventLocation = chosenContent.getPlaceName();
            locationName.setText(eventLocation);
        }
        eventNotes.setText(EventNotes);

        eventTitle.setText(eventName);

        if (endTime_txt.equals("Click to set") == false){
            endTime.setText(endTime_txt);
        }
        if (startTime_txt.equals("Click to set") == false){
            startTime.setText(startTime_txt);
        }

        if (spinnerId == MAXSIZE){
            //then we also update the age custom
            ageCustom.setText(String.valueOf(groupSize1));
        }
        spinner.setSelection(spinnerId);
        aSwitch.setChecked(switchParam);


    }

    private boolean checkValidity() {
        boolean returnFlag = false;
        if (startDate == null || endDate == null){
            //then we display the error message
            errorDialogBox("Invalid Timing", "Please select valid start and end times");
            returnFlag = true;
        }
        //With the remaining, ltee' sjust set error hint s
        EditText getLocation;
        EditText minAge;
        EditText maxAge;

        getLocation = locationLogistics.getLocationVal();
        EditText title = groupLogistics.getEventTitle();

        int minimumAge = 0;
        int maximumAge = 0;

        if (title.getText().toString().length() == 0) {
            title.setError("Please enter an event title");
            returnFlag = true;
        }
        if (getLocation.getText().toString().trim().length() == 0) {
                getLocation.setError("Please enter a location");
                returnFlag = true;
        }
        return returnFlag;
    }

    @Override
    public void sendButtonClick_startTime() {

        displayCalendarDialog(STARTTIME);
    }

    @Override
    public void sendButtonClick_endTime() {
        displayCalendarDialog(ENDTIME);
    }

    private void displayCalendarDialog(String TIME) {
        boolean switchf = groupLogistics.getTimeSwitch_Checked();

        DialogFragment newFragment = new CalendarDialogFragment(TIME,startDate,endDate,switchf);

        newFragment.show(getSupportFragmentManager(), "datepicker");

    }
    private void setStartDate(Date startDate_Day, Date startDate_Time,String txt){

        groupLogistics.setstartDate_Day(startDate_Day);
        groupLogistics.setStartDate_Time(startDate_Time);
        groupLogistics.setStartTime(txt);
        eventCreate.setStartTime(startDate_Time, startDate_Day,txt);

    }
    private void setEndDate(Date endDate_Day, Date endDate_Time,String txt){

        groupLogistics.setEndDate_Day(endDate_Day);
        groupLogistics.setEndDate_Time(endDate_Time);
        groupLogistics.setEndTime(txt);
        eventCreate.setEndTime(endDate_Time, endDate_Day,txt);

    }
    @Override
    public void sendDate(int year, int month, int dayOfMonth, String type) {

        //We also want to send the date of the start and end time so that we can update the text for whether or not it is all day or partial of a day

        boolean timeSwitch_checked = groupLogistics.getTimeSwitch_Checked();

        isIncorrectFormat();
        //Display the time picker dialog fragment
        if (type.equals(STARTTIME) == true){
            //Then we set the start time\
            Calendar myCalendar = new GregorianCalendar(year, month, dayOfMonth);
            startDate = myCalendar.getTime();
            displayTimeDialogFragment(STARTTIME);

        }
        else{
            Calendar myCalendar = new GregorianCalendar(year, month, dayOfMonth);
            endDate = myCalendar.getTime();

            displayTimeDialogFragment(ENDTIME);
        }

        //Update the fragment


    }

    private void displayTimeDialogFragment(String type) {
        DialogFragment newFragment = new TimePickerFragment(type);
        newFragment.show(getSupportFragmentManager(), type);
    }
    private void isIncorrectFormat() {
        Date startDate_Time = groupLogistics.getStartDate_Time();
        Date endDate_Time = groupLogistics.getEndDate_Time();
        if (startDate != null && endDate != null) {
            if (startDate.after(endDate) == true) {
                //then we need to reset because it is no longer
                groupLogistics.resetTimes();
                startDate = null;
                endDate = null;
                errorDialogBox("Invalid Dates", "Please choose a start date before the end date");
            }
            else if (startDate.compareTo(endDate) == 0){
                if(startDate_Time.after(endDate_Time) == true){
                    groupLogistics.resetTimes();
                    startDate = null;
                    endDate = null;
                    errorDialogBox("Invalid Dates", "Please choose a start date before the end date");
                }
            }
           }
    }
    public void errorDialogBox(String title, String errorMessage){
        AlertDialog alertDialog = new AlertDialog.Builder(CreateGroup.this).create();
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

    @Override
    public void sendDate_clock(TimePicker view, int hourOfDay, int minute, String paramType) {
        SimpleDateFormat clock_format = new SimpleDateFormat("hh:mm a");
        Date date = new Date();
        boolean timeSwitch_checked = groupLogistics.getTimeSwitch_Checked();

        date.setHours(hourOfDay);
        date.setMinutes(minute);

        String timeText = clock_format.format(date);


        if (paramType.equals(STARTTIME) == true){
            //Then we set the start time
            SimpleDateFormat newFormat = new SimpleDateFormat("E, MMMM dd ");

            if (timeSwitch_checked == false){
                //Just change the date
                String dateString = newFormat.format(startDate);
                setStartDate(startDate,date,dateString); //set the start date\

                if (endDate == null){
                    //Tjem we set the end date to be the start date
                    setEndDate(startDate,date,dateString);
                }
                else{
                    setEndDate(endDate,date,dateString);

                }
            }
            else{
                //otherwise we change the time as well
                String dateString = newFormat.format(startDate) + ": " + timeText;
                setStartDate(startDate,date,dateString); //set the start date\

            }

        }
        else{
            SimpleDateFormat newFormat = new SimpleDateFormat("E, MMMM dd ");
            String dateString = newFormat.format(endDate);
            //group_logistics_fragment.setEndTime(dateString + ": " + timeText);

            if (timeSwitch_checked == false){//It is set to all day
                //Just change the date
                //We set hte start date because we have set it all day
                if (startDate != null){
                    setEndDate(endDate,date,dateString);

                }
                else{
                    String enDa = newFormat.format(endDate);

                    setStartDate(endDate,date,dateString);
                    setEndDate(endDate,date,dateString);

                }

            }
            else{
                //otherwise we change the time as well
                setEndDate(endDate,date,dateString + ": " + timeText);
            }

        }
        isIncorrectFormat();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place asd = Autocomplete.getPlaceFromIntent(data);
                LatLng location = asd.getLatLng();

                Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                List<Address> addresses = null;
                try {
                    addresses = geocoder.getFromLocation(location.latitude,location.longitude, 1);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                String cityName = addresses.get(0).getAddressLine(0);
                locationLogistics.setLocationName(cityName);
                locationLogistics.setChecked(false);

            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                // TODO: Handle the error.
                Status status = Autocomplete.getStatusFromIntent(data);

            }
        }
    }
}
