package com.example.newcomer_io.ui.main.GroupTiming;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;
import com.example.newcomer_io.R;
import com.example.newcomer_io.ui.main.GroupTiming.CreateGroup;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Arrays;
import java.util.List;

public class LocationLogistics extends CreateGroup {
    private static final int MAXSPINNER = 5;
    private static final int RESULT_OK = 1 ;
    private FloatingActionButton floatingActionButton;
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
    private String[] arr_groupSize_name;
    private int[] arr_groupSize;
    private ArrayAdapter<String> arrayAdapter;

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
        floatingActionButton = fragment_groupLocation.findViewById(R.id.floatingActionButton);
        eventNotes = fragment_groupLocation.findViewById(R.id.editText2);
        ageCustom = fragment_groupLocation.findViewById(R.id.ageCustom);

        ageCustom.setVisibility(View.INVISIBLE);
        arr_groupSize_name = new String[]{"3 People", "4 People", "5 People ", "6 People", "7 People", "Enter a size"};
        arr_groupSize = new int[]{3,4,5,6,7};
        arrayAdapter = new ArrayAdapter<String>(context,android.R.layout.simple_list_item_1,arr_groupSize_name);

        groupNumber.setAdapter(arrayAdapter);

       // initializeGroupSize();
        //View fragment_groupLogistics = inflater.inflate(R.layout.fragment_group_logistics, scroll, true);
    }

    public ArrayAdapter<String> getArrayAdapter(){return this.arrayAdapter; }
    public Spinner getGroupSpinner(){return this.groupNumber; }
    public int[] getArr_groupSize(){return this.arr_groupSize; }
        public EditText getAgeCustom() {
        return ageCustom;
    }
    public String[] getArr(){return this.arr_groupSize_name;}

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

    public int getMaxSpinnerSize() {
        return this.MAXSPINNER;
    }

    public int getSpinnerId(int groupSize1) {
        for (int i =0 ; i < this.arr_groupSize.length;i++){
            if (arr_groupSize[i] == groupSize1){
                return i;
            }
        }
        return MAXSPINNER;
    }

    public FloatingActionButton getFloatingActionButton() {
        return floatingActionButton;
    }

    public void setFloatingActionButton(FloatingActionButton floatingActionButton) {
        this.floatingActionButton = floatingActionButton;
    }
}