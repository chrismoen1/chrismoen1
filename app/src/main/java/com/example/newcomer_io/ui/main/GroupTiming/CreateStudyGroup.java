package com.example.newcomer_io.ui.main.GroupTiming;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.example.newcomer_io.R;

public class CreateStudyGroup extends AppCompatActivity {

    private View GroupTiming;
    private View LocationLogistics;
    private View SubjectLogistics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_study_group);

        LinearLayout scroll = findViewById(R.id.scrollView);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(this.LAYOUT_INFLATER_SERVICE);
        GroupTiming = inflater.inflate(R.layout.fragment_group_logistics, scroll, true);
        LocationLogistics = inflater.inflate(R.layout.fragment_location_logistics,scroll,true);
        SubjectLogistics = inflater.inflate(R.layout.fragment_group_subject_logistics,scroll,true);

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
}
