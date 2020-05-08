package com.example.newcomer_io.ui.main.GroupTiming;

import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.*;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.fragment.app.DialogFragment;
import com.example.newcomer_io.*;
import com.example.newcomer_io.ui.main.LocationSettings.LocationType;
import com.example.newcomer_io.ui.main.UserDetails.EventCreate;
import com.example.newcomer_io.ui.main.UserDetails.UserData;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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
            fillEventDetails(eventCreate);

        }
        createTheGroup = locationLogistics.getFragment_groupLocation_View().findViewById(R.id.floatingActionButton);

        createTheGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkValidity(); //we need to check the validity of the paramaters that the user has entered before going to the next
            }
        });

        locationLogisticsListeners(locationLogistics);

        //scroll.addView(fragment_groupLogistics);
        // scroll.addView(fragment_groupLocation);

        //Now we create the group in th is class
        //createLocationLogisticsFrag();
        //createGroupLogisticsFrag();t
    }

    private void locationLogisticsListeners(LocationLogistics locationLogistics) {


        EditText locationVal = locationLogistics.getLocationVal();
        locationVal.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {

                    //We also want to fill all of the values

                    fillValues();

                    Intent intent = new Intent(v.getContext(), LocationType.class);
                    startActivity(intent);

                }
                return false;
            }
        });

        final Spinner groupSpinner = locationLogistics.getGroupSpinner();
        final int MAXSPINNERSIZE = locationLogistics.getMaxSpinnerSize();
        final int[] arr_GroupSize = locationLogistics.getArr_groupSize();
        /*
        if (position != MAXSPINNERSIZE){
                                int sizeoMadeo = arr_GroupSize[position];
                                eventCreate.setGroupSize(sizeoMadeo);
                            }
   if (position == MAXSPINNER){//aka the last value in the spinner
                    //Then we display the edit text
                   ageCustom.setVisibility(View.VISIBLE);
                }
                else{
                    //Then we hide
                    ageCustom.setVisibility(View.INVISIBLE);
                }
         */
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

    }

    private void fillValues() {

         EditText customAge = locationLogistics.getAgeCustom();
         Spinner groupSpinner = locationLogistics.getGroupSpinner();
         EditText ageMax = locationLogistics.getAgeMax();
         EditText ageMin = locationLogistics.getAgeMin();
         int MAXSPINNERSIZE = locationLogistics.getMaxSpinnerSize();

        EditText locationVal = locationLogistics.getLocationVal();
        EditText eventNotes = locationLogistics.getEventNotes();

        if (ageMin.length() != 0){
            eventCreate.setAgeMin(ageMin.getText().toString());
        }
        if (ageMax.length() != 0){
            eventCreate.setAgeMax(ageMax.getText().toString());
        }
        if (eventNotes.length() != 0){
            eventCreate.setEventNotes(eventNotes.getText().toString());
        }
        if (eventNotes.length() != 0){
            eventCreate.setEventNotes(eventNotes.getText().toString());
        }
        if (groupSpinner.getId() == MAXSPINNERSIZE){
            eventCreate.setGroupSize(MAXSPINNERSIZE);
        }

    }

    private void fillEventDetails(EventCreate eventCreate) {
        String EventNotes = eventCreate.getEventNotes();
        String eventName = eventCreate.getEventName();
        String endTime_txt = eventCreate.getEndTime_txt();
        String startTime_txt = eventCreate.getStartTime_txt();
        int groupSize1 = eventCreate.getGroupSize();
        String ageMin1 = eventCreate.getAgeMin();
        String eventLocation = eventCreate.getLocationName();
        String ageMax1 = eventCreate.getAgeMax();

        EditText eventNotes = locationLogistics.getEventNotes();
        EditText locationName = locationLogistics.locationName();
        EditText ageMin = locationLogistics.getAgeMin();
        EditText ageMax = locationLogistics.getAgeMax();
        EditText ageCustom = locationLogistics.getAgeCustom();
        ArrayAdapter<String> arrayAdapter = locationLogistics.getArrayAdapter();

        Spinner spinner = locationLogistics.getGroupSpinner();
        int spinnerId = locationLogistics.getSpinnerId(groupSize1);
        int MAXSIZE = locationLogistics.getMaxSpinnerSize();

        eventNotes.setText(EventNotes);
        ageMin.setText(ageMin1);
        locationName.setText(eventLocation);
        ageMax.setText(ageMax1);

        if (spinnerId == MAXSIZE){
            //then we also update the age custom
            ageCustom.setText(String.valueOf(groupSize1));
        }
        spinner.setSelection(spinnerId);
    }

    private void checkValidity() {

        if (startDate == null || endDate == null){
            //then we display the error message
            errorDialogBox("Invalid Timing", "Please select valid start and end times");
        }
        //With the remaining, ltee' sjust set error hint s
        EditText getLocation;
        EditText minAge;
        EditText maxAge;

        getLocation = locationLogistics.getLocationVal();
        minAge = locationLogistics.getAgeMin();
        maxAge = locationLogistics.getAgeMax();
        EditText title = groupLogistics.getEventTitle();

        int minimumAge = 0;
        int maximumAge = 0;
        if (minAge.getText().toString().length() != 0){
            minimumAge = Integer.parseInt(minAge.getText().toString());
        }
        if (maxAge.getText().toString().length() != 0){
            maximumAge = Integer.parseInt((maxAge.getText().toString()));
        }

        //We want to check that the user did initiate a minimum age that is greater than the maximum age \s
        if (minimumAge > maximumAge){
            //set the error
            minAge.setError("Please enter a minimum age smaller than the maximum age");
            minAge.setText("");
            maxAge.setText("");
            minAge.requestFocus();

        }

        //Set the paramaters to hte length of the appropriate values for hte length if it is zero than we want ot
        if (minAge.getText().toString().length() == 0){
            minAge.setError("Please enter a minimum age");
        }
         if (maxAge.getText().toString().length() == 0) {
             maxAge.setError("Please enter a maximum age");
         }

        if (title.getText().toString().length() == 0) {
            title.setError("Please enter an event title");

        }
            if (getLocation.getText().toString().trim().length() == 0) {
                getLocation.setError("Please enter a location");
        }
    }


 /*   private void createLocationLogisticsFrag(){
        NestedScrollView scroll = findViewById(R.id.nestedScrollView);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        Fragment locationLogistics = new LocationLogistics();
        fragmentTransaction.add(scroll.getId(),locationLogistics,"Location Logistics Fragment");
        fragmentTransaction.commit();
    }*/
/*    private void createGroupLogisticsFrag() {
        LinearLayout scroll = findViewById(R.id.linearLayout);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        Fragment groupLogistics = new GroupLogistics();
        fragmentTransaction.add(scroll.getId(),groupLogistics,"Group Logistics Fragment");
        fragmentTransaction.commit();

    }*/

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
            startDate = new Date(year,month,dayOfMonth);
            displayTimeDialogFragment(STARTTIME);

        }
        else{
            endDate = new Date(year,month,dayOfMonth);
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
