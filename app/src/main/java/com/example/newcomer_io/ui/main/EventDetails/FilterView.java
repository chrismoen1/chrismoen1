package com.example.newcomer_io.ui.main.EventDetails;

import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.example.newcomer_io.R;

public class FilterView extends AppCompatActivity {
    //This class will incorporate the current filter for holding all of the values related to the filtering view

    private EditText now;
    private EditText future;
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_view);
    }
}
