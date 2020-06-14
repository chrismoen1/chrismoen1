package com.example.newcomer_io.ui.main.Onboarding;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.example.newcomer_io.R;

public class ProfileInformation extends AppCompatActivity {

    private EditText firstName;
    private EditText lastName;
    private EditText schoolName;

    private Spinner faculty;
    private Spinner studyYear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_information);
        firstName = findViewById(R.id.firstName);
        lastName = findViewById(R.id.editText);
        schoolName = findViewById(R.id.schoolName);

        setEditListener(firstName);
        setEditListener(lastName);
        setEditListener(schoolName);

        String[] faculty_Strs = {"Arts", "Business","Engineering", "Science","Other"};
        String[] year_Strs = {"1st Year","2nd Year", "3rd Year", "4th Year", "5th Year", "6th Year"};
        ArrayAdapter<String> faculty_Adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,faculty_Strs);

        //faculty.set

        //String[] preferredSubjects = {""}
    }

    private void setEditListener(final EditText text) {

        text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //Then we update the colour of the view paramaters by changing the colour of the border
                text.setBackgroundResource(R.drawable.text_backdrop_selected);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {


            }
        });
    }

    public EditText getLastName() {
        return lastName;
    }

    public void setLastName(EditText lastName) {
        this.lastName = lastName;
    }

    public EditText getFirstName() {
        return firstName;
    }

    public void setFirstName(EditText firstName) {
        this.firstName = firstName;
    }

    public EditText getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(EditText schoolName) {
        this.schoolName = schoolName;
    }

    public Spinner getFaculty() {
        return faculty;
    }

    public void setFaculty(Spinner faculty) {
        this.faculty = faculty;
    }

    public Spinner getStudyYear() {
        return studyYear;
    }

    public void setStudyYear(Spinner studyYear) {
        this.studyYear = studyYear;
    }
}
