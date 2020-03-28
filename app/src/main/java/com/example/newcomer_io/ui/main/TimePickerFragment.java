package com.example.newcomer_io.ui.main;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.TimePicker;
import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import com.example.newcomer_io.R;

import java.util.Calendar;

public class TimePickerFragment extends DialogFragment
        implements TimePickerDialog.OnTimeSetListener {
    private String action_type;
    public TimePickerFragment(String paramType) {
        this.action_type = paramType;
    }

    private OnInputListener mListener;

    @Override
    public void onAttach(@NonNull Context context) {
        mListener = (TimePickerFragment.OnInputListener)context;
        super.onAttach(context);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current time as the default values for the picker
        //mListener = (TimePickerFragment.OnInputListener) TimePickerFragment.this;

        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        // Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(getActivity(), R.style.TimePicker, this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        mListener.sendDate_clock(view,hourOfDay,minute,action_type);
    }

    public interface OnInputListener {
        // TODO: Update argument type and name
        void sendDate_clock(TimePicker view, int hourOfDay, int minute, String paramType);

    }

}