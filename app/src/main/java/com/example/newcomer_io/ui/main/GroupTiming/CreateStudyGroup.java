package com.example.newcomer_io.ui.main.GroupTiming;

import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.*;
import android.view.inputmethod.EditorInfo;
import android.widget.*;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.fragment.app.DialogFragment;
import com.crystal.crystalrangeseekbar.interfaces.OnSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.widgets.CrystalSeekbar;
import com.example.newcomer_io.R;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CreateStudyGroup extends AppCompatActivity implements CalendarDialogFragment.OnClickDate, TimePickerFragment.OnInputListener {

    private static final int AUTOCOMPLETE_REQUEST_CODE = 1;
    private static final int TYPE_FILTER_ESTABLISHMENT = 34;
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
        activitateLocationLogistics(LocationLogistics);
        activateSubjectLogistics(SubjectLogistics);

    }

    private void activateSubjectLogistics(View subjectLogistics) {
        final CheckBox other = subjectLogistics.findViewById(R.id.other);
        final LinearLayout studyGroupType = subjectLogistics.findViewById(R.id.studyGroupType );
        final int otherID = 10;
        FloatingActionButton floatingActionButton = subjectLogistics.findViewById(R.id.floatingActionButton);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Check all params
                if (getStageConfirmation(1) && getStageConfirmation(2) && getStageConfirmation(3)){
                    //Then we go forth with creating the group
                }
            }
        });

        other.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){

                    //Then we create the other field
                    final EditText theme = new EditText(getApplicationContext());

                    theme.setHint("Enter A Theme");
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    layoutParams.gravity = Gravity.CENTER;
                    theme.setLayoutParams(layoutParams);
                    theme.setId(otherID);
                    studyGroupType.addView(theme);

                    theme.setImeOptions(EditorInfo.IME_ACTION_DONE);

                    theme.setSingleLine(true);
                    theme.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                        @Override
                        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                            other.setText(theme.getText().toString());
                            studyGroupType.removeView(theme);
                            return false;
                        }
                    });
                }else{

                    try{
                        EditText theme = studyGroupType.findViewById(otherID);

                        if (theme != null){
                            studyGroupType.removeView(theme);
                        }

                    } catch (Exception e) {
                         e.printStackTrace();

                    }

                }
            }
        });
    }

    private void activitateLocationLogistics(View locationLogistics) {
        EditText locationName = locationLogistics.findViewById(R.id.location);
        EditText meetingDetails = locationLogistics.findViewById(R.id.meetingNotes);
        final TextView groupSizeNum = locationLogistics.findViewById(R.id.groupSizeNum);

        meetingDetails.setImeOptions(EditorInfo.IME_ACTION_DONE);
        meetingDetails.setSingleLine(false);

        CrystalSeekbar groupSize = locationLogistics.findViewById(R.id.groupSize);

        groupSize.setMinValue(3);
        groupSize.setMinStartValue(6);
        groupSize.setMaxValue(15);
        groupSize.apply();

        locationName.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                //then we will activiate the google places to get the name fo the location that the event will take place
                if (event.getAction() == MotionEvent.ACTION_DOWN){

                    callPlacesIntent();
                    if (getStageConfirmation(2)){
                        setStage_Highlight(3);
                    }
                }
                return false;
            }
        });
        groupSize.setOnSeekbarChangeListener(new OnSeekbarChangeListener() {
            @Override
            public void valueChanged(Number value) {
                //Then we update the text view of the number being displayed on the seekbar
                int progress= value.intValue();
                groupSizeNum.setText("Up to " + String.valueOf(progress) + " people");
                if (getStageConfirmation(2)){
                    setStage_Highlight(3);
                }
            }
        });
        meetingDetails.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (getStageConfirmation(2)){
                    setStage_Highlight(3);
                }
                return false;
            }
        });
        
    }
    private void callPlacesIntent() {
        //Set the paramaters as needed
        Places.initialize(this.getApplicationContext(), "AIzaSyAjGcF4XC-OEVJHKPmPefDUxGjxiSCbFK8");
        PlacesClient placesClient = Places.createClient(this.getApplicationContext());
        List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME,Place.Field.LAT_LNG);
// autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME,Place.Field.LAT_LNG));
        // Start the autocomplete intent.
        Intent intent = new Autocomplete.IntentBuilder(
                AutocompleteActivityMode.FULLSCREEN, fields)
                .setTypeFilter(TypeFilter.ESTABLISHMENT)
                .build(this);
        this.startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);
/*        // Initialize the AutocompleteSupportFragment.
        this.autocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.autocomplete1);
        this.autocompleteFragment.getView().setVisibility(View.INVISIBLE);*/

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place asd = Autocomplete.getPlaceFromIntent(data);
                LatLng location = asd.getLatLng();
                
                String name = asd.getName();
                
                Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                List<Address> addresses = null;

                try {
                    addresses = geocoder.getFromLocation(location.latitude,location.longitude, 1);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                String cityName = addresses.get(0).getAddressLine(0);
                View locationLogistics = getLocationLogistics();
                EditText locationName = locationLogistics.findViewById(R.id.location);
                locationName.setText(name);
                //locationLogistics.setLocationName(cityName);
                //locationLogistics.setChecked(false);

            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                // TODO: Handle the error.
                Status status = Autocomplete.getStatusFromIntent(data);
            }
        }
    }//onActivityResult

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

        }else if(stageNum == 2){
        //Otherwise if all of hte values on stage 2 are correct then we can proceed
            View locationLogistics = getLocationLogistics();
            EditText location = locationLogistics.findViewById(R.id.location);
            EditText meetingNotes = locationLogistics.findViewById(R.id.meetingNotes);

            if (location.getText().toString().equals("") == false && meetingNotes.getText().toString().equals("") == false){
                return true;
            }else{
                return false;
            }
        }
        else{
            View subjectLogistics = getSubjectLogistics();
            CheckBox finalExam = subjectLogistics.findViewById(R.id.finalExam);
            CheckBox problemSets = subjectLogistics.findViewById(R.id.problemSets);
            CheckBox midTerm = subjectLogistics.findViewById(R.id.midterm);
            CheckBox quizzes = subjectLogistics.findViewById(R.id.quizzes);

            EditText subject = subjectLogistics.findViewById(R.id.subject);
            if ((finalExam.isChecked() || problemSets.isChecked() || midTerm.isChecked() || quizzes.isChecked()) && subject.getText().toString().equals("") == false){
                //Then we will navigate to the next screen
                return true;
            }
            else{
                return false;
            }

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
