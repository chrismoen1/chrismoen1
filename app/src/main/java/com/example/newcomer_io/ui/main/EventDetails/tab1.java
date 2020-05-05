package com.example.newcomer_io.ui.main.EventDetails;

import android.app.Activity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.newcomer_io.R;
import com.example.newcomer_io.ui.main.LocationSettings.TrendingContent;
import com.example.newcomer_io.ui.main.UserDetails.EventCreate;

import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link tab1#newInstance} factory method to
 * create an instance of this fragment.
 */
public class tab1 extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private EventCreate eventCreate;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private TrendingContent content;

    public tab1(EventCreate eventCreate) {
        // Required empty public constructor
        //this.content = content;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment tab1.
     */
    // TODO: Rename and change types and number of parameters

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //    * @param   year    the year minus 1900.
        //     * @param   month   the month between 0-11.
        //     * @param   date    the day of the month between 1-31.
        //     * @param   hrs     the hours between 0-23.
        //     * @param   min     the minutes between 0-59.


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View inflate = inflater.inflate(R.layout.fragment_tab1, container, false);
        LinearLayout scrollView = inflate.findViewById(R.id.scrollLayout); //This represents the scroll for all of the posts



       /* for (int i =0 ; i < 3;i++){
            View trending_content = inflater.inflate(R.layout.user_row, null);
            ConstraintLayout cardr = trending_content.findViewById(R.id.constraintLayout);
            cardr.setId(i);
            trending_content.setId(i);

            scrollView.addView(trending_content);

        }*/


        eventCreate.addPost("John Dobalina","You mama is so fat, that one day she went to the store to go to the store", scrollView);
        eventCreate.addPost("Joe Smoe", "Hi guys!!!!", scrollView);

        return inflate;
    }
}
