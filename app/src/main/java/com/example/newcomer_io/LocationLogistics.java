package com.example.newcomer_io;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;

import java.util.Arrays;
import java.util.List;

public class LocationLogistics extends CreateGroup{
    private static final int MAXSPINNER = 5;
    private static final int RESULT_OK = 1 ;
    //This class holds all of the relevant information to the group location logistics information box
    private View fragment_groupLocation;
    int AUTOCOMPLETE_REQUEST_CODE = 1;
    private boolean checked;
    private EditText locationName;
    private EditText eventNotes;
    private EditText ageCustom;
    private LatLng place;

    private Spinner groupNumber;

    private EditText minAge;
    private EditText maxAge;

    private Activity context;

    public LocationLogistics(Activity context){
        LinearLayout scroll = context.findViewById(R.id.layout);
        this.context = context;


        LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);

        fragment_groupLocation = inflater.inflate(R.layout.fragment_location_logistics,scroll,true);
        checked = false;
        //Initialize all of the places
        locationName = fragment_groupLocation.findViewById(R.id.editText4);

        minAge = fragment_groupLocation.findViewById(R.id.minAge);
        maxAge = fragment_groupLocation.findViewById(R.id.maxAge);

        groupNumber = fragment_groupLocation.findViewById(R.id.spinner);

        eventNotes = fragment_groupLocation.findViewById(R.id.editText2);
        ageCustom = fragment_groupLocation.findViewById(R.id.ageCustom);

        ageCustom.setVisibility(View.INVISIBLE);
        String[] arr = {"3 People","4 People","5 People ","6 People", "7 People", "Enter a size"};
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(context,android.R.layout.simple_list_item_1,arr);
        groupNumber.setAdapter(arrayAdapter);

        initializeGroupSize();
        //View fragment_groupLogistics = inflater.inflate(R.layout.fragment_group_logistics, scroll, true);
    }

    private void initializeGroupSize() {
        groupNumber.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == MAXSPINNER){//aka the last value in the spinner
                    //Then we display the edit text
                   ageCustom.setVisibility(View.VISIBLE);
                }
                else{
                    //Then we hide
                    ageCustom.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    public View getFragment_groupLocation_View(){
        return this.fragment_groupLocation;
    }
    public EditText locationName(){
        return locationName;
    }
    /*private void initializePlacesComplete() {
        locationName.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    //callPlacesIntent();
                    Intent intent = new Intent(context, LocationType.class);
                    startActivity(intent);
                }
                return false;
            }
        });

    }*/
    public EditText getLocationVal() {
        //This gets the location value of the edit text paramater
        return locationName;
    }
    public EditText getEventNotes(){
        return eventNotes;
    }
    private void callPlacesIntent() {
        //Set the paramaters as needed
        Places.initialize(context.getApplicationContext(), "AIzaSyAjGcF4XC-OEVJHKPmPefDUxGjxiSCbFK8");
        PlacesClient placesClient = Places.createClient(context.getApplicationContext());
        List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME,Place.Field.LAT_LNG);
// autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME,Place.Field.LAT_LNG));
        // Start the autocomplete intent.
        Intent intent = new Autocomplete.IntentBuilder(
                AutocompleteActivityMode.FULLSCREEN, fields)
                .setTypeFilter(TypeFilter.ADDRESS)
                .build(context);
        context.startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);
/*        // Initialize the AutocompleteSupportFragment.
        this.autocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.autocomplete1);
        this.autocompleteFragment.getView().setVisibility(View.INVISIBLE);*/

    }
    public void setLocationName(String txt){
        this.locationName.setText(txt);
    }
    public void setLocation(LatLng latLng){
        this.place = latLng;
    }
    public void setChecked(boolean value){
        this.checked = value;
    }

    public EditText getAgeMin() {
        return minAge;
    }
    public EditText getAgeMax(){
        return maxAge;
    }

}