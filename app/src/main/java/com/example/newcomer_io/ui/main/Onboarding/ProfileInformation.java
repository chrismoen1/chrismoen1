package com.example.newcomer_io.ui.main.Onboarding;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.example.newcomer_io.R;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class ProfileInformation extends AppCompatActivity {

    private EditText firstName;
    private EditText lastName;
    private EditText schoolName;
    private ArrayList<CheckBox> checkBoxArrayList;
    private LinearLayout subjectName;
    private Button saveChanges;

    private LinearLayout faculty_Container;
    private LinearLayout year_Container;

    private Spinner faculty;
    private Spinner studyYear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_information);
        firstName = findViewById(R.id.firstName);
        lastName = findViewById(R.id.editText);
        schoolName = findViewById(R.id.schoolName);
        subjectName = findViewById(R.id.subjectName);

        faculty_Container = findViewById(R.id.facultyContainer);
        year_Container = findViewById(R.id.yearContainer);
        saveChanges = findViewById(R.id.saveChanges);

        faculty = findViewById(R.id.faculty);
        studyYear = findViewById(R.id.year_Spinner);

        checkBoxArrayList = new ArrayList<CheckBox>();
        checkBoxArrayList.add((CheckBox) findViewById(R.id.psychology));
        checkBoxArrayList.add((CheckBox) findViewById(R.id.accounting));
        checkBoxArrayList.add((CheckBox) findViewById(R.id.mathematics));
        checkBoxArrayList.add((CheckBox) findViewById(R.id.chemistry));
        checkBoxArrayList.add((CheckBox) findViewById(R.id.computerScience));
        checkBoxArrayList.add((CheckBox) findViewById(R.id.engineering));
        checkBoxArrayList.add((CheckBox) findViewById(R.id.statistics));
        checkBoxArrayList.add((CheckBox) findViewById(R.id.biology));
        checkBoxArrayList.add((CheckBox) findViewById(R.id.physics));
        checkBoxArrayList.add((CheckBox) findViewById(R.id.physiology2));
        checkBoxArrayList.add((CheckBox) findViewById(R.id.economics));

        for (int i =0; i < checkBoxArrayList.size();i++){
            //Then we get the current checkbox and set hte checkbox listener
            setCheckBoxListener(checkBoxArrayList.get(i));
        }

        setEditListener(firstName);
        setEditListener(lastName);
        setEditListener(schoolName);

        setSaveButtonListener(saveChanges);

        //Set the adapters for the spinner drop downs that we will display on the profile information page
        String[] faculty_Strs = {"Arts", "Business","Engineering", "Science","Other"};
        String[] year_Strs = {"1st Year","2nd Year", "3rd Year", "4th Year", "5th Year", "6th Year"};

        ArrayAdapter<String> faculty_Adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item,faculty_Strs);
        ArrayAdapter<String> years_Adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,year_Strs);

        faculty.setAdapter(faculty_Adapter);
        studyYear.setAdapter(years_Adapter);

        setSpinnerListener(faculty, faculty_Container);
        setSpinnerListener(studyYear, year_Container);

        faculty_Container.setBackgroundResource(R.drawable.rounded_border_deselected);
        year_Container.setBackgroundResource(R.drawable.rounded_border_deselected);

    }
    private void setSaveButtonListener(Button button){
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText firstName = getFirstName();
                EditText lastName = getLastName();
                EditText schoolName = getSchoolName();

                if (firstName.getText().toString().isEmpty()){
                    //Then we do not go to the next page and instead give a request focus and error on those names stating that they need to have
                    //values inputted into it
                    firstName.requestFocus();
                    firstName.setError("Please enter your first name");
                }
                if (lastName.getText().toString().isEmpty()){
                    lastName.requestFocus();
                    lastName.setError("Please enter your last name");
                }
                if (schoolName.getText().toString().isEmpty()){
                   schoolName.requestFocus();
                   schoolName.setError("Please enter a school name");
                }

            }
        });
    }

    private void setSpinnerListener(final Spinner faculty, final LinearLayout container) {
        faculty.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                container.setBackgroundResource(R.drawable.rounded_border);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                container.setBackgroundResource(R.drawable.rounded_border_deselected);
            }
        });
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
                if (s.toString().length() == 0){
                    text.setBackgroundResource(R.drawable.text_backdrop);
                }

            }
        });
    }

    public void setCheckBoxListener(CheckBox currCheckBox){
        currCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
               checkArrayBox_Colour(getCheckBoxArrayList());
            }
        });
    }

    private void checkArrayBox_Colour(ArrayList<CheckBox> checkBoxArrayList) {
        boolean flag = false;
        for (int i =0; i < checkBoxArrayList.size();i++){
            CheckBox checkBox = checkBoxArrayList.get(i);
            if (checkBox.isChecked()){
                getSubjectName().setBackgroundResource(R.drawable.rounded_border);
                flag = true;
            }
        }
        if (flag == false) {
            //then we set it back to colour
            getSubjectName().setBackgroundResource(R.drawable.rounded_border_deselected);
        }
    }

    private void updateLayoutColor(LinearLayout linearLayout) {
        linearLayout.setBackgroundResource(R.drawable.rounded_border);
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
    public ArrayList<CheckBox> getCheckBoxArrayList(){
        return this.checkBoxArrayList;
    }

    public LinearLayout getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(LinearLayout subjectName) {
        this.subjectName = subjectName;
    }

    public LinearLayout getFaculty_Container() {
        return faculty_Container;
    }

    public void setFaculty_Container(LinearLayout faculty_Container) {
        this.faculty_Container = faculty_Container;
    }

    public LinearLayout getYear_Container() {
        return year_Container;
    }

    public void setYear_Container(LinearLayout year_Container) {
        this.year_Container = year_Container;
    }

    public Button getSaveChanges() {
        return saveChanges;
    }

    public void setSaveChanges(Button saveChanges) {
        this.saveChanges = saveChanges;
    }
}
