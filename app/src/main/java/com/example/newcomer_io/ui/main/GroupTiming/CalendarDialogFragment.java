package com.example.newcomer_io.ui.main.GroupTiming;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import com.example.newcomer_io.R;

import java.util.Calendar;
import java.util.Date;

public class CalendarDialogFragment extends DialogFragment {

    private CalendarView calendarView;
    private long HOUR = 3600;
    OnClickDate mListener;
    private String action_type;


    public CalendarDialogFragment(String starttime) {
    this.action_type = starttime;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if (context != null){
            mListener = (OnClickDate) context;
        }
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             Bundle savedInstances) {

        View v = inflater.inflate(R.layout.calendar_dialog, container, false);
        calendarView = v.findViewById(R.id.calendarView);
        setCalendarTimeFrame();
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                mListener.sendDate(year,month,dayOfMonth,action_type);
                getDialog().dismiss();
            }
        });

        return v;
    }

    public void setCalendarTimeFrame() {
        Date currentTime= Calendar.getInstance().getTime();
        //then that means they have specified the start time
        long time = currentTime.getTime() + (1000* 3600 * 24 * 14);
        calendarView.setDate(currentTime.getTime(), false,true);
        calendarView.setMaxDate(time);
        calendarView.setMinDate(currentTime.getTime());
    }
    public String getAction_type() {
        return action_type;
    }

    public void setAction_type(String action_type) {
        this.action_type = action_type;
    }

    public interface OnClickDate{
        void sendDate(int year, int month, int dayOfMonth, String type);
    }
}